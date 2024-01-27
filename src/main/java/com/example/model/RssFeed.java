package com.example.model;

import java.io.File;

public class RssFeed {
    private String url;
    private String rssName;
    private Thread listenerThread;
    private File rssFile;

    public RssFeed(String url,String rssName) {
        this.url = url;
        this.rssName=rssName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Thread getListenerThread() {
        return listenerThread;
    }

    public void setListenerThread(Thread listenerThread) {
        this.listenerThread = listenerThread;
    }

    public File getRssFile() {
        return rssFile;
    }

    public void setRssFile(File rssFile) {
        this.rssFile = rssFile;
    }

    public String getRssName() {
        return rssName;
    }

    public void setRssName(String rssName) {
        this.rssName = rssName;
    }
}
