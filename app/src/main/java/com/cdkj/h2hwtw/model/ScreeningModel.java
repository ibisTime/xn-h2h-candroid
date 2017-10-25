package com.cdkj.h2hwtw.model;

/**
 * Created by 李先俊 on 2017/10/25.
 */

public class ScreeningModel {

    private String area;
    private String city;
    private String province;
    private String latitude;
    private String longitude;

    private String yunfei;
//    private String orderColumn;//price
//    private String orderDir; //asc desc
    private String maxPrice;
    private String minPrice;
    private boolean isNew;
    private boolean isUpPrice;
    private String category;
    private String type;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getYunfei() {
        return yunfei;
    }

    public void setYunfei(String yunfei) {
        this.yunfei = yunfei;
    }


    public boolean isUpPrice() {
        return isUpPrice;
    }

    public void setUpPrice(boolean upPrice) {
        isUpPrice = upPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
