package com.example.mapper;

import com.example.model.RssSource;
import com.example.model.RssSourceWithoutXmlDataDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * RssSource 的 Mapper 接口
 */
@Mapper
public interface RssSourceMapper {
    /**
     * 插入 RSS 源
     * @param rssSource RSS 源
     * @return rss_id
     */
    int insertRssSource(RssSource rssSource);

    /**
     * 删除 RSS 源
     * @param rssId RSS 源的 ID
     * @return 删除的记录数
     */
    int deleteRssSource(int rssId);

    /**
     * 查询所有的 RSS 源（不包括 XML 数据）
     * @return 所有的 RSS 源
     */
    List<RssSourceWithoutXmlDataDTO> selectAllRssSourcesWithoutXmlData();

    /**
     * 查询 RSS 源的 XML 数据
     * @param rssId RSS 源的 ID
     * @return XML 数据
     */
    String selectRssSourcesXmlData(int rssId);

    /**
     * 查询所有的 RSS 源
     * @return 所有的 RSS 源
     */
    List<RssSource> selectAllRssSources();


    /**
     * 更新 RSS 源
     * @param rssSource RSS 源
     * @return 更新的记录数
     */
    int update(RssSource rssSource);
}