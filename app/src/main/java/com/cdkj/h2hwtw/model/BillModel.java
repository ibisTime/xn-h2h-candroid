package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by LeiQ on 2016/12/30.
 */

public class BillModel implements Parcelable {

    /**
     * code : AJ201707181935421546289284138
     * refNo : EC20170718193542151876827435
     * accountNumber : A201707181621322293533626016
     * transAmount : 500000
     * userId : U20170718162132217422
     * realName : 13765051712
     * type : taster
     * currency : CB
     * bizType : 201
     * bizNote : 橙券授信
     * preAmount : 0
     * postAmount : 500000
     * status : 1
     * remark : 记得对账哦
     * createDatetime : Jul 18, 2017 7:35:42 PM
     * workDate : 20170718
     * channelType : 0
     * systemCode : CD-CYC000009
     * companyCode : CD-CYC000009
     */

    private String code;
    private String refNo;

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

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

    private String accountNumber;
    private BigDecimal transAmount;
    private BigDecimal frozenAmount;
    private BigDecimal addAmount;
    private BigDecimal inAmount;
    private BigDecimal outAmount;
    private String userId;
    private String realName;
    private String type;
    private String currency;
    private String bizType;
    private String bizNote;
    private BigDecimal preAmount;
    private BigDecimal postAmount;
    private String status;
    private String remark;
    private String createDatetime;
    private String workDate;
    private String channelType;
    private String systemCode;
    private String companyCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizNote() {
        return bizNote;
    }

    public void setBizNote(String bizNote) {
        this.bizNote = bizNote;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public BigDecimal getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(BigDecimal preAmount) {
        this.preAmount = preAmount;
    }

    public BigDecimal getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(BigDecimal postAmount) {
        this.postAmount = postAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.refNo);
        dest.writeString(this.accountNumber);
        dest.writeSerializable(this.transAmount);
        dest.writeSerializable(this.frozenAmount);
        dest.writeSerializable(this.addAmount);
        dest.writeSerializable(this.inAmount);
        dest.writeSerializable(this.outAmount);
        dest.writeString(this.userId);
        dest.writeString(this.realName);
        dest.writeString(this.type);
        dest.writeString(this.currency);
        dest.writeString(this.bizType);
        dest.writeString(this.bizNote);
        dest.writeSerializable(this.preAmount);
        dest.writeSerializable(this.postAmount);
        dest.writeString(this.status);
        dest.writeString(this.remark);
        dest.writeString(this.createDatetime);
        dest.writeString(this.workDate);
        dest.writeString(this.channelType);
        dest.writeString(this.systemCode);
        dest.writeString(this.companyCode);
    }

    public BillModel() {
    }

    protected BillModel(Parcel in) {
        this.code = in.readString();
        this.refNo = in.readString();
        this.accountNumber = in.readString();
        this.transAmount = (BigDecimal) in.readSerializable();
        this.frozenAmount = (BigDecimal) in.readSerializable();
        this.addAmount = (BigDecimal) in.readSerializable();
        this.inAmount = (BigDecimal) in.readSerializable();
        this.outAmount = (BigDecimal) in.readSerializable();
        this.userId = in.readString();
        this.realName = in.readString();
        this.type = in.readString();
        this.currency = in.readString();
        this.bizType = in.readString();
        this.bizNote = in.readString();
        this.preAmount = (BigDecimal) in.readSerializable();
        this.postAmount = (BigDecimal) in.readSerializable();
        this.status = in.readString();
        this.remark = in.readString();
        this.createDatetime = in.readString();
        this.workDate = in.readString();
        this.channelType = in.readString();
        this.systemCode = in.readString();
        this.companyCode = in.readString();
    }

    public static final Parcelable.Creator<BillModel> CREATOR = new Parcelable.Creator<BillModel>() {
        @Override
        public BillModel createFromParcel(Parcel source) {
            return new BillModel(source);
        }

        @Override
        public BillModel[] newArray(int size) {
            return new BillModel[size];
        }
    };
}
