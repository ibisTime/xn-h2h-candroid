package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityAboutUsBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 关于我们
 * Created by cdkj on 2017/11/2.
 */

public class AboutAsActivity extends AbsBaseLoadActivity {

    private ActivityAboutUsBinding mbinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AboutAsActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_about_us, null, false);
        return mbinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("关于我们");
        getKeyUrl("aboutUs");
        getPhoneNum();
        getTime();
    }


    public void getKeyUrl(String key) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("ckey", key);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("805917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                mbinding.webViewAboutUs.loadData(data.getCvalue(), "text/html;charset=UTF-8", "UTF-8");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AboutAsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    public void getTime() {


        Map<String, String> map = new HashMap<>();
        map.put("ckey", "serviceTime");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("805917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                mbinding.tvServiceTiem.setText("服务时间："+data.getCvalue());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AboutAsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取服务电话
     */
    public void getPhoneNum() {


        Map<String, String> map = new HashMap<>();
        map.put("ckey", "telephone");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("805917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                mbinding.tvServicePhone.setText("服务电话："+data.getCvalue());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AboutAsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}
