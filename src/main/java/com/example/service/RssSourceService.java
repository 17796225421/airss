package com.example.service;

import com.example.model.RssSource;

import java.util.ArrayList;
import java.util.List;

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
    public void deleteRssSource(String name) {
        RssSource toDelete = null;
        for (RssSource rssSource : rssSources) {
            if (rssSource.getName().equals(name)) {
                toDelete = rssSource;
                break;
            }
        }
        if (toDelete != null) {
            rssSources.remove(toDelete);
            rssHandler.unbindAndStop(toDelete);
        }
    }

    // 获取所有RssSource的名称
    public List<String> getAllRssSourcesName() {
        List<String> names = new ArrayList<>();
        for (RssSource rssSource : rssSources) {
            names.add(rssSource.getName());
        }
        return names;
    }

    // 获取指定名称的RssSource
    public RssSource getRssSource(String name) {
        for (RssSource rssSource : rssSources) {
            if (rssSource.getName().equals(name)) {
                return rssSource;
            }
        }
        return null;
    }
}