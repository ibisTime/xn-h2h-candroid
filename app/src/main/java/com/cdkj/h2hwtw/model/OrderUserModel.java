package com.cdkj.h2hwtw.model;

/**
 * Created by 李先俊 on 2017/10/19.
 */

public class OrderUserModel {


    /**
     * userId : U201710131355098586852
     * kind : C
     * level : 1
     * loginName : 13765051712
     * nickname : 1111
     * photo : ANDROID_1508251802514_610_816.jpg
     * mobile : 13765051712
     * identityFlag : 0
     * loginLogDatetime : Oct 18, 2017 8:53:04 PM
     */

    private String userId;
    private String kind;
    private String level;
    private String loginName;
    private String nickname;
    private String photo;
    private String mobile;
    private String identityFlag;
    private String loginLogDatetime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentityFlag() {
        return identityFlag;
    }

    public void setIdentityFlag(String identityFlag) {
        this.identityFlag = identityFlag;
    }

    public String getLoginLogDatetime() {
        return loginLogDatetime;
    }

    public void setLoginLogDatetime(String loginLogDatetime) {
        this.loginLogDatetime = loginLogDatetime;
    }
}
