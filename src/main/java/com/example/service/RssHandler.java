package com.example.service;

import java.nio.file.Files;
import com.example.model.RssSource;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RssHandler {
    private ScheduledExecutorService executorService;
    private AIHandler aiHandler;
    private Map<RssSource, ScheduledFuture<?>> futureMap; // 存储每个RssSource对应的ScheduledFuture

    public RssHandler(AIHandler aiHandler) {
        this.aiHandler = aiHandler;
        this.executorService = Executors.newScheduledThreadPool(5);
        this.futureMap = new HashMap<>();
    }

    public void bindAndStart(RssSource rssSource) {
        // 创建一个SyndFeed对象
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(rssSource.getName());
        feed.setLink(rssSource.getUrl());
        feed.setDescription("无");
        feed.setEntries(new ArrayList<>());

        // 创建一个XML文件
        File file = new File("rssFiles/" + rssSource.getId() + ".xml");
        if (!file.exists()) { // 如果文件不存在
            try (Writer writer = new FileWriter(file)) {
                new SyndFeedOutput().output(feed, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 创建一个Runnable任务，该任务会周期性地读取RSS源的所有item，并处理未处理过的item
        Runnable task = () -> {
            try {
                URL feedUrl = new URL(rssSource.getUrl());
                SyndFeedInput input = new SyndFeedInput();
                List<SyndEntry> items = input.build(new XmlReader(feedUrl)).getEntries();
                List<SyndEntry> newEntries = new ArrayList<>();
                System.out.println("更新开始");

                // 读取XML文件内容
                String xmlContent = new String(Files.readAllBytes(file.toPath()));

                for (SyndEntry syndEntry : items) {
                    String link = syndEntry.getLink();
                    // 如果item未被处理过，则进行处理
                    if (!xmlContent.contains(link)) {
                        SyndEntry newEntry;
                        if (rssSource.getType().equals(RssSource.Type.ARTICLE)) {
                            newEntry = aiHandler.handleArticle(syndEntry);
                        } else {
                            newEntry = aiHandler.handleVideo(syndEntry);
                        }
                        newEntries.add(newEntry);
                    }
                }
                // 更新XML文件
                synchronized (file) {
                    SyndFeed oldFeed = new SyndFeedInput().build(file);
                    List<SyndEntry> oldEntries = oldFeed.getEntries();
                    oldEntries.addAll(newEntries);
                    try (Writer writer = new FileWriter(file)) {
                        new SyndFeedOutput().output(oldFeed, writer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("更新结束");
            } catch (IOException | IllegalArgumentException | FeedException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        };

        // 将任务绑定到一个线程上，并启动线程
        // 该线程会每隔一定时间（例如，60秒）执行一次任务
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(task, 0, 60*10, TimeUnit.SECONDS);
        // 将RssSource和对应的ScheduledFuture存入futureMap
        futureMap.put(rssSource, future);
    }

    // 关闭对应的处理线程并解除绑定
    public void unbindAndStop(RssSource rssSource) {
        // 从futureMap中获取RssSource对应的ScheduledFuture
        ScheduledFuture<?> future = futureMap.get(rssSource);
        if (future != null) {
            // 停止任务
            future.cancel(true);
            // 从futureMap中移除这个RssSource
            futureMap.remove(rssSource);
        }
    }
}