package com.cdkj.h2hwtw.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 李先俊 on 2017/10/18.
 */

public class OrderModel {

    /**
     * code : DD2017101714122392762296
     * type : 1
     * toUser : U1111111111111111
     * receiver : 吴联请
     * reMobile : 17890987777
     * reAddress : 浙江省杭州市余杭区仓前街道梦想小镇天使村8幢宜车叮叮2楼
     * applyUser : U1111111111111111
     * applyNote : sdfdsfsfsdfafd
     * applyDatetime : Oct 17, 2017 2:12:23 PM
     * amount1 : 20000
     * yunfei : 10000
     * status : 5
     * payType : 68
     * payDatetime : Oct 17, 2017 7:59:48 PM
     * payAmount1 : 0
     * payAmount2 : 14950000
     * payAmount3 : 100
     * couponCode : CP2017102900253467399
     * promptTimes : 0
     * deliverer : U1111111111111111
     * deliveryDatetime : Oct 18, 2017 2:17:39 PM
     * logisticsCode : 333333333333
     * logisticsCompany : SF
     * signer : U1111111111111111
     * signDatetime : Oct 18, 2017 3:00:39 PM
     * updater : U1111111111111111
     * updateDatetime : Oct 18, 2017 2:36:05 PM
     * remark : 已评论
     * companyCode : CD-WTW000016
     * systemCode : CD-WTW000016
     * productOrderList : [{"code":"CD2017101714122392720565","orderCode":"DD2017101714122392762296","productCode":"CP2017101617132151229224","productName":"名称名称名称名称名称名称名称名称名称名称名称名称名称名称名称","productPic":"763f6098-1c7e-457e-8b6e-c4f1a5f8407d.png||d1b6d8af-5523-4a32-8ed9-7534a3e23a42.JPG||a2190ab7-3287-471b-aac0-9fcf569322ba.png||cb2b434a-368a-4cb8-af70-590467398cce.JPG||d786cde5-14fb-4915-ac37-6c418b1a9d8b.jpg","productDescription":"简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介","quantity":1,"companyCode":"CD-WTW000016","systemCode":"CD-WTW000016"}]
     * user : {"userId":"U1111111111111111","kind":"C","level":"0","loginName":"18868824532","nickname":"昵称昵称2","photo":"69c0bbbe-66ec-4f3a-a350-9d1473f3711d.JPG","mobile":"18868824532","identityFlag":"0","loginLogDatetime":"Oct 18, 2017 3:31:05 PM"}
     * companyUser : {"userId":"U1111111111111111","kind":"C","level":"0","loginName":"18868824532","nickname":"昵称昵称2","photo":"69c0bbbe-66ec-4f3a-a350-9d1473f3711d.JPG","mobile":"18868824532","identityFlag":"0","loginLogDatetime":"Oct 18, 2017 3:31:05 PM"}
     */

    private String code;
    private String type;
    private String toUser;
    private String receiver;
    private String reMobile;
    private String reAddress;
    private String applyUser;
    private String applyNote;
    private String applyDatetime;
    private BigDecimal amount1;
    private BigDecimal yunfei;
    private String status;
    private String payType;
    private String payDatetime;
    private BigDecimal payAmount1;
    private BigDecimal payAmount2;
    private BigDecimal payAmount3;
    private String couponCode;
    private int promptTimes;
    private String deliverer;
    private String deliveryDatetime;
    private String logisticsCode;
    private String logisticsCompany;
    private String signer;
    private String signDatetime;
    private String updater;
    private String updateDatetime;
    private String remark;
    private String companyCode;
    private String systemCode;

    public OrderUserModel getUser() {
        return user;
    }

    public void setUser(OrderUserModel user) {
        this.user = user;
    }

    public OrderCompanyUser getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(OrderCompanyUser companyUser) {
        this.companyUser = companyUser;
    }

    private OrderUserModel user;
    private OrderCompanyUser companyUser;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getPromptTimes() {
        return promptTimes;
    }

    public void setPromptTimes(int promptTimes) {
        this.promptTimes = promptTimes;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public String getDeliveryDatetime() {
        return deliveryDatetime;
    }

    public void setDeliveryDatetime(String deliveryDatetime) {
        this.deliveryDatetime = deliveryDatetime;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getSignDatetime() {
        return signDatetime;
    }

    public void setSignDatetime(String signDatetime) {
        this.signDatetime = signDatetime;
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
         * code : CD2017101714122392720565
         * orderCode : DD2017101714122392762296
         * productCode : CP2017101617132151229224
         * productName : 名称名称名称名称名称名称名称名称名称名称名称名称名称名称名称
         * productPic : 763f6098-1c7e-457e-8b6e-c4f1a5f8407d.png||d1b6d8af-5523-4a32-8ed9-7534a3e23a42.JPG||a2190ab7-3287-471b-aac0-9fcf569322ba.png||cb2b434a-368a-4cb8-af70-590467398cce.JPG||d786cde5-14fb-4915-ac37-6c418b1a9d8b.jpg
         * productDescription : 简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介
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
