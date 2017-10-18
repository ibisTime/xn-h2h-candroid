package com.cdkj.h2hwtw.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 李先俊 on 2017/10/18.
 */

public class OrderModel {


    /**
     * code : DD2017101815422981179406
     * type : 1
     * toUser : U1111111111111111
     * receiver : 55888
     * reMobile : 55366
     * reAddress : 四川省 阿坝藏族羌族自治州 阿坝县5866
     * applyUser : U201710131355098586852
     * applyNote :
     * applyDatetime : Oct 18, 2017 3:42:29 PM
     * amount1 : 11000
     * yunfei : 12000
     * status : 91
     * payAmount1 : 0
     * payAmount2 : 0
     * payAmount3 : 0
     * promptTimes : 0
     * updater : U1111111111111111
     * updateDatetime : Oct 18, 2017 3:57:42 PM
     * remark : 不想卖了
     * companyCode : CD-WTW000016
     * systemCode : CD-WTW000016
     * productOrderList : [{"code":"CD2017101815422981276469","orderCode":"DD2017101815422981179406","productCode":"CP2017101616025424327224","productName":"包包包包包包包","productPic":"68b53042-304a-4166-af67-821aa2be04cc.JPG||8e8f336b-8daf-41ac-9129-48e025ad4f9c.jpg||b2739a51-e442-4046-a5d2-953e8f931321.png||80f6aef8-cb4f-4c16-90e1-5abbaefbf7d0.JPG","productDescription":"乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片","quantity":1,"companyCode":"CD-WTW000016","systemCode":"CD-WTW000016"}]
     */

    private String code;
    private String type;
    private String toUser;

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getYunfei() {
        return yunfei;
    }

    public void setYunfei(BigDecimal yunfei) {
        this.yunfei = yunfei;
    }

    public BigDecimal getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(BigDecimal payAmount1) {
        this.payAmount1 = payAmount1;
    }

    public BigDecimal getPayAmount2() {
        return payAmount2;
    }

    public void setPayAmount2(BigDecimal payAmount2) {
        this.payAmount2 = payAmount2;
    }

    public BigDecimal getPayAmount3() {
        return payAmount3;
    }

    public void setPayAmount3(BigDecimal payAmount3) {
        this.payAmount3 = payAmount3;
    }

    private String receiver;
    private String reMobile;
    private String reAddress;
    private String applyUser;
    private String applyNote;
    private String applyDatetime;
    private BigDecimal amount1;
    private BigDecimal yunfei;
    private String status;
    private BigDecimal payAmount1;
    private BigDecimal payAmount2;
    private BigDecimal payAmount3;
    private int promptTimes;
    private String updater;
    private String updateDatetime;
    private String remark;
    private String companyCode;
    private String systemCode;
    private List<ProductOrderListBean> productOrderList;

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

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReMobile() {
        return reMobile;
    }

    public void setReMobile(String reMobile) {
        this.reMobile = reMobile;
    }

    public String getReAddress() {
        return reAddress;
    }

    public void setReAddress(String reAddress) {
        this.reAddress = reAddress;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPromptTimes() {
        return promptTimes;
    }

    public void setPromptTimes(int promptTimes) {
        this.promptTimes = promptTimes;
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

    public List<ProductOrderListBean> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrderListBean> productOrderList) {
        this.productOrderList = productOrderList;
    }

    public static class ProductOrderListBean {
        /**
         * code : CD2017101815422981276469
         * orderCode : DD2017101815422981179406
         * productCode : CP2017101616025424327224
         * productName : 包包包包包包包
         * productPic : 68b53042-304a-4166-af67-821aa2be04cc.JPG||8e8f336b-8daf-41ac-9129-48e025ad4f9c.jpg||b2739a51-e442-4046-a5d2-953e8f931321.png||80f6aef8-cb4f-4c16-90e1-5abbaefbf7d0.JPG
         * productDescription : 乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片乒乒乓乓乒乒乓乓乒乒乓乓乒乒乓乓片
         * quantity : 1
         * companyCode : CD-WTW000016
         * systemCode : CD-WTW000016
         */

        private String code;
        private String orderCode;
        private String productCode;
        private String productName;
        private String productPic;
        private String productDescription;
        private int quantity;
        private String companyCode;
        private String systemCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductPic() {
            return productPic;
        }

        public void setProductPic(String productPic) {
            this.productPic = productPic;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
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
    }
}
