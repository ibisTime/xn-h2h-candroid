package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李先俊 on 2017/10/16.
 */

public class ProductListModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 3
     * totalPage : 1
     * list : [{"code":"CP2017101613092345344782","storeCode":"U1111111111111111","isJoin":"0","kind":"1","name":"商品名称","pic":"3f855190-6e04-4f31-be6a-ca0706da60aa.JPG||0762ff67-2263-421d-a75b-084dee8ab917.jpg||e4a0c16c-9f2a-42e0-8fd3-5f395d1dc92b.png||036d0339-6cf2-419b-b548-86771c418fa1.png||805f7487-7076-4c77-9d85-d5c40a399744.JPG","description":"商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述","province":"浙江省","city":"杭州市","area":"余杭区","longitude":"120.00205","latitude":"30.28816","isNew":"1","originalPrice":2000000,"price":100000,"yunfei":0,"isPublish":"1","publishDatetime":"Oct 16, 2017 1:09:23 PM","location":"1","orderNo":0,"status":"3","updater":"admin","updateDatetime":"Oct 16, 2017 3:33:16 PM","remark":"","boughtCount":0,"companyCode":"CD-WTW000016","systemCode":"CD-WTW000016","productSpecsList":[]},{"code":"CP201710131038152923685","storeCode":"SYS_USER_WTW","kind":"1","category":"UH","type":"UH","name":"胶囊咖啡机","pic":"3f855190-6e04-4f31-be6a-ca0706da60aa.JPG||0762ff67-2263-421d-a75b-084dee8ab917.jpg||e4a0c16c-9f2a-42e0-8fd3-5f395d1dc92b.png||036d0339-6cf2-419b-b548-86771c418fa1.png||805f7487-7076-4c77-9d85-d5c40a399744.JPG","description":"图文详情","province":"所在省","city":"string","area":"string","longitude":"80","latitude":"80","isNew":"1","originalPrice":300,"price":100,"yunfei":11,"discount":80,"isPublish":"1","publishDatetime":"Oct 13, 2017 10:38:15 AM","location":"1","orderNo":1,"status":"3","updater":"admin","updateDatetime":"Oct 16, 2017 3:33:28 PM","remark":"string","boughtCount":0,"companyCode":"CD-WTW000016","systemCode":"CD-WTW000016","productSpecsList":[]},{"code":"CP2017101310332638618807","storeCode":"SYS_USER_WTW","kind":"1","category":"UH","type":"UH","name":"胶囊咖啡机","pic":"3f855190-6e04-4f31-be6a-ca0706da60aa.JPG||0762ff67-2263-421d-a75b-084dee8ab917.jpg||e4a0c16c-9f2a-42e0-8fd3-5f395d1dc92b.png||036d0339-6cf2-419b-b548-86771c418fa1.png||805f7487-7076-4c77-9d85-d5c40a399744.JPG","description":"图文详情","province":"所在省","city":"string","area":"string","longitude":"80","latitude":"80","isNew":"1","originalPrice":200,"price":100,"yunfei":11,"discount":80,"isPublish":"1","publishDatetime":"Oct 13, 2017 10:33:30 AM","location":"1","orderNo":2,"status":"3","updater":"admin","updateDatetime":"Oct 16, 2017 3:33:38 PM","remark":"string","boughtCount":0,"companyCode":"CD-WTW000016","systemCode":"CD-WTW000016","productSpecsList":[]}]
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * code : CP2017101613092345344782
         * storeCode : U1111111111111111
         * isJoin : 0
         * kind : 1
         * name : 商品名称
         * pic : 3f855190-6e04-4f31-be6a-ca0706da60aa.JPG||0762ff67-2263-421d-a75b-084dee8ab917.jpg||e4a0c16c-9f2a-42e0-8fd3-5f395d1dc92b.png||036d0339-6cf2-419b-b548-86771c418fa1.png||805f7487-7076-4c77-9d85-d5c40a399744.JPG
         * description : 商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述商品描述描述描述
         * province : 浙江省
         * city : 杭州市
         * area : 余杭区
         * longitude : 120.00205
         * latitude : 30.28816
         * isNew : 1
         * originalPrice : 2000000
         * price : 100000
         * yunfei : 0
         * isPublish : 1
         * publishDatetime : Oct 16, 2017 1:09:23 PM
         * location : 1
         * orderNo : 0
         * status : 3
         * updater : admin
         * updateDatetime : Oct 16, 2017 3:33:16 PM
         * remark :
         * boughtCount : 0
         * companyCode : CD-WTW000016
         * systemCode : CD-WTW000016
         * productSpecsList : []
         * category : UH
         * type : UH
         * discount : 80
         */

        private String code;
        private String storeCode;
        private String isJoin;
        private String kind;
        private String name;
        private String pic;
        private String description;
        private String province;
        private String city;
        private String area;
        private String longitude;
        private String latitude;
        private String isNew;
        private BigDecimal originalPrice;
        private BigDecimal price;
        private BigDecimal yunfei;
        private String isPublish;
        private String publishDatetime;
        private String location;
        private int orderNo;
        private String status;
        private String updater;
        private String updateDatetime;
        private String remark;
        private int boughtCount;
        private String companyCode;
        private String systemCode;
        private String category;
        private String type;
        private String categoryName;
        private String typeName;
        private String isCollect;
        private BigDecimal discount;
        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }





        public String getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(String isCollect) {
            this.isCollect = isCollect;
        }


        private List<ProductSpecsListBean> productSpecsList;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStoreCode() {
            return storeCode;
        }

        public void setStoreCode(String storeCode) {
            this.storeCode = storeCode;
        }

        public String getIsJoin() {
            return isJoin;
        }

        public void setIsJoin(String isJoin) {
            this.isJoin = isJoin;
        }

        public BigDecimal getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(BigDecimal originalPrice) {
            this.originalPrice = originalPrice;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getKind() {
            return kind;

        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }


        public BigDecimal getYunfei() {
            return yunfei;
        }

        public void setYunfei(BigDecimal yunfei) {
            this.yunfei = yunfei;
        }

        public String getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(String isPublish) {
            this.isPublish = isPublish;
        }

        public String getPublishDatetime() {
            return publishDatetime;
        }

        public void setPublishDatetime(String publishDatetime) {
            this.publishDatetime = publishDatetime;
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

        public int getBoughtCount() {
            return boughtCount;
        }

        public void setBoughtCount(int boughtCount) {
            this.boughtCount = boughtCount;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ProductSpecsListBean> getProductSpecsList() {
            return productSpecsList;
        }

        public void setProductSpecsList(List<ProductSpecsListBean> productSpecsList) {
            this.productSpecsList = productSpecsList;
        }

        public ListBean() {
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.code);
            dest.writeString(this.storeCode);
            dest.writeString(this.isJoin);
            dest.writeString(this.kind);
            dest.writeString(this.name);
            dest.writeString(this.pic);
            dest.writeString(this.description);
            dest.writeString(this.province);
            dest.writeString(this.city);
            dest.writeString(this.area);
            dest.writeString(this.longitude);
            dest.writeString(this.latitude);
            dest.writeString(this.isNew);
            dest.writeSerializable(this.originalPrice);
            dest.writeSerializable(this.price);
            dest.writeSerializable(this.yunfei);
            dest.writeString(this.isPublish);
            dest.writeString(this.publishDatetime);
            dest.writeString(this.location);
            dest.writeInt(this.orderNo);
            dest.writeString(this.status);
            dest.writeString(this.updater);
            dest.writeString(this.updateDatetime);
            dest.writeString(this.remark);
            dest.writeInt(this.boughtCount);
            dest.writeString(this.companyCode);
            dest.writeString(this.systemCode);
            dest.writeString(this.category);
            dest.writeString(this.type);
            dest.writeString(this.categoryName);
            dest.writeString(this.typeName);
            dest.writeString(this.isCollect);
            dest.writeSerializable(this.discount);
            dest.writeTypedList(this.productSpecsList);
        }

        protected ListBean(Parcel in) {
            this.code = in.readString();
            this.storeCode = in.readString();
            this.isJoin = in.readString();
            this.kind = in.readString();
            this.name = in.readString();
            this.pic = in.readString();
            this.description = in.readString();
            this.province = in.readString();
            this.city = in.readString();
            this.area = in.readString();
            this.longitude = in.readString();
            this.latitude = in.readString();
            this.isNew = in.readString();
            this.originalPrice = (BigDecimal) in.readSerializable();
            this.price = (BigDecimal) in.readSerializable();
            this.yunfei = (BigDecimal) in.readSerializable();
            this.isPublish = in.readString();
            this.publishDatetime = in.readString();
            this.location = in.readString();
            this.orderNo = in.readInt();
            this.status = in.readString();
            this.updater = in.readString();
            this.updateDatetime = in.readString();
            this.remark = in.readString();
            this.boughtCount = in.readInt();
            this.companyCode = in.readString();
            this.systemCode = in.readString();
            this.category = in.readString();
            this.type = in.readString();
            this.categoryName = in.readString();
            this.typeName = in.readString();
            this.isCollect = in.readString();
            this.discount = (BigDecimal) in.readSerializable();
            this.productSpecsList = in.createTypedArrayList(ProductSpecsListBean.CREATOR);
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }
}
