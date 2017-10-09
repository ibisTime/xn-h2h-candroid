package com.cdkj.baselibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell1 on 2016/12/24.
 */

public class BankCardModel implements Parcelable {


    /**
     * code : CT2016122415174773687
     * bankcardNumber : 121321312312312
     * bankName : 农业银行
     * subbranch : 余杭
     * bindMobile : 12312312312
     * userId : U2016121720251308788
     * realName : 天雷
     * type : B
     * status : 1
     * currency : CNY
     * createDatetime : Dec 24, 2016 3:17:47 PM
     * systemCode : CD-CZH000001
     */

    private String code;
    private String bankcardNumber;
    private String bankName;
    private String bankCode;
    private String subbranch;
    private String bindMobile;
    private String userId;
    private String realName;
    private String type;
    private String status;
    private String currency;
    private String createDatetime;
    private String systemCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSubbranch() {
        return subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(String bindMobile) {
        this.bindMobile = bindMobile;
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
        dest.writeString(this.code);
        dest.writeString(this.bankcardNumber);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCode);
        dest.writeString(this.subbranch);
        dest.writeString(this.bindMobile);
        dest.writeString(this.userId);
        dest.writeString(this.realName);
        dest.writeString(this.type);
        dest.writeString(this.status);
        dest.writeString(this.currency);
        dest.writeString(this.createDatetime);
        dest.writeString(this.systemCode);
    }

    public BankCardModel() {
    }

    protected BankCardModel(Parcel in) {
        this.code = in.readString();
        this.bankcardNumber = in.readString();
        this.bankName = in.readString();
        this.bankCode = in.readString();
        this.subbranch = in.readString();
        this.bindMobile = in.readString();
        this.userId = in.readString();
        this.realName = in.readString();
        this.type = in.readString();
        this.status = in.readString();
        this.currency = in.readString();
        this.createDatetime = in.readString();
        this.systemCode = in.readString();
    }

    public static final Creator<BankCardModel> CREATOR = new Creator<BankCardModel>() {
        @Override
        public BankCardModel createFromParcel(Parcel source) {
            return new BankCardModel(source);
        }

        @Override
        public BankCardModel[] newArray(int size) {
            return new BankCardModel[size];
        }
    };
}
