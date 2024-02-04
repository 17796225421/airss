package com.example.controller;

import com.example.model.RssSource;
import com.example.service.RssSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rss")
public class RssController {
    @Autowired
    private RssSourceService rssSourceService;

    // 添加rss源
    @PostMapping("/add")
    public String addRssSource(@RequestBody RssSource rssSource) {
        rssSourceService.addRssSource(rssSource);
        return "Rss source added successfully!";
    }

    // 删除rss源，使用id作为参数
    @DeleteMapping("/delete/{id}")
    public String deleteRssSource(@PathVariable int id) {
        rssSourceService.deleteRssSource(id);
        return "Rss source deleted successfully!";
    }

    // 查看所有rss源，返回RssSource列表
    @GetMapping("/all")
    public List<RssSource> getAllRssSources() {
        return rssSourceService.getAllRssSources();
    }

    // 获取指定rss源，使用id作为参数
    @GetMapping("/get/{id}")
    public String getRssSource(@PathVariable int id) {
        return rssSourceService.getRssSourceXmlText(id);
    }
}