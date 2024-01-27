package com.example.controller;

import com.example.service.RssSourceManager;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class RssController {
    private final RssSourceManager rssSourceManager;

    public RssController(RssSourceManager rssSourceManager) {
        this.rssSourceManager = rssSourceManager;
    }

    @PostMapping("/addRss")
    public ResponseEntity<String> addRss(@RequestParam String rssUrl, @RequestParam String rssName) {
        rssSourceManager.addRssSource(rssUrl, rssName);
        return ResponseEntity.ok("RSS源已添加");
    }

    @GetMapping("/viewRss")
    public ResponseEntity<List<String>> viewRss() {
        // 这里我们先返回一个空列表
        return ResponseEntity.ok(Collections.emptyList());
    }

    @DeleteMapping("/deleteRss")
    public ResponseEntity<String> deleteRss(@RequestBody String rssUrl) {
        // 这里我们先返回一个默认的响应
        return ResponseEntity.ok("删除RSS源的功能尚未实现");
    }

    @GetMapping("/rss/{rssName}")
    public ResponseEntity<Resource> getRss(@PathVariable String rssName) {
        // 获取RSS源文件
        Resource rssFile = rssSourceManager.getRssFile(rssName);
        if (rssFile == null) {
            // 如果找不到RSS源文件，返回404 Not Found
            return ResponseEntity.notFound().build();
        } else {
            // 如果找到RSS源文件，返回文件内容
            return ResponseEntity.ok(rssFile);
        }
    }

}
