package com.cdkj.h2hwtw.model;

import java.math.BigDecimal;

/**
 * Created by 李先俊 on 2017/10/21.
 */

public class WantProductModel {


    /**
     * code : IN20171029404364172
     * type : 1
     * entityCode : CP201710211536174736910
     * interacter : U201710131355098586852
     * interactDatetime : Oct 21, 2017 4:36:41 PM
     * companyCode : CD-WTW000016
     * systemCode : CD-WTW000016
     * product : {"code":"CP201710211536174736910","storeCode":"U201710131354390878485","isJoin":"0","kind":"1","category":"FL20171016100413684635","type":"FL2017101610184775410120","name":"哈哈哈","pic":"iOS_1508571365556113_2448_3264.jpg||iOS_1508571365556241_750_1334.jpg","description":"氢气球","province":"浙江省","city":"杭州市","area":"余杭区","longitude":"119.9973633120","latitude":"30.2905885429","isNew":"0","originalPrice":999999000,"price":999999000,"yunfei":999000,"discount":1,"isPublish":"1","publishDatetime":"Oct 21, 2017 3:36:17 PM","status":"3","updater":"U201710131354390878485","updateDatetime":"Oct 21, 2017 3:36:17 PM","boughtCount":0,"companyCode":"CD-WTW000016","systemCode":"CD-WTW000016","categoryName":"旅行包","typeName":"旅行包"}
     */

    private String code;
    private String type;
    private String entityCode;
    private String interacter;
    private String interactDatetime;
    private String companyCode;
    private String systemCode;
    private ProductBean product;

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

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getInteracter() {
        return interacter;
    }

    public void setInteracter(String interacter) {
        this.interacter = interacter;
    }

    public String getInteractDatetime() {
        return interactDatetime;
    }

    public void setInteractDatetime(String interactDatetime) {
        this.interactDatetime = interactDatetime;
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

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean {
        /**
         * code : CP201710211536174736910
         * storeCode : U201710131354390878485
         * isJoin : 0
         * kind : 1
         * category : FL20171016100413684635
         * type : FL2017101610184775410120
         * name : 哈哈哈
         * pic : iOS_1508571365556113_2448_3264.jpg||iOS_1508571365556241_750_1334.jpg
         * description : 氢气球
         * province : 浙江省
         * city : 杭州市
         * area : 余杭区
         * longitude : 119.9973633120
         * latitude : 30.2905885429
         * isNew : 0
         * originalPrice : 999999000
         * price : 999999000
         * yunfei : 999000
         * discount : 1
         * isPublish : 1
         * publishDatetime : Oct 21, 2017 3:36:17 PM
         * status : 3
         * updater : U201710131354390878485
         * updateDatetime : Oct 21, 2017 3:36:17 PM
         * boughtCount : 0
         * companyCode : CD-WTW000016
         * systemCode : CD-WTW000016
         * categoryName : 旅行包
         * typeName : 旅行包
         */

        private String code;
        private String storeCode;
        private String isJoin;
        private String kind;
        private String category;
        private String type;
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
        private BigDecimal discount;
        private String isPublish;
        private String publishDatetime;
        private String status;
        private String updater;
        private String updateDatetime;
        private int boughtCount;
        private String companyCode;
        private String systemCode;
        private String categoryName;
        private String typeName;

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

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
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

        public BigDecimal getYunfei() {
            return yunfei;
        }

        public void setYunfei(BigDecimal yunfei) {
            this.yunfei = yunfei;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
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
    }
}
