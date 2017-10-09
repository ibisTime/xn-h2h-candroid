package com.cdkj.h2hwtw;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;

import org.greenrobot.eventbus.EventBus;


/**
 * 基础Application
 * Created by Administrator on 2016/8/29.
 */

//TODO 项目目前需配置参数第三方 支付宝 高德 微信
public class MyApplication extends Application {

    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (true) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
        CdApplication.initialize(this, true, "");
        EventBus.builder().throwSubscriberException(true).installDefaultEventBus();
    }


    public static MyApplication getInstance() {
        return application;
    }
}

