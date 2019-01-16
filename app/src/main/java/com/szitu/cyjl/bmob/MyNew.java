package com.szitu.cyjl.bmob;

import cn.bmob.v3.BmobObject;

public class MyNew extends BmobObject {
    private String come;
    private String content;
    private String date;
    private String name;
    private String icon;
    private String url;
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setCome(String come) {
        this.come = come;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCome() {
        return come;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }
}
