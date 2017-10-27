package com.cdkj.h2hwtw.other;

import android.text.TextUtils;

import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.h2hwtw.BuildConfig;
import com.cdkj.h2hwtw.MyApplication;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

/**
 * 腾讯云Im管理
 * Created by cdkj on 2017/10/27.
 */

public class TXImManager {

    private static TXImManager instance = null;

    private TXImManager() {
    }

    public static TXImManager getInstance() {
        if (instance == null) {
            instance = new TXImManager();
        }
        return instance;
    }


    /**
     * SDK初始化
     *
     * @param appid
     */
    public void init(int appid) {
        if (MsfSdkUtils.isMainProcess(MyApplication.getInstance())) {
            //初始化SDK基本配置
            TIMSdkConfig config = new TIMSdkConfig(appid)
                    .enableCrashReport(false)//是否开启错误上报
                    .enableLogPrint(true)//是否开启日志
                    .setLogLevel(BuildConfig.IS_DEBUG ? TIMLogLevel.ERROR : TIMLogLevel.OFF);

            //初始化SDK
            TIMManager.getInstance().init(MyApplication.getInstance(), config);

        }
    }

    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return TextUtils.isEmpty(TIMManager.getInstance().getLoginUser());
    }

    /**
     * 登录
     *
     * @param id
     * @param sig
     * @param loCallBack
     */
    public void login(String id, String sig, final LoginBallBack loCallBack) {
        TIMManager.getInstance().login(id, sig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.E("腾讯云登录失败" + i + s);
                if (loCallBack == null) return;
                loCallBack.onError(i, s);
            }

            @Override
            public void onSuccess() {
                LogUtil.E("腾讯云登录成功");
                if (loCallBack == null) return;
                loCallBack.onSuccess();
            }
        });
    }


    public interface LoginBallBack {

        void onError(int i, String s);

        void onSuccess();
    }

}
