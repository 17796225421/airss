package com.example.service;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class AIHandler {

    private final RestTemplate restTemplate = new RestTemplate();

    public SyndEntry handleArticle(SyndEntry syndEntry) throws URISyntaxException {
        // 创建一个新的SyndEntry对象，复制原来的title、link和pubDate
        SyndEntry newEntry = new SyndEntryImpl();
        newEntry.setTitle(syndEntry.getTitle());
        newEntry.setLink(syndEntry.getLink());
        newEntry.setPublishedDate(syndEntry.getPublishedDate());

        // 调用http服务获取description
        String description = callHttpService("http://127.0.0.1:8083/generateText?url=" + syndEntry.getLink());
        System.out.println("title: "+syndEntry.getTitle()+"\n"+"url: "+syndEntry.getLink()+"\n"+"description"+description+"\n");
        SyndContent syndContent = new SyndContentImpl();
        syndContent.setValue(description);
        newEntry.setDescription(syndContent);

        return newEntry;
    }

    public SyndEntry handleVideo(SyndEntry syndEntry) throws URISyntaxException {
        // 创建一个新的SyndEntry对象，复制原来的title、link和pubDate
        SyndEntry newEntry = new SyndEntryImpl();
        newEntry.setTitle(syndEntry.getTitle());
        newEntry.setLink(syndEntry.getLink());
        newEntry.setPublishedDate(syndEntry.getPublishedDate());

        // 调用http服务获取description
        String description = callHttpService("http://127.0.0.1:9502/processVideo?url=" + syndEntry.getLink());
        System.out.println("title: "+syndEntry.getTitle()+"\n"+"url: "+syndEntry.getLink()+"\n"+"description"+description+"\n");
        SyndContent syndContent = new SyndContentImpl();
        syndContent.setValue(description);
        newEntry.setDescription(syndContent);

        return newEntry;
    }

    private String callHttpService(String url) throws URISyntaxException {
        // 使用RestTemplate调用http服务
        ResponseEntity<String> responseEntity = restTemplate.exchange(new URI(url), HttpMethod.GET, null, String.class);
        return responseEntity.getBody();
    }
}