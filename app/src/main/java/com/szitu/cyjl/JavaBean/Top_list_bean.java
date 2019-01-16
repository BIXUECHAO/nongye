package com.szitu.cyjl.JavaBean;

import cn.bmob.v3.BmobObject;

public class Top_list_bean extends BmobObject {
    private String url;
    private String title;
    private String content;
    private String date;


    public Top_list_bean(){
        this.setTableName("new");

    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
