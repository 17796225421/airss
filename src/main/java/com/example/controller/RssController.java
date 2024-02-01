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

    // 删除rss源
    @DeleteMapping("/delete/{name}")
    public String deleteRssSource(@PathVariable String name) {
        rssSourceService.deleteRssSource(name);
        return "Rss source deleted successfully!";
    }

    // 查看所有rss源
    @GetMapping("/all")
    public List<String> getAllRssSourcesName() {
        return rssSourceService.getAllRssSourcesName();
    }

    // 获取指定rss源文件
    @GetMapping("/get/{name}")
    public RssSource getRssSource(@PathVariable String name) {
        return rssSourceService.getRssSource(name);
    }
}