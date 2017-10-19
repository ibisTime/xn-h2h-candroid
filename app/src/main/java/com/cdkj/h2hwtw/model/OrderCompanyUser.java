package com.cdkj.h2hwtw.model;

/**
 * Created by 李先俊 on 2017/10/19.
 */

public class OrderCompanyUser {


    /**
     * userId : U1111111111111111
     * kind : C
     * level : 0
     * loginName : 18868824532
     * nickname : 昵称昵称2
     * photo : 69c0bbbe-66ec-4f3a-a350-9d1473f3711d.JPG
     * mobile : 18868824532
     * identityFlag : 0
     * loginLogDatetime : Oct 18, 2017 3:31:05 PM
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
