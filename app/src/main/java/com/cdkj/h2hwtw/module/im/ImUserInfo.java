package com.cdkj.h2hwtw.module.im;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李先俊 on 2017/10/30.
 */

public class ImUserInfo implements Parcelable {

    private String leftImg;  //聊天对象头像
    private String rightImg;//我的头像

    private String toUserId;//对象id

    private String userName;//对象姓名

    public String getLeftImg() {
        return leftImg;
    }

    public void setLeftImg(String leftImg) {
        this.leftImg = leftImg;
    }

    public String getRightImg() {
        return rightImg;
    }

    public void setRightImg(String rightImg) {
        this.rightImg = rightImg;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leftImg);
        dest.writeString(this.rightImg);
        dest.writeString(this.toUserId);
        dest.writeString(this.userName);
    }

    public ImUserInfo() {
    }

    protected ImUserInfo(Parcel in) {
        this.leftImg = in.readString();
        this.rightImg = in.readString();
        this.toUserId = in.readString();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<ImUserInfo> CREATOR = new Parcelable.Creator<ImUserInfo>() {
        @Override
        public ImUserInfo createFromParcel(Parcel source) {
            return new ImUserInfo(source);
        }

        @Override
        public ImUserInfo[] newArray(int size) {
            return new ImUserInfo[size];
        }
    };
}
