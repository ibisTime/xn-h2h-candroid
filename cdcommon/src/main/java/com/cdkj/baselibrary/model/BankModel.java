package com.cdkj.baselibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LeiQ on 2016/12/28.
 */


public class BankModel implements Parcelable {


    /**
     * id : 4
     * bankCode : ADC
     * bankName : 中国农业银行
     * channelType : 11
     * payType : PC
     * status : 1
     * paybank : 080009
     * orderAmount : 10000
     * dayAmount : 20000
     * monthAmount : 100000
     * remark : remark
     */

    private int id;
    private String bankCode;
    private String bankName;
    private String channelType;
    private String payType;
    private String status;
    private String paybank;
    private int orderAmount;
    private int dayAmount;
    private int monthAmount;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaybank() {
        return paybank;
    }

    public void setPaybank(String paybank) {
        this.paybank = paybank;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(int dayAmount) {
        this.dayAmount = dayAmount;
    }

    public int getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(int monthAmount) {
        this.monthAmount = monthAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.channelType);
        dest.writeString(this.payType);
        dest.writeString(this.status);
        dest.writeString(this.paybank);
        dest.writeInt(this.orderAmount);
        dest.writeInt(this.dayAmount);
        dest.writeInt(this.monthAmount);
        dest.writeString(this.remark);
    }

    public BankModel() {
    }

    protected BankModel(Parcel in) {
        this.id = in.readInt();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.channelType = in.readString();
        this.payType = in.readString();
        this.status = in.readString();
        this.paybank = in.readString();
        this.orderAmount = in.readInt();
        this.dayAmount = in.readInt();
        this.monthAmount = in.readInt();
        this.remark = in.readString();
    }

    public static final Parcelable.Creator<BankModel> CREATOR = new Parcelable.Creator<BankModel>() {
        @Override
        public BankModel createFromParcel(Parcel source) {
            return new BankModel(source);
        }

        @Override
        public BankModel[] newArray(int size) {
            return new BankModel[size];
        }
    };
}
