package com.szitu.cyjl.JavaBean;

public class Message {
    private String icon;
    private String name;
    private String message;

    public Message(String icon,String name,String message){
        this.icon = icon;
        this.name = name;
        this.message = message;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
