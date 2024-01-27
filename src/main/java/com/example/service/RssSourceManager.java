package com.example.service;

import com.example.model.RssFeed;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class RssSourceManager {

    public void addRssSource(String url,String rssName) {
        RssFeed rssFeed = new RssFeed(url,rssName);

        // 创建并启动新的RssListenerThread线程
        RssListenerThread listenerThread = new RssListenerThread(rssFeed);
        listenerThread.start();

        // 将监听线程添加到RssFeed实例中
        rssFeed.setListenerThread(listenerThread);
    }

    public void deleteRssSource(String url) {
        return;
    }

    public Resource getRssFile(String rssName) {
        try {
            // 获取RSS源文件的路径
            String rssFilePath = "rss/" + rssName + ".xml";
            // 创建Resource对象
            Resource resource = new UrlResource(Paths.get(rssFilePath).toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + rssFilePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + rssName, e);
        }
    }
}
