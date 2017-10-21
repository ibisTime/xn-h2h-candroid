package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cdkj on 2017/10/21.
 */

public class ActivityCenterModel implements Parcelable {


    /**
     * code : PU2017102900110101311
     * type : 1
     * advPic : banner@2x_1508490423901.png
     * description : <p>12</p>
     * startDatetime : Oct 17, 2017 12:00:00 AM
     * endDatetime : Oct 17, 2017 1:09:59 PM
     * value1 : 0.8
     * value2 : 0
     * value3 : 0
     * location : 1
     * orderNo : 1
     * status : 1
     * updater : admin
     * updateDatetime : Oct 20, 2017 5:07:25 PM
     * remark : 下架
     * companyCode : CD-WTW000016
     * systemCode : CD-WTW000016
     */

    private String code;
    private String type;
    private String advPic;
    private String description;
    private String startDatetime;
    private String endDatetime;
    private double value1;
    private int value2;
    private int value3;
    private String location;
    private int orderNo;
    private String status;
    private String updater;
    private String updateDatetime;
    private String remark;
    private String companyCode;
    private String systemCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.type);
        dest.writeString(this.advPic);
        dest.writeString(this.description);
        dest.writeString(this.startDatetime);
        dest.writeString(this.endDatetime);
        dest.writeDouble(this.value1);
        dest.writeInt(this.value2);
        dest.writeInt(this.value3);
        dest.writeString(this.location);
        dest.writeInt(this.orderNo);
        dest.writeString(this.status);
        dest.writeString(this.updater);
        dest.writeString(this.updateDatetime);
        dest.writeString(this.remark);
        dest.writeString(this.companyCode);
        dest.writeString(this.systemCode);
    }

    public ActivityCenterModel() {
    }

    protected ActivityCenterModel(Parcel in) {
        this.code = in.readString();
        this.type = in.readString();
        this.advPic = in.readString();
        this.description = in.readString();
        this.startDatetime = in.readString();
        this.endDatetime = in.readString();
        this.value1 = in.readDouble();
        this.value2 = in.readInt();
        this.value3 = in.readInt();
        this.location = in.readString();
        this.orderNo = in.readInt();
        this.status = in.readString();
        this.updater = in.readString();
        this.updateDatetime = in.readString();
        this.remark = in.readString();
        this.companyCode = in.readString();
        this.systemCode = in.readString();
    }

    public static final Parcelable.Creator<ActivityCenterModel> CREATOR = new Parcelable.Creator<ActivityCenterModel>() {
        @Override
        public ActivityCenterModel createFromParcel(Parcel source) {
            return new ActivityCenterModel(source);
        }

        @Override
        public ActivityCenterModel[] newArray(int size) {
            return new ActivityCenterModel[size];
        }
    };
}
