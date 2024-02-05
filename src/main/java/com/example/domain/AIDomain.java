package com.example.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AIDomain {
    public String processArticle(String link) {
        RestTemplate restTemplate = new RestTemplate();
        String urlText = "http://127.0.0.1:8083/generateText?url=" + link;
        ResponseEntity<String> response = restTemplate.getForEntity(urlText, String.class);
        return response.getBody();
    }

    public String processVideo(String link) {
        RestTemplate restTemplate = new RestTemplate();
        String urlText = "http://127.0.0.1:9502/processVideo?url=" + link;
        ResponseEntity<String> response = restTemplate.getForEntity(urlText, String.class);
        return response.getBody();
    }
}