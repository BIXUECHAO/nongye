package com.szitu.cyjl.JavaBean;

public class Msg {
    public static final int TYPE_RECRIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }
}
