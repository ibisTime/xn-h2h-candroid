package com.cdkj.baselibrary.interfaces;

import com.cdkj.baselibrary.model.LocationModel;

/**
 * 用于实现定位接口
 * Created by cdkj on 2017/11/7.
 */

public interface LocationInterface {

    void init(LocationCallBackListener listener);

    void startLocation();

    void stopLocation();

    void destroyLocation();

}
