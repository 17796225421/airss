package com.example.model;

import lombok.Data;

/**
 * 该类定义了RSS源的属性，对应数据库中的一张表
 */
@Data
public class RssSource {
    /**
     * RSS源ID，作为主键
     */
    private int rssId;
    /**
     * RSS源的名称
     */
    private String rssName;
    /**
     * RSS源的地址
     */
    private String rssUrl;
    /**
     * RSS源的类型，分为文章和视频两种
     */
    private int rssType;
    /**
     * RSS源的XML数据
     */
    private String rssXmlData;
}