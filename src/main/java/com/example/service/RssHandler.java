package com.example.service;

import com.example.model.RssSource;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

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
        // 创建一个哈希表来存储已处理过的item的链接
        Set<String> processedLinks = new HashSet<>();

        // 创建一个Runnable任务，该任务会周期性地读取RSS源的所有item，并处理未处理过的item
        Runnable task = () -> {
            try {
                URL feedUrl = new URL(rssSource.getUrl());
                SyndFeedInput input = new SyndFeedInput();
                List<SyndEntry> items = input.build(new XmlReader(feedUrl)).getEntries();
                for (SyndEntry item : items) {
                    String link = item.getLink();
                    // 如果item未被处理过，则进行处理
                    if (!processedLinks.contains(link)) {
                        if (rssSource.getType().equals(RssSource.Type.ARTICLE)) {
                            //aiHandler.handleArticle(item);
                        } else {
                            //aiHandler.handleVideo(item);
                        }
                        // 将item的链接添加到已处理链接的哈希表中
                        processedLinks.add(link);
                    }
                }
            } catch (IOException | IllegalArgumentException | FeedException e) {
                e.printStackTrace();
            }
        };

        // 将任务绑定到一个线程上，并启动线程
        // 该线程会每隔一定时间（例如，60秒）执行一次任务
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);
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