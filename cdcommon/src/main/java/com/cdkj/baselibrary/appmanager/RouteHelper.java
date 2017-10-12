package com.cdkj.baselibrary.appmanager;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 路由管理
 * Created by cdkj on 2017/10/12.
 */

public class RouteHelper {
    //跳转到登录页面
    public static final String APPLOGIN = "/user/login";

    public static void openLogin(boolean canopenmain) {
        ARouter.getInstance().build(APPLOGIN)
                .withBoolean("canOpenMain", canopenmain)
                .navigation();
    }

}
