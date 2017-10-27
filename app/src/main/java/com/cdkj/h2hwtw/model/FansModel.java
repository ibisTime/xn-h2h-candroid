package com.cdkj.h2hwtw.model;

/**
 * Created by 李先俊 on 2017/10/27.
 */

public class FansModel {
    /**
     * userId : U201710271332408276230
     * loginName : 13765051712
     * mobile : 13765051712
     * nickname : 08276230
     * kind : C
     * level : 1
     * status : 0
     * tradepwdFlag : false
     */

    private String userId;
    private String loginName;
    private String mobile;
    private String nickname;
    private String kind;
    private String level;
    private String status;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String photo;
    private boolean tradepwdFlag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTradepwdFlag() {
        return tradepwdFlag;
    }

    public void setTradepwdFlag(boolean tradepwdFlag) {
        this.tradepwdFlag = tradepwdFlag;
    }
}
