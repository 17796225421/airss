package com.example.service;

import com.example.model.RssFeed;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RssListenerThread extends Thread {
    private RssFeed rssFeed;

    public RssListenerThread(RssFeed rssFeed) {
        this.rssFeed = rssFeed;
    }

    @Override
    public void run() {
        Set<String> processedEntries = new HashSet<>();  // 用于存储已处理过的文章链接
        SyndFeedOutput output = new SyndFeedOutput();
        SyndFeed newFeed = new SyndFeedImpl();
        newFeed.setFeedType("rss_2.0");
        newFeed.setTitle("GPT Processed RSS Feed");
        newFeed.setDescription("This is a GPT processed RSS feed.");
        List<SyndEntry> newEntries = new ArrayList<>();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 获取RSS源的最新文章
                URL url = new URL(rssFeed.getUrl());
                URLConnection connection = url.openConnection();
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(connection));
                for (SyndEntry entry : feed.getEntries()) {
                    // 检查文章是否已经被处理过
                    if (!processedEntries.contains(entry.getLink())) {
                        // 处理文章
                        GptProcessor gptProcessor = new GptProcessor();
                        String processedArticle = gptProcessor.processArticle(entry.getDescription().getValue());
                        // 创建新的SyndEntry
                        SyndEntry newEntry = new SyndEntryImpl();
                        newEntry.setTitle(entry.getTitle());
                        newEntry.setLink(entry.getLink());
                        newEntry.setPublishedDate(entry.getPublishedDate());
                        SyndContent description = new SyndContentImpl();
                        description.setType("text/plain");
                        description.setValue(processedArticle);
                        newEntry.setDescription(description);
                        newEntries.add(newEntry);
                        // 将文章链接添加到已处理链接的集合中
                        processedEntries.add(entry.getLink());
                    }
                }
                newFeed.setEntries(newEntries);
                // 设置新RSS源的链接为原RSS源的链接
                newFeed.setLink(rssFeed.getUrl());
                // 在根目录下创建rss目录
                File rssDir = new File("rss");
                if (!rssDir.exists()) {
                    rssDir.mkdir();
                }
                // 将处理后的文章保存到文件中，文件名由rssName决定
                String rssFileName = rssFeed.getRssName() + ".xml";
                Writer writer = new FileWriter(rssDir.getPath() + "/" + rssFileName);
                output.output(newFeed, writer);
                writer.close();
                // 每隔一段时间检查一次RSS源
                Thread.sleep(60000);
            } catch (Exception e) {
                // 处理异常
                e.printStackTrace();
            }
        }
    }



}
