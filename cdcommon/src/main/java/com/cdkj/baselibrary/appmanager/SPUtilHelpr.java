package com.cdkj.baselibrary.appmanager;


import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.SPUtils;

/**
 * SPUtils 工具辅助类
 */

public class SPUtilHelpr {

    private static final String USERTOKEN = "user_toke_ylq";
    private static final String USERID = "user_id_ylq";

    /**
     * 设置用户token
     *
     * @param s
     */
    public static void saveUserToken(String s) {
        SPUtils.put(CdApplication.getContext(), USERTOKEN, s);
    }

    /**
     * 设置用户token
     *
     * @param
     */
    public static String getUserToken() {
        return SPUtils.getString(CdApplication.getContext(), USERTOKEN, "");
    }


    /**
     * 设置用户token
     *
     * @param s
     */
    public static void saveUserId(String s) {
        SPUtils.put(CdApplication.getContext(), USERID, s);
    }

    /**
     * 设置用户手机号码
     *
     * @param s
     */
    public static void saveUserPhoneNum(String s) {
        SPUtils.put(CdApplication.getContext(), "user_phone", s);
    }

    /**
     * 获取用户手机号
     */
    public static String getUserPhoneNum() {
        return SPUtils.getString(CdApplication.getContext(), "user_phone", "");
    }

    /**
     * 设置用户手机号码
     *
     * @param s
     */
    public static void saveUserName(String s) {
        SPUtils.put(CdApplication.getContext(), "user_name", s);
    }

    /**
     * 获取用户手机号
     */
    public static String getUserName() {
        return SPUtils.getString(CdApplication.getContext(), "user_name", "");
    }

    /**
     * 用户是否保存银行卡
     *
     * @param s
     */
    public static void saveUserIsBindCard(boolean s) {
        SPUtils.put(CdApplication.getContext(), "user_is_bind_bankcard", s);
    }

    /**
     * 获取用户是否保存银行卡
     */
    public static boolean getUserIsBindCard() {
        return SPUtils.getBoolean(CdApplication.getContext(), "user_is_bind_bankcard", false);
    }


    /**
     * 设置用户token
     *
     * @param
     */
    public static String getUserId() {
        return SPUtils.getString(CdApplication.getContext(), USERID, "");

    }


    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean isLogin(Context context, boolean canopenmain) {
        if (TextUtils.isEmpty(getUserId())) {
            SPUtilHelpr.logOutClear();
//			ToastUtil.show(context,"请先登录");
            // 路由跳转登录页面
            ARouter.getInstance().build("/user/login")
                    .withBoolean("canOpenMain", canopenmain)
                    .navigation();
            return false;
        }

        return true;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean isLoginNoStart() {
        if (TextUtils.isEmpty(getUserId())) {
            return false;
        }
        return true;
    }


    /**
     * 退出登录清除数据
     */
    public static void logOutClear() {
        saveUserToken("");
        saveUserId("");
        saveUserPhoneNum("");
        saveUserName("");
        saveUserIsBindCard(false);
    }

}
