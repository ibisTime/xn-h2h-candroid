package com.cdkj.h2hwtw.model;

/**
 * Created by 李先俊 on 2017/6/10.
 */

public class ProductTypeModel {

    /**
     * code : FL201703261345184657
     * type : 1
     * parentCode : FL201703261330193710
     * name : 空调
     * pic : http://www.baidu.com
     * orderNo : 1
     * status : 0
     * companyCode : CD-CCG000007
     * systemCode : CD-CCG000007
     * parentName : 家用电器update
     */

    private String code;
    private String type;
    private String parentCode;
    private String name;
    private String pic;
    private int orderNo;
    private String status;
    private String companyCode;
    private String systemCode;
    private String parentName;

    private boolean isAllMenu;//自用参数用于添加全部菜单

    public boolean isAllMenu() {
        return isAllMenu;
    }

    public void setAllMenu(boolean allMenu) {
        isAllMenu = allMenu;
    }

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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
