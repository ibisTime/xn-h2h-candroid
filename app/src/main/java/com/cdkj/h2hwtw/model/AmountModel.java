package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by 李先俊 on 2017/6/16.
 */

public class AmountModel implements Parcelable {


    /**
     * accountNumber : A2016122410334393629
     * userId : U2016122116231392740
     * realName : 谢谢
     * type : C
     * status : 0
     * currency : CNY
     * amount : 0
     * frozenAmount : 0
     * md5 : f0ed31502f5d1f206753a5e8114c87e0
     * createDatetime : Dec 24, 2016 10:33:43 AM
     * systemCode : CD-CZH000001
     */

    private String accountNumber;
    private String userId;
    private String realName;
    private String type;
    private String status;
    private String currency;
    private BigDecimal amount;
    private BigDecimal frozenAmount;
    private String md5;
    private String createDatetime;
    private String systemCode;

    private BigDecimal addAmount;

    public BigDecimal getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(BigDecimal addAmount) {
        this.addAmount = addAmount;
    }

    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    public BigDecimal getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
    }

    private BigDecimal inAmount;
    private BigDecimal outAmount;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
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
        dest.writeString(this.accountNumber);
        dest.writeString(this.userId);
        dest.writeString(this.realName);
        dest.writeString(this.type);
        dest.writeString(this.status);
        dest.writeString(this.currency);
        dest.writeSerializable(this.amount);
        dest.writeSerializable(this.frozenAmount);
        dest.writeString(this.md5);
        dest.writeString(this.createDatetime);
        dest.writeString(this.systemCode);
        dest.writeSerializable(this.addAmount);
        dest.writeSerializable(this.inAmount);
        dest.writeSerializable(this.outAmount);
    }

    public AmountModel() {
    }

    protected AmountModel(Parcel in) {
        this.accountNumber = in.readString();
        this.userId = in.readString();
        this.realName = in.readString();
        this.type = in.readString();
        this.status = in.readString();
        this.currency = in.readString();
        this.amount = (BigDecimal) in.readSerializable();
        this.frozenAmount = (BigDecimal) in.readSerializable();
        this.md5 = in.readString();
        this.createDatetime = in.readString();
        this.systemCode = in.readString();
        this.addAmount = (BigDecimal) in.readSerializable();
        this.inAmount = (BigDecimal) in.readSerializable();
        this.outAmount = (BigDecimal) in.readSerializable();
    }

    public static final Parcelable.Creator<AmountModel> CREATOR = new Parcelable.Creator<AmountModel>() {
        @Override
        public AmountModel createFromParcel(Parcel source) {
            return new AmountModel(source);
        }

        @Override
        public AmountModel[] newArray(int size) {
            return new AmountModel[size];
        }
    };
}
