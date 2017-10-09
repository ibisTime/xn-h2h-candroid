package com.cdkj.baselibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 吐司
 *
 * @author mxy
 * @time 2016/9/6
 */
public class ToastUtil {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacksAndMessages(null);
        if (mToast != null){
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        } else{
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        }

        mToast.show();
        mHandler.postDelayed(r,900);
    }

    public static void show(Context context, String info) {

        if(context==null)
        {
            return;
        }

        try {
            showToast(context, info, Toast.LENGTH_LONG);
        }catch (Exception e){
             LogUtil.E("Toast 错误"+ e.toString());
        }

    }

    public static void show(Context context, int info) {
        if(context==null)
        {
            return;
        }
        try{
            showToast(context, info+"", Toast.LENGTH_LONG);
        }catch (Exception e){
            LogUtil.E("Toast 错误"+ e.toString());
        }
    }
}
