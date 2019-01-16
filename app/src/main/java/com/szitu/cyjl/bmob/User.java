package com.szitu.cyjl.bmob;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String nickname;
    private String userlcon;

    public String getUserlcon() {
        return userlcon;
    }

    public void setUserlcon(String userlcon) {
        this.userlcon = userlcon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 序列化SharedPreference
     *
     * @return
     */
    public static User getUserInfo() {
        return BmobUser.getCurrentUser(User.class);
    }
}