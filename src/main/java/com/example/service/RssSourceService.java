package com.example.service;

import com.example.model.RssSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssSourceService {
    private List<RssSource> rssSources; // 管理所有RssSource
    private RssHandler rssHandler; // RssHandler对象，用于处理RssSource

    public RssSourceService() {
        this.rssSources = new ArrayList<>();
        this.rssHandler = new RssHandler(new AIHandler());
    }

    // 添加一个RssSource，并启动对应的处理线程
    public void addRssSource(RssSource rssSource) {
        this.rssSources.add(rssSource);
        this.rssHandler.bindAndStart(rssSource);
    }

    // 删除一个RssSource，并关闭对应的处理线程
    public void deleteRssSource(int id) {
        RssSource toDelete = null;
        for (RssSource rssSource : rssSources) {
            if (rssSource.getId() == id) {
                toDelete = rssSource;
                break;
            }
        }
        if (toDelete != null) {
            rssSources.remove(toDelete);
            rssHandler.unbindAndStop(toDelete);
        }
    }

    // 获取所有RssSource
    public List<RssSource> getAllRssSources() {
        return rssSources;
    }

    // 获取指定id的RssSource
    public String getRssSource(int id) {
        try {
            Path path = Paths.get("rssFiles/" + id + ".xml");
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}