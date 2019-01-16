package com.szitu.cyjl.bmob;

import cn.bmob.v3.BmobObject;

public class Market extends BmobObject {
    private String name;
    private String price;
    private String date;
    private String adress;
    private String twoname;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setTwoname(String twoname) {
        this.twoname = twoname;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }



    public String getTwoname() {
        return twoname;
    }
}
