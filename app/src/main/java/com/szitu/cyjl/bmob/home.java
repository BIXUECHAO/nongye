package com.szitu.cyjl.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class home extends BmobObject {
    private BmobFile pjoto;
    private String name;

    public BmobFile getPjoto() {
        return pjoto;
    }

    public String getName() {
        return name;
    }

    public void setPjoto(BmobFile pjoto) {
        this.pjoto = pjoto;
    }

    public void setName(String name) {
        this.name = name;
    }
}
