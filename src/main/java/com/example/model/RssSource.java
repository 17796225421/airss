package com.example.model;

public class RssSource {
    private String url; // RSS源的url
    private String name; // RSS源的名称
    private Type type; // RSS源的类型，可以是图文或者视频

    public RssSource(String url, String name, Type type) {
        this.url = url;
        this.name = name;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ARTICLE, // 图文
        VIDEO // 视频
    }
}