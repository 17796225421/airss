package com.example.model;

public class RssSource {
    private int id; // RSS源的唯一id
    private String url; // RSS源的url
    private String name; // RSS源的名称
    private Type type; // RSS源的类型，可以是图文或者视频

    public RssSource(String url, String name, Type type) {
        this.id = Math.abs(url.hashCode()) % 100000; // 使用URL的哈希值的绝对值对100000取模作为id
        this.url = url;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
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