package com.cdkj.baselibrary.interfaces;

import com.cdkj.baselibrary.model.LocationModel;

/**
 * 李先俊
 * Created by cdkj on 2017/11/7.
 */

public interface LocationCallBackListener {

    void locationSuccessful(LocationModel locationModel);//定位成功

    void locationFailure(String msg);//定位失败

    void noPermissions();//没有定位权限
}
