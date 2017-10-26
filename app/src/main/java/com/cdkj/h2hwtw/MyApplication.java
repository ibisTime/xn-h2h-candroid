package com.cdkj.h2hwtw;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMLogListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;

import org.greenrobot.eventbus.EventBus;


/**
 * 基础Application
 * Created by Administrator on 2016/8/29.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (BuildConfig.IS_DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
        CdApplication.initialize(this, true, "");
        EventBus.builder().throwSubscriberException(BuildConfig.IS_DEBUG).installDefaultEventBus();

//
//        TIMSdkConfig timSdkConfig=new TIMSdkConfig(Log.ERROR);
//
//        timSdkConfig.setLogLevel(TIMLogLevel.ERROR);//设置日志错误级别  TIMLogLevel.OFF来关闭ImSDK的文件日志输出
//
//        timSdkConfig.enableLogPrint(true);//是否开启日志
//
//        timSdkConfig.enableCrashReport(true);//是否开启错误上报
//
//        TIMManager.getInstance().init(this, timSdkConfig);

    }


    public static MyApplication getInstance() {
        return application;
    }
}

