package com.szitu.cyjl.bmob;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Subscribe extends BmobObject {
    private String type;
    private String local;
    private String min;
    private String max;
    private String introduce;
    private List<String> filebyte;
    private String username;
    private String userOblectID;
    private String localtime;
    private String phone;
    private String UerIcon;
    private String sp_name;
    private String sp_number;

    public String getSp_name() {
        return sp_name;
    }

    public String getSp_number() {
        return sp_number;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public void setSp_number(String sp_number) {
        this.sp_number = sp_number;
    }

    public String getUerIcon() {
        return UerIcon;
    }

    public void setUerIcon(String uerIcon) {
        UerIcon = uerIcon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserOblectID(String userOblectID) {
        this.userOblectID = userOblectID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserOblectID() {
        return userOblectID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getType() {
        return type;
    }

    public String getLocal() {
        return local;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getIntroduce() {
        return introduce;
    }

    public List<String> getFilebyte() {
        return filebyte;
    }

    public void setFilebyte(List<String> filebyte) {
        this.filebyte = filebyte;
    }
}
