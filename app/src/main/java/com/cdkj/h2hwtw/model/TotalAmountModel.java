package com.cdkj.h2hwtw.model;

import java.math.BigDecimal;

/**统计金额
 * Created by 李先俊 on 2017/10/14.
 */

public class TotalAmountModel {


    /**
     * inTotalAmount : 测试内容fei9
     * outTotalAmount : 测试内容q207
     * txTotalAmount : 测试内容4188
     * zjCash : 测试内容gyv6
     * zjConsume : 测试内容8ncp
     */

    private BigDecimal inTotalAmount;
    private BigDecimal outTotalAmount;
    private BigDecimal txTotalAmount;
    private String zjCash;
    private String zjConsume;

    public BigDecimal getInTotalAmount() {
        return inTotalAmount;
    }

    public void setInTotalAmount(BigDecimal inTotalAmount) {
        this.inTotalAmount = inTotalAmount;
    }

    public BigDecimal getOutTotalAmount() {
        return outTotalAmount;
    }

    public void setOutTotalAmount(BigDecimal outTotalAmount) {
        this.outTotalAmount = outTotalAmount;
    }

    public BigDecimal getTxTotalAmount() {
        return txTotalAmount;
    }

    public void setTxTotalAmount(BigDecimal txTotalAmount) {
        this.txTotalAmount = txTotalAmount;
    }

    public String getZjCash() {
        return zjCash;
    }

    public void setZjCash(String zjCash) {
        this.zjCash = zjCash;
    }

    public String getZjConsume() {
        return zjConsume;
    }

    public void setZjConsume(String zjConsume) {
        this.zjConsume = zjConsume;
    }
}
