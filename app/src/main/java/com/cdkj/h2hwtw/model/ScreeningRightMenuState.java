package com.cdkj.h2hwtw.model;

/**
 * 用于保存右边筛选菜单选择状态
 * Created by cdkj on 2017/10/25.
 */

public class ScreeningRightMenuState {

    private boolean isSend;//是否包邮
    private boolean isNew;//是否全新

    private String lowPrice;//最低价
    private String heightPrice;//最高价

    private int pricePositio = -5;//价格选中需索引

    private String requestHeightPrice;//选择的最高价
    private String requestLowPrice;//选择的最低价

    public String getRequestHeightPrice() {
        return requestHeightPrice;
    }

    public void setRequestHeightPrice(String requestHeightPrice) {
        this.requestHeightPrice = requestHeightPrice;
    }

    public String getRequestLowPrice() {
        return requestLowPrice;
    }

    public void setRequestLowPrice(String requestLowPrice) {
        this.requestLowPrice = requestLowPrice;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getHeightPrice() {
        return heightPrice;
    }

    public void setHeightPrice(String heightPrice) {
        this.heightPrice = heightPrice;
    }

    public int getPricePositio() {
        return pricePositio;
    }

    public void setPricePositio(int pricePositio) {
        this.pricePositio = pricePositio;
    }
}
