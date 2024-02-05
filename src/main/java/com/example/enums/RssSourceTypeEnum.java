package com.example.enums;

/**
 * RSS源类型枚举
 * 该枚举定义了两种类型的RSS源: ARTICLE和VIDEO
 */
public enum RssSourceTypeEnum {
    /**
     * 文章类型的RSS源
     */
    ARTICLE(1),
    /**
     * 视频类型的RSS源
     */
    VIDEO(2);

    private final int value;

    RssSourceTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}