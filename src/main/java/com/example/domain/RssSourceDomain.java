package com.example.domain;

import com.example.enums.RssSourceTypeEnum;
import com.example.mapper.RssSourceMapper;
import com.example.model.RssSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jdom2.output.Format;

import java.io.StringReader;
import java.net.URL;
import java.util.List;

/**
 * RssSource领域类，负责初始化RssSource的Xml数据和更新RssSource
 */
@Component
@Slf4j
public class RssSourceDomain {

    @Autowired
    private RssSourceMapper rssSourceMapper;
    @Autowired
    private AIDomain aiDomain; // Assuming there is an AI class for processing items

    /**
     * 初始化RssSource的Xml数据
     * @param rssSource RSS源
     */
    public void initRssXmlData(RssSource rssSource) {
        StringBuilder rssXmlBuilder = new StringBuilder();

        // RSS的开头标签
        rssXmlBuilder.append("<rss version=\"2.0\">\n");
        rssXmlBuilder.append("<channel>\n");

        // RSS的标题
        rssXmlBuilder.append("<title>");
        rssXmlBuilder.append(rssSource.getRssName());
        rssXmlBuilder.append("</title>\n");

        // RSS的链接
        rssXmlBuilder.append("<link>");
        rssXmlBuilder.append(rssSource.getRssUrl());
        rssXmlBuilder.append("</link>\n");

        // RSS的关闭标签
        rssXmlBuilder.append("</channel>\n");
        rssXmlBuilder.append("</rss>");

        rssSource.setRssXmlData(rssXmlBuilder.toString());
    }

    /**
     * 更新RssSource
     * @param rssSource RSS源
     */
    public void updateRssSource(RssSource rssSource) {
        log.debug("开始更新RSS源: {}", rssSource.getRssName());
        try {
            URL url = new URL(rssSource.getRssUrl());
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(url);
            log.debug("成功从URL获取文档: {}", url);

            Element rss = document.getRootElement();
            Element channel = rss.getChild("channel");
            List<Element> items = channel.getChildren("item");
            log.debug("获取到 {} 个项目", items.size());

            String oldXmlData = rssSource.getRssXmlData();
            Document oldDocument = builder.build(new StringReader(oldXmlData));
            Element oldRss = oldDocument.getRootElement();
            Element oldChannel = oldRss.getChild("channel");

            for (Element item : items) {
                String link = item.getChildText("link");
                if (!oldXmlData.contains(link)) {
                    String description = "";
                    if (rssSource.getRssType() == RssSourceTypeEnum.ARTICLE.getValue()) {
                        description = aiDomain.processArticle(link);
                    } else {
                        description = aiDomain.processVideo(link);
                    }
                    log.debug("处理完成，链接地址：{}，描述：{}", link, description);

                    Element descriptionElement = item.getChild("description");
                    if (descriptionElement != null) {
                        item.removeContent(descriptionElement); // 删除旧的description
                    }
                    descriptionElement = new Element("description"); // 创建新的description元素
                    descriptionElement.setText(description); // 设置新的description
                    item.addContent(descriptionElement); // 添加新的description到item

                    Element newItem = copyElement(item);
                    oldChannel.addContent(newItem);
                }
            }

            Format format = Format.getPrettyFormat();
            format.setIndent("    ");
            XMLOutputter outputter = new XMLOutputter(format);

            rssSource.setRssXmlData(outputter.outputString(oldDocument));
            rssSourceMapper.update(rssSource);
            log.debug("更新数据库成功，RSS源: {}", rssSource.getRssName());

        } catch (Exception e) {
            log.error("更新RSS源出错，RSS源: {}", rssSource.getRssName(), e);
            e.printStackTrace();
        }
    }

    private Element copyElement(Element element) {
        Element newElement = new Element(element.getName()); // 创建一个新的Element，名称与原Element一样
        newElement.setText(element.getText().trim()); // 复制原Element的文本内容并去除首尾空白字符
        // 复制原Element的所有属性
        for (Attribute attribute : element.getAttributes()) {
            newElement.setAttribute(attribute.clone());
        }
        // 递归复制所有子Element并且添加换行符
        for (Element child : element.getChildren()) {
            newElement.addContent(copyElement(child));
            newElement.addContent("\n"); // 添加换行
        }
        return newElement;
    }

}