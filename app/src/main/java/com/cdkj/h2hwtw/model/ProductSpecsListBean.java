package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by 李先俊 on 2017/10/16.
 */


public  class ProductSpecsListBean implements Parcelable {
    /**
     * code : PS2017061618003802618928
     * name : 型号
     * productCode : CP2017061614173859925599
     * originalPrice : 20000000
     * price1 : 10
     * quantity : 20
     * orderNo : 2
     * companyCode : CD-JKEG000011
     * systemCode : CD-JKEG000011
     */

    private String code;
    private String name;
    private String productCode;
    private BigDecimal originalPrice;
    private BigDecimal price1;
    private int quantity;
    private int orderNo;
    private String companyCode;
    private String systemCode;

    private int mBuyNum=0;//自定义属性 用于保存用户选择数量

    public int getmBuyNum() {
        return mBuyNum;
    }

    public void setmBuyNum(int mBuyNum) {
        this.mBuyNum = mBuyNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
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

    public ProductSpecsListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.productCode);
        dest.writeSerializable(this.originalPrice);
        dest.writeSerializable(this.price1);
        dest.writeInt(this.quantity);
        dest.writeInt(this.orderNo);
        dest.writeString(this.companyCode);
        dest.writeString(this.systemCode);
        dest.writeInt(this.mBuyNum);
    }

    protected ProductSpecsListBean(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.productCode = in.readString();
        this.originalPrice = (BigDecimal) in.readSerializable();
        this.price1 = (BigDecimal) in.readSerializable();
        this.quantity = in.readInt();
        this.orderNo = in.readInt();
        this.companyCode = in.readString();
        this.systemCode = in.readString();
        this.mBuyNum = in.readInt();
    }

    public static final Creator<ProductSpecsListBean> CREATOR = new Creator<ProductSpecsListBean>() {
        @Override
        public ProductSpecsListBean createFromParcel(Parcel source) {
            return new ProductSpecsListBean(source);
        }

        @Override
        public ProductSpecsListBean[] newArray(int size) {
            return new ProductSpecsListBean[size];
        }
    };
}