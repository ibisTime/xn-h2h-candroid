package com.cdkj.baselibrary.model;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by 李先俊 on 2017/6/28.
 */

public class IntroductionDkeyModel implements IPickerViewData {


    /**
     * id : 153
     * type : 1
     * parentKey : back_kd_company
     * dkey : STO
     * dvalue : 申通快递
     * updater : admin
     * updateDatetime : Oct 19, 2017 9:08:38 PM
     * remark :
     * companyCode : CD-WTW000016
     * systemCode : CD-WTW000016
     */

    private int id;
    private String type;
    private String parentKey;
    private String dkey;
    private String dvalue;
    private String updater;
    private String updateDatetime;
    private String remark;
    private String companyCode;
    private String systemCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getDkey() {
        return dkey;
    }

    public void setDkey(String dkey) {
        this.dkey = dkey;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
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

    @Override
    public String getPickerViewText() {
        return dvalue;
    }
}
