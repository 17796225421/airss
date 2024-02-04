package com.example.service;

import com.example.model.RssSource;
import com.example.repository.RssSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RssSourceService {
    private RssHandler rssHandler; // RssHandler对象，用于处理RssSource
    @Autowired
    private RssSourceRepository rssSourceRepository;

    public RssSourceService() {
        this.rssHandler = new RssHandler(new AIHandler());
    }

    // 添加一个RssSource，并启动对应的处理线程
    public void addRssSource(RssSource rssSource) {
        rssSourceRepository.save(rssSource);
        rssHandler.bindAndStart(rssSource);
    }

    // 删除一个RssSource，并关闭对应的处理线程
    public void deleteRssSource(int id) {
        // 从数据库中查找到对应的RssSource
        RssSource toDelete = rssSourceRepository.findById(id).orElse(null);
        if (toDelete != null) {
            // 关闭对应的处理线程
            rssHandler.unbindAndStop(toDelete);
            // 从数据库中删除这个RssSource
            rssSourceRepository.delete(toDelete);
        }
    }

    // 获取所有RssSource
    public List<RssSource> getAllRssSources() {
        return rssSourceRepository.findAll();
    }

    // 获取指定id的RssSource的xmlText
    public String getRssSourceXmlText(int id) {
        RssSource rssSource = rssSourceRepository.findById(id).orElse(null);
        if (rssSource != null) {
            return rssSource.getXmlText();
        }
        return null;
    }
}