package com.cdkj.baselibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by 李先俊 on 2017/6/8.
 */

public class AppUtils {


    /**
     * 判断一个Activity 是否存在
     *
     * @param clz
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isActivityExist(Activity clz) {

        Activity activity = clz;
        if (activity == null) {
            return false;
        }

        if(activity.isFinishing()){
            return false;
        }

        if (!getAndroidVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) //如果版本小于 4.2
        {
            return true;
        }

        if (activity.isDestroyed()) {
            return false;
        }

        return true;
    }

    public static Boolean getAndroidVersion(int version) {
        if (Build.VERSION.SDK_INT >= version) {
            return true;

        } else {
            return false;
        }
    }
    /**
     * 根据包名跳转到系统自带的应用程序信息界面
     *
     * @param activity
     */
    public static void startDetailsSetting(Activity activity) {
        try {
            Uri packageURI = Uri.parse("package:" + getPackgeName(activity));
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            activity.startActivity(intent);
        }catch (Exception e){
        }
    }


    /*获取版本信息*/
    public static String getPackgeName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.packageName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /*获取版本信息*/
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    /**
     * 验证码倒计时
     *
     * @param count 秒数
     * @param btn   按钮
     * @return
     */
    public static Disposable startCodeDown(final int count, final Button btn) {
        return Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(count)//只发射开始的N项数据或者一定时间内的数据
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxView.enabled(btn).accept(false);
                        RxTextView.text(btn).accept("60秒后重发");
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        RxView.enabled(btn).accept(false);
                        RxTextView.text(btn).accept((count - aLong) + "秒后重发");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        RxView.enabled(btn).accept(true);
                        RxTextView.text(btn).accept("重发验证码");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        RxView.enabled(btn).accept(true);
                        RxTextView.text(btn).accept("重发验证码");
                    }
                });
    }


    public static void startWeb(Context context, String url) {

        LogUtil.E("downloadUrl___"+url);

        if (context == null || TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            if (!hasPreferredApplication(context, intent)) {
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            }

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
            }else{
                ToastUtil.show(context,"没有可用浏览器");
            }
        } catch (Exception e) {
            ToastUtil.show(context,"打开浏览器出现错误");
            LogUtil.E("startWeb error");
        }
    }

    //如果info.activityInfo.packageName为android,则没有设置,否则,有默认的程序.
    //判断系统是否设置了默认浏览器
    public static boolean hasPreferredApplication(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }


    /**
     * 获取手机联系人
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     *
     * @return 联系人链表
     */
    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
        LogUtil.E("调用");
//        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        try {
            // 5.解析cursor
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 6.获取查询的数据
                    String contact_id = cursor.getString(0);
                    // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
                    // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
                    // 判断contact_id是否为空
                    if (!TextUtils.isEmpty(contact_id)) {//null   ""
                        // 7.根据contact_id查询view_data表中的数据
                        // selection : 查询条件
                        // selectionArgs :查询条件的参数
                        // sortOrder : 排序
                        // 空指针: 1.null.方法 2.参数为null
                        Cursor c = resolver.query(date_uri, new String[]{"data1",
                                        "mimetype"}, "raw_contact_id=?",
                                new String[]{contact_id}, null);
                        HashMap<String, String> map = new HashMap<String, String>();
                        // 8.解析c
                        if (c != null) {
                            while (c.moveToNext()) {
                                // 9.获取数据
                                String data1 = c.getString(0);
                                String mimetype = c.getString(1);
                                // 10.根据类型去判断获取的data1数据并保存
                                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                                    // 电话
                                    map.put("mobile", data1);
                                } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                                    // 姓名
                                    map.put("name", data1);
                                }
                            }
                        }
                        // 11.添加到集合中数据
                        list.add(map);
                        // 12.关闭cursor
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            }
        } finally {
            // 12.关闭cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }



}
