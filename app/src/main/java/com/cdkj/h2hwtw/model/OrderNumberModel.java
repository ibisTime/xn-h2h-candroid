package com.cdkj.h2hwtw.model;

/**订单数据统计
 * Created by cdkj on 2017/11/2.
 */

public class OrderNumberModel {


    /**
     * toPayCount : 6
     * payCount : 21
     * sendCount : 1
     * receiveCount : 1
     */

    private int toPayCount;
    private int payCount;
    private int sendCount;
    private int receiveCount;

    public int getToPayCount() {
        return toPayCount;
    }

    public void setToPayCount(int toPayCount) {
        this.toPayCount = toPayCount;
    }

    public int getPayCount() {
        return payCount;
    }

    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }
}
