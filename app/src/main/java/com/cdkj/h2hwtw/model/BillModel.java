package com.cdkj.h2hwtw.model;

import java.math.BigDecimal;

/**
 * Created by LeiQ on 2016/12/30.
 */

public class BillModel {

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
    private String accountNumber;
    private BigDecimal transAmount;
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
}
