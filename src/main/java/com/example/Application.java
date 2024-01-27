package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // 设置HTTP代理
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "10809");

        // 设置HTTPS代理（如果需要的话）
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10809");

        SpringApplication.run(Application.class, args);
    }
}
