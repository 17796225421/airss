package com.example.controller;

import com.example.model.RssSource;
import com.example.model.RssSourceWithoutXmlDataDTO;
import com.example.service.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RssController 控制器，处理前端关于 RSS 源的请求
 */
@RestController
@RequestMapping("/rss")
public class RssController {

    @Autowired
    private RssService rssService;

    /**
     * 添加 RSS 源
     * @param rssSource RSS 源
     * @return 添加成功的记录数
     */
    @PostMapping("/add")
    public int addRssSource(@RequestBody RssSource rssSource) {
        return rssService.addRssSource(rssSource);
    }

    /**
     * 删除 RSS 源
     * @param rssId RSS 源的 ID
     * @return 删除成功的记录数
     */
    @DeleteMapping("/delete/{rssId}")
    public int deleteRssSource(@PathVariable int rssId) {
        return rssService.deleteRssSource(rssId);
    }

    /**
     * 获取所有的 RSS 源（不包括 XML 数据）
     * @return 所有的 RSS 源的列表
     */
    @GetMapping("/getAll")
    public List<RssSourceWithoutXmlDataDTO> getAllRssSourcesWithoutXmlData() {
        return rssService.getAllRssSourcesWithoutXmlData();
    }

    /**
     * 获取 RSS 源的 XML 数据
     * @param rssId RSS 源的 ID
     * @return XML 数据
     */
    @GetMapping("/getXmlData/{rssId}")
    public String getRssSourcesXmlData(@PathVariable int rssId) {
        return rssService.getRssSourcesXmlData(rssId);
    }
}