package com.example.service;

import com.example.domain.RssSourceDomain;
import com.example.mapper.RssSourceMapper;
import com.example.model.RssSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class RssScheduleTask {
    @Autowired
    private RssSourceMapper rssSourceMapper;
    @Autowired
    private RssSourceDomain rssSourceDomain;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    // 使用 @Scheduled 注解定时任务，设置为每小时执行一次
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void updateRssSources() {
        log.debug("开始任务");
        // 从数据库查出所有的 rssId
        List<RssSource> rssSources = rssSourceMapper.selectAllRssSources();
        rssSources.forEach(rssSource -> {
            executor.submit(() -> rssSourceDomain.updateRssSource(rssSource));
        });
    }

    public void updateRssSourceImmediately(RssSource rssSource) {
        // 提交一个新的更新 RssSource 的任务给线程池
        executor.submit(() -> rssSourceDomain.updateRssSource(rssSource));
    }
}