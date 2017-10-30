package com.cdkj.h2hwtw;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.h2hwtw.other.TXImManager;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMLogListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

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
    }


    public static MyApplication getInstance() {
        return application;
    }
}

