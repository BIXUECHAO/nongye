package com.szitu.cyjl.data;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username; // 用户名

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    private String feature;


    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    private String faceImage; // 用户的脸部图片

    private String IMEI; // 设备的唯一标识符

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }
}