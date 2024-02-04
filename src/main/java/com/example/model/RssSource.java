package com.example.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class RssSource {
    @Id
    private int id; // RSS源的唯一id
    private String url; // RSS源的url

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getXmlText() {
        return xmlText;
    }

    public void setXmlText(String xmlText) {
        this.xmlText = xmlText;
    }

    private String name; // RSS源的名称
    @Enumerated(EnumType.STRING) // 使用EnumType.STRING模式
    private Type type; // RSS源的类型，可以是图文或者视频
    private String xmlText; // RSS源的xml文本


    public enum Type {
        ARTICLE, // 图文
        VIDEO // 视频
    }

}
