package com.example.service;

import com.example.domain.RssSourceDomain;
import lombok.extern.slf4j.Slf4j;
import com.example.mapper.RssSourceMapper;
import com.example.model.RssSource;
import com.example.model.RssSourceWithoutXmlDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RssService 的实现类，完成对 RSS 源的增删查等操作
 */
@Service
@Slf4j
public class RssService {
    @Autowired
    private RssSourceMapper rssSourceMapper;
    @Autowired
    private RssSourceDomain rssSourceDomain;
    @Autowired
    private RssScheduleTask rssScheduleTask;

    /**
     * 添加 RSS 源
     * @param rssSource RSS 源
     * @return 添加成功的记录数
     */
    public int addRssSource(RssSource rssSource) {
        log.info("开始添加RSS源，源信息：{}", rssSource);
        rssSourceDomain.initRssXmlData(rssSource);
        int rows = rssSourceMapper.insertRssSource(rssSource);
        log.info("rssId:{}",rssSource.getRssId());
        rssScheduleTask.updateRssSourceImmediately(rssSource);
        log.info("添加RSS源结束，添加结果：{}", 1);
        return rows;
    }

    /**
     * 删除 RSS 源
     * @param rssId RSS 源的 ID
     * @return 删除成功的记录数
     */
    public int deleteRssSource(int rssId) {
        log.info("开始删除RSS源，源ID：{}", rssId);
        int result = rssSourceMapper.deleteRssSource(rssId);
        log.info("删除RSS源结束，删除结果：{}", result);
        return result;
    }

    /**
     * 获取所有的 RSS 源（不包括 XML 数据）
     * @return 所有的 RSS 源的列表
     */
    public List<RssSourceWithoutXmlDataDTO> getAllRssSourcesWithoutXmlData() {
        log.info("开始获取所有RSS源(不包括XML数据)");
        List<RssSourceWithoutXmlDataDTO> result = rssSourceMapper.selectAllRssSourcesWithoutXmlData();
        log.info("获取所有RSS源(不包括XML数据)结束，结果: {}", result);
        return result;
    }

    /**
     * 获取 RSS 源的 XML 数据
     * @param rssId RSS 源的 ID
     * @return XML 数据
     */
    public String getRssSourcesXmlData(int rssId) {
        log.info("开始获取RSS源的XML数据，源ID：{}", rssId);
        String result = rssSourceMapper.selectRssSourcesXmlData(rssId);
        log.info("获取RSS源的XML数据结束，结果：{}", result);
        return result;
    }
}