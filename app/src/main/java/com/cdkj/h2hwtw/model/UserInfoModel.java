package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public class UserInfoModel implements Parcelable {


    /**
     * address : 梦想小镇天使村
     * appOpenId : 544345353
     * area : 余杭区
     * birthday : 1989-08-02
     * city : 杭州市
     * companyCode : CD-CZH000001
     * createDatetime : Aug 3, 2017 5:33:54 PM
     * diploma : 0
     * divRate : 0.1
     * email : leo.zheng@hichengdai.com
     * gender : 1
     * h5OpenId : 544345353
     * idKind : 1
     * idNo : 330281198908023312
     * introduce : 自我介绍
     * kind : C
     * latitude : 120
     * level : 1
     * loginName : 15268501481
     * loginPwdStrength : 1
     * longitude : 120
     * mobile : 15268501481
     * nickname : 41317064
     * occupation : 1
     * pdf : 324.pdf
     * photo : photo.jpg
     * province : 浙江省
     * realName : 真实姓名
     * remark : 新注册用户
     * roleCode : SR3422
     * status : 0
     * systemCode : CD-CZH000001
     * tradePwdStrength : 1
     * unionId : 544345353
     * updateDatetime : Aug 3, 2017 5:33:54 PM
     * updater : admin
     * userId : U201708031733541317064
     * userReferee : U2017080317335413170
     * workTime : 3
     * "totalFollowNum":0.0,"totalFansNum":0.0
     */

    private String address;
    private String appOpenId;
    private String area;
    private String birthday;
    private String city;
    private String companyCode;
    private String createDatetime;
    private String diploma;
    private String divRate;
    private String email;
    private String gender;
    private String h5OpenId;
    private String idKind;
    private String idNo;
    private String introduce;
    private String kind;
    private String latitude;
    private String level;
    private String loginName;
    private String loginPwdStrength;
    private String longitude;
    private String mobile;
    private String nickname;
    private String occupation;
    private String pdf;
    private String photo;
    private String province;
    private String realName;
    private String remark;
    private String roleCode;
    private String status;
    private String systemCode;
    private String tradePwdStrength;
    private String unionId;
    private String updateDatetime;
    private String updater;
    private String userId;
    private String userReferee;
    private String workTime;
    private boolean tradepwdFlag;
    private long totalFollowNum;

    public long getTotalFollowNum() {
        return totalFollowNum;
    }

    public void setTotalFollowNum(long totalFollowNum) {
        this.totalFollowNum = totalFollowNum;
    }

    public long getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(long totalFansNum) {
        this.totalFansNum = totalFansNum;
    }

    private long totalFansNum;

    public boolean isTradepwdFlag() {
        return tradepwdFlag;
    }

    public void setTradepwdFlag(boolean tradepwdFlag) {
        this.tradepwdFlag = tradepwdFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppOpenId() {
        return appOpenId;
    }

    public void setAppOpenId(String appOpenId) {
        this.appOpenId = appOpenId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public String getDivRate() {
        return divRate;
    }

    public void setDivRate(String divRate) {
        this.divRate = divRate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getH5OpenId() {
        return h5OpenId;
    }

    public void setH5OpenId(String h5OpenId) {
        this.h5OpenId = h5OpenId;
    }

    public String getIdKind() {
        return idKind;
    }

    public void setIdKind(String idKind) {
        this.idKind = idKind;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getLoginPwdStrength() {
        return loginPwdStrength;
    }

    public void setLoginPwdStrength(String loginPwdStrength) {
        this.loginPwdStrength = loginPwdStrength;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getTradePwdStrength() {
        return tradePwdStrength;
    }

    public void setTradePwdStrength(String tradePwdStrength) {
        this.tradePwdStrength = tradePwdStrength;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserReferee() {
        return userReferee;
    }

    public void setUserReferee(String userReferee) {
        this.userReferee = userReferee;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.appOpenId);
        dest.writeString(this.area);
        dest.writeString(this.birthday);
        dest.writeString(this.city);
        dest.writeString(this.companyCode);
        dest.writeString(this.createDatetime);
        dest.writeString(this.diploma);
        dest.writeString(this.divRate);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.h5OpenId);
        dest.writeString(this.idKind);
        dest.writeString(this.idNo);
        dest.writeString(this.introduce);
        dest.writeString(this.kind);
        dest.writeString(this.latitude);
        dest.writeString(this.level);
        dest.writeString(this.loginName);
        dest.writeString(this.loginPwdStrength);
        dest.writeString(this.longitude);
        dest.writeString(this.mobile);
        dest.writeString(this.nickname);
        dest.writeString(this.occupation);
        dest.writeString(this.pdf);
        dest.writeString(this.photo);
        dest.writeString(this.province);
        dest.writeString(this.realName);
        dest.writeString(this.remark);
        dest.writeString(this.roleCode);
        dest.writeString(this.status);
        dest.writeString(this.systemCode);
        dest.writeString(this.tradePwdStrength);
        dest.writeString(this.unionId);
        dest.writeString(this.updateDatetime);
        dest.writeString(this.updater);
        dest.writeString(this.userId);
        dest.writeString(this.userReferee);
        dest.writeString(this.workTime);
    }

    public UserInfoModel() {
    }

    protected UserInfoModel(Parcel in) {
        this.address = in.readString();
        this.appOpenId = in.readString();
        this.area = in.readString();
        this.birthday = in.readString();
        this.city = in.readString();
        this.companyCode = in.readString();
        this.createDatetime = in.readString();
        this.diploma = in.readString();
        this.divRate = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.h5OpenId = in.readString();
        this.idKind = in.readString();
        this.idNo = in.readString();
        this.introduce = in.readString();
        this.kind = in.readString();
        this.latitude = in.readString();
        this.level = in.readString();
        this.loginName = in.readString();
        this.loginPwdStrength = in.readString();
        this.longitude = in.readString();
        this.mobile = in.readString();
        this.nickname = in.readString();
        this.occupation = in.readString();
        this.pdf = in.readString();
        this.photo = in.readString();
        this.province = in.readString();
        this.realName = in.readString();
        this.remark = in.readString();
        this.roleCode = in.readString();
        this.status = in.readString();
        this.systemCode = in.readString();
        this.tradePwdStrength = in.readString();
        this.unionId = in.readString();
        this.updateDatetime = in.readString();
        this.updater = in.readString();
        this.userId = in.readString();
        this.userReferee = in.readString();
        this.workTime = in.readString();
    }

    public static final Parcelable.Creator<UserInfoModel> CREATOR = new Parcelable.Creator<UserInfoModel>() {
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
