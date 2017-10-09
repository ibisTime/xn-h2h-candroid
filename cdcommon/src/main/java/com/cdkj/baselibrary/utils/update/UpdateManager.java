package com.cdkj.baselibrary.utils.update;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.TypeInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * apk下载更新管理
 * Created by 李先俊 on 2017/8/19.
 */

public class UpdateManager {

    private Call call;
    private String mAppName;

    public UpdateManager(String appName) {
        mAppName = appName;
    }

    private UpdateManager() {

    }

    public void checkNewApp(final Context context) {

        Map<String, String> map = new HashMap<>();
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("type", "android-c");

        call = RetrofitUtils.getBaseAPiService().getTypeSystemInfo("805918", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<TypeInfoModel>(context) {
            @Override
            protected void onSuccess(TypeInfoModel data, String SucMessage) {

                if (TextUtils.isEmpty(data.getDownloadUrl())) {
                    return;
                }

                //比对版本号判断是否更新
                if (!TextUtils.equals(data.getVersion(), AppUtils.getAppVersionName(context)) && !TextUtils.isEmpty(AppUtils.getAppVersionName(context))) {
                    if (TextUtils.equals(data.getForceUpdate(), "1")) {//强制更新
                        showUpdateDialog2(context, data.getDownloadUrl(), data.getNote());
                    } else {
                        showUpdateDialog(context, data.getDownloadUrl(), data.getNote());
                    }
                }

            }

            @Override
            protected void onFinish() {

            }
        });

    }

    public void clear() {
        if (call != null) {
            call.cancel();
            call = null;
        }
    }


    /**
     * 展示更新信息
     */
    private void showUpdateDialog(final Context context, final String url, final String note) {

//        final String path = ApkLoaderUtil.getFilePath(context);

        new CommonDialog(context).builder().setTitle("发现新版本！").setContentMsg(note)
                .setPositiveBtn("立刻更新", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {

                        AppUtils.startWeb(context, url);

        /*                final DownloadDialog mDownloadDialog = new DownloadDialog(context, mAppName + "正在更新", false);
                        mDownloadDialog.setCancelable(false);
                        mDownloadDialog.show();
                        new ApkLoaderUtil().loadApk(path, url, new APKDownloadListener() {
                            @Override
                            public void loadProgress(float progress) {
                                mDownloadDialog.setProgress(progress);
                            }

                            @Override
                            public void loadCompeted(float progress) {
                                ApkLoaderUtil.installApk(context);
                                mDownloadDialog.setProgress(progress);
                                mDownloadDialog.dismiss();
                            }

                            @Override
                            public void loadLoadError(float progress) {
                                mDownloadDialog.setProgress(progress);
                                mDownloadDialog.dismiss();
                            }
                        });*/
                    }
                })
                .setNegativeBtn("取消", null).setCancelable(false).show();

    }


    /**
     * 展示更新信息
     */
    private void showUpdateDialog2(final Context context, final String url, final String note) {

//        final String path = ApkLoaderUtil.getFilePath(context);

        new CommonDialog(context).builder().setTitle("发现新版本！").setContentMsg(note)
                .setPositiveBtn("立刻更新", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        AppUtils.startWeb(context, url);
                        EventBus.getDefault().post(EventTags.AllFINISH);
                        EventBus.getDefault().post(EventTags.MAINFINISH);
                     /*   final DownloadDialog mDownloadDialog = new DownloadDialog(context, "正在更新", true);
                        mDownloadDialog.setCancelable(false);
                        mDownloadDialog.show();
                        new ApkLoaderUtil().loadApk(path, url, new APKDownloadListener() {
                            @Override
                            public void loadProgress(float progress) {
                                mDownloadDialog.setProgress(progress);
                            }

                            @Override
                            public void loadCompeted(float progress) {
                                ApkLoaderUtil.installApk(context);
                                mDownloadDialog.setProgress(progress);
                                mDownloadDialog.dismiss();
                            }

                            @Override
                            public void loadLoadError(float progress) {
                                mDownloadDialog.setProgress(progress);
                            }
                        });*/
                    }
                })
                .setCancelable(false).show();

    }

}
