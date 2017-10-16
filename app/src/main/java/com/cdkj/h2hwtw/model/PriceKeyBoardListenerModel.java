package com.cdkj.h2hwtw.model;

/**
 * Created by cdkj on 2017/10/16.
 */

public class PriceKeyBoardListenerModel {

    private String price;
    private String oldPrice;
    private String sendPrice;//运费
    private boolean isCanSend;//是

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(String sendPrice) {
        this.sendPrice = sendPrice;
    }

    public boolean isCanSend() {
        return isCanSend;
    }

    public void setCanSend(boolean canSend) {
        isCanSend = canSend;
    }

    // 否包邮


}
