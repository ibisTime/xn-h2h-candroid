package com.cdkj.h2hwtw.model;

import java.util.List;

/**
 * Created by 李先俊 on 2017/7/19.
 */

public class BillListMode {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 1
     * totalPage : 1
     * list : [{"code":"AJ201707181935421546289284138","refNo":"EC20170718193542151876827435","accountNumber":"A201707181621322293533626016","transAmount":500000,"userId":"U20170718162132217422","realName":"13765051712","type":"taster","currency":"CB","bizType":"201","bizNote":"橙券授信","preAmount":0,"postAmount":500000,"status":"1","remark":"记得对账哦","createDatetime":"Jul 18, 2017 7:35:42 PM","workDate":"20170718","channelType":"0","systemCode":"CD-CYC000009","companyCode":"CD-CYC000009"}]
     */

    private List<BillModel> list;

    public List<BillModel> getList() {
        return list;
    }

    public void setList(List<BillModel> list) {
        this.list = list;
    }
}
