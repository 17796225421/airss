package com.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GptProcessor {
    private OkHttpClient client;
    private String apiKey="sk-GwRaUfEtHAhobdxA1299858776274f47B32030Ac7cCdE0B5";
    private String gptVersion="gpt-3.5-turbo-1106";
    private int maxTokens=100;
    private String website="https://gpts.onechat.fun";
    private Gson gson;

    public GptProcessor() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String processArticle(String article) {
        // 构造请求URL
        String url = website + "/v1/chat/completions";

        // 创建 message 对象
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", article + "\\n\\n请总结以上内容。");

        // 创建包含 message 对象的 JSON 数组
        JsonArray messagesArray = new JsonArray();
        messagesArray.add(message);

        // 构建整个 JSON 请求对象
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("model", gptVersion);
        jsonRequest.add("messages", messagesArray);
        jsonRequest.addProperty("stream", false);
        jsonRequest.addProperty("max_tokens", maxTokens);

        // 转换为 JSON 字符串
        String json = gson.toJson(jsonRequest);

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // 解析响应
            String responseBody = response.body().string();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
            JsonArray choices = responseJson.getAsJsonArray("choices");
            if (choices.size() > 0) {
                JsonObject firstChoice = choices.get(0).getAsJsonObject();
                if (firstChoice.has("message") && firstChoice.getAsJsonObject("message").has("content")) {
                    String content = firstChoice.getAsJsonObject("message").get("content").getAsString();
                    // 创建一个新的<p>元素来包含摘要
                    String summaryElement = "<p>" + content + "</p>";

                    // 解析HTML内容并将摘要插入到<body>元素的开头
                    Document doc = Jsoup.parse(article);
                    doc.body().prepend(summaryElement);

                    // 获取修改后的HTML内容
                    article = doc.html();
                }
            }

            // 如果没有生成摘要，将"No summary available."插入到<body>元素的开头
            if (article == null) {
                Document doc = Jsoup.parse(article);
                doc.body().prepend("<p>No summary available.</p>");
                article = doc.html();
            }

            return article;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process article", e);
        }

    }

}
