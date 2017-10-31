package com.cdkj.h2hwtw.other;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cdkj.baselibrary.utils.DeviceHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.h2hwtw.BuildConfig;
import com.cdkj.h2hwtw.MyApplication;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

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
        if (instance == null) return false;
        return !TextUtils.isEmpty(TIMManager.getInstance().getLoginUser());
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

                switch (i) {
                    case 6208:
                        //离线状态下被其他终端踢下线
                        loCallBack.onError(i, "你的账号已在其他终端登录,请重新登录");
                        break;
                    case 6200:
                        loCallBack.onError(i, "暂无网络");
                        break;
                    default:
                        loCallBack.onError(i, "登陆失败");
                        break;
                }

            }

            @Override
            public void onSuccess() {
                LogUtil.E("腾讯云登录成功");
                //注册小米推送
                if (DeviceHelper.isXiaomi() || SystemUtils.isMIUI()) {//判断是不是小米设备
                    if (MsfSdkUtils.isMainProcess(MyApplication.getInstance())) {
                        Log.d("MyApplication", "main process");
                        MiPushClient.registerPush(MyApplication.getInstance(), "2882303761517633048", "5201763375048");
                    }
                }

                //全局推送开启离线推送
                TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
                settings.setEnabled(true);
                TIMManager.getInstance().setOfflinePushSettings(settings);
                if (loCallBack == null) return;
                loCallBack.onSuccess();
            }
        });
    }


    /**
     * 设置昵称
     *
     * @param name               要修改的姓名
     * @param changeInfoBallBack
     */
    public void setUserNickName(String name, final changeInfoBallBack changeInfoBallBack) {
        if (!isLogin()) {
            if (changeInfoBallBack != null) {
                changeInfoBallBack.onError(0, "");
            }
            return;
        }
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setNickname(name);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                LogUtil.E("modifyProfile failed: " + code + " desc" + desc);
                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onError(code, desc);
                }
            }

            @Override
            public void onSuccess() {
                LogUtil.E("modifyProfile succ");

                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onSuccess();
                }
            }
        });
    }

    /**
     * 设置昵称
     *
     * @param changeInfoBallBack
     */
    public void setUserLogo(String url, final changeInfoBallBack changeInfoBallBack) {
        if (!isLogin()) {
            if (changeInfoBallBack != null) {
                changeInfoBallBack.onError(0, "");
            }
            return;
        }
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(url);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                LogUtil.E("modifyProfile failed: " + code + " desc" + desc);
                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onError(code, desc);
                }
            }

            @Override
            public void onSuccess() {
                LogUtil.E("modifyProfile succ");

                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onSuccess();
                }
            }
        });
    }


    /**
     * 登出imsdk
     *
     * @param callBack 登出后回调
     */
    public void logout(TIMCallBack callBack) {
        if (!isLogin()) {
            callBack.onSuccess();
            return;
        }
        try {
            TIMManager.getInstance().logout(callBack);
        } catch (Exception e) {

        }

    }

    /**
     * 登出imsdk
     *
     * @param
     */
    public void logout() {
        if (!isLogin()) return;
        try {
            TIMManager.getInstance().logout(new TIMCallBack() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess() {

                }
            });
        } catch (Exception e) {

        }
    }


    public interface LoginBallBack {

        void onError(int i, String s);

        void onSuccess();
    }

    public interface changeInfoBallBack {

        void onError(int i, String s);

        void onSuccess();
    }

}
