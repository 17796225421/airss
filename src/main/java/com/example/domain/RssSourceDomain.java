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
        log.debug("开始更新RSS源: {}", rssSource.getRssName());   // 添加debug日志，打印当前正在更新的RSS源名称
        try {
            URL url = new URL(rssSource.getRssUrl());
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(url);
            log.debug("成功从URL获取文档: {}", url);  // 添加debug日志，打印成功获取文档的消息

            // 获取新xml中的所有item
            Element rss = document.getRootElement();
            Element channel = rss.getChild("channel");
            List<Element> items = channel.getChildren("item");
            log.debug("获取到 {} 个项目", items.size()); // 添加debug日志，打印获取到的项目数量

            String oldXmlData = rssSource.getRssXmlData();
            Document oldDocument = builder.build(new StringReader(oldXmlData));
            Element oldRss = oldDocument.getRootElement();
            Element oldChannel = oldRss.getChild("channel");

            // 判断新xml中的item是否在旧xml中存在，如果不存在则处理新item并插入到旧xml中
            for (Element item : items) {
                String link = item.getChildText("link");
                if (!oldXmlData.contains(link)) {
                    String description = ""; // 初始化描述字符串
                    // 如果rssSource的type是文章，就调用aiDomain的processArticle(link)，否则处理视频
                    if (rssSource.getRssType() == RssSourceTypeEnum.ARTICLE.getValue()) {
                        description = aiDomain.processArticle(link); // 使用链接作为参数
                    } else {
                        description = aiDomain.processVideo(link); // 使用链接作为参数
                    }
                    log.debug("处理完成，链接地址：{}，描述：{}", link, description); // 添加debug日志，打印成功处理完成的消息

                    // 将获得的描述设置为item的description
                    Element descriptionElement = new Element("description");
                    descriptionElement.setText(description);
                    item.addContent(descriptionElement);
                    Element newItem = copyElement(item); // 创建一个新的item，复制原item的内容
                    oldChannel.addContent(newItem); // 将新item添加到oldChannel
                }
            }

            // 创建一个新的Format实例，设置缩进为4个空格，添加换行，然后创建一个新的XMLOutputter
            Format format = Format.getPrettyFormat();
            format.setIndent("    ");
            XMLOutputter outputter = new XMLOutputter(format);

            // 更新rssSource的xml数据并保存到数据库
            rssSource.setRssXmlData(outputter.outputString(oldDocument));
            rssSourceMapper.update(rssSource);
            log.debug("更新数据库成功，RSS源: {}", rssSource.getRssName()); // 添加debug日志，打印成功更新数据库的消息

        } catch (Exception e) {
            log.error("更新RSS源出错，RSS源: {}", rssSource.getRssName(), e); // 添加错误日志，打印错误消息和异常堆栈信息
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