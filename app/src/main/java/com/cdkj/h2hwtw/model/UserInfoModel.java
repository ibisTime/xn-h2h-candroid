package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public class UserInfoModel implements Parcelable {

    /**
     * userId : U201708081636580423983
     * loginName : 13765051712
     * mobile : 13765051712
     * nickname : 80423983
     * loginPwdStrength : 1
     * kind : C
     * level : 1
     * status : 0
     * createDatetime : Aug 8, 2017 4:36:58 PM
     * companyCode : CD-YLQ000014
     * systemCode : CD-YLQ000014
     * tradepwdFlag : false
     * totalFollowNum : 0
     * totalFansNum : 0
     */

    private String userId;
    private String loginName;


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    private String realName;
    private String mobile;
    private String nickname;
    private String loginPwdStrength;
    private String kind;
    private String level;
    private String status;
    private String createDatetime;
    private String companyCode;
    private String systemCode;
    private String photo;
    private String blacklistFlag;
    private String bankcardFlag;

    public String getBlacklistFlag() {
        return blacklistFlag;
    }

    public void setBlacklistFlag(String blacklistFlag) {
        this.blacklistFlag = blacklistFlag;
    }

    public String getBankcardFlag() {
        return bankcardFlag;
    }

    public void setBankcardFlag(String bankcardFlag) {
        this.bankcardFlag = bankcardFlag;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private boolean tradepwdFlag;
    private int totalFollowNum;
    private int totalFansNum;

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

    public String getLoginPwdStrength() {
        return loginPwdStrength;
    }

    public void setLoginPwdStrength(String loginPwdStrength) {
        this.loginPwdStrength = loginPwdStrength;
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

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public boolean isTradepwdFlag() {
        return tradepwdFlag;
    }

    public void setTradepwdFlag(boolean tradepwdFlag) {
        this.tradepwdFlag = tradepwdFlag;
    }

    public int getTotalFollowNum() {
        return totalFollowNum;
    }

    public void setTotalFollowNum(int totalFollowNum) {
        this.totalFollowNum = totalFollowNum;
    }

    public int getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(int totalFansNum) {
        this.totalFansNum = totalFansNum;
    }

    public UserInfoModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.loginName);
        dest.writeString(this.realName);
        dest.writeString(this.mobile);
        dest.writeString(this.nickname);
        dest.writeString(this.loginPwdStrength);
        dest.writeString(this.kind);
        dest.writeString(this.level);
        dest.writeString(this.status);
        dest.writeString(this.createDatetime);
        dest.writeString(this.companyCode);
        dest.writeString(this.systemCode);
        dest.writeString(this.photo);
        dest.writeString(this.blacklistFlag);
        dest.writeString(this.bankcardFlag);
        dest.writeByte(this.tradepwdFlag ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalFollowNum);
        dest.writeInt(this.totalFansNum);
    }

    protected UserInfoModel(Parcel in) {
        this.userId = in.readString();
        this.loginName = in.readString();
        this.realName = in.readString();
        this.mobile = in.readString();
        this.nickname = in.readString();
        this.loginPwdStrength = in.readString();
        this.kind = in.readString();
        this.level = in.readString();
        this.status = in.readString();
        this.createDatetime = in.readString();
        this.companyCode = in.readString();
        this.systemCode = in.readString();
        this.photo = in.readString();
        this.blacklistFlag = in.readString();
        this.bankcardFlag = in.readString();
        this.tradepwdFlag = in.readByte() != 0;
        this.totalFollowNum = in.readInt();
        this.totalFansNum = in.readInt();
    }

    public static final Creator<UserInfoModel> CREATOR = new Creator<UserInfoModel>() {
        @Override
        public UserInfoModel createFromParcel(Parcel source) {
            return new UserInfoModel(source);
        }

        @Override
        public UserInfoModel[] newArray(int size) {
            return new UserInfoModel[size];
        }
    };
}
