package com.cdkj.h2hwtw.module.im;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.h2hwtw.MainActivity;
import com.cdkj.h2hwtw.MyApplication;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityTxlogingBinding;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.model.immodel.getTxKeyModel;
import com.cdkj.h2hwtw.module.user.login.LoginActivity;
import com.cdkj.h2hwtw.other.TXImManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 腾讯im推送信息获取
 * Created by cdkj on 2017/10/30.
 */

public class TxImPushActivity extends AbsBaseLoadActivity {

    private ActivityTxlogingBinding binding;
    private String toUserId;

    @Override
    public View addMainView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_txloging, null, false);
        return binding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            toUserId = getIntent().getStringExtra("toUserId");
        }
        if (!SPUtilHelpr.isLoginNoStart() || !TXImManager.getInstance().isLogin()) { //如果没有登录的话先登录
            startLogin();
        } else {
            getUserInfoRequest(false); //登录--> 获取用户信息 -->获取腾讯签名-->登录腾讯--->登录成功
        }
    }

    public void startLogin() {
        EventBus.getDefault().post(EventTags.MAINFINISH);
        EventBus.getDefault().post(EventTags.AllFINISH);
        LoginActivity.open(TxImPushActivity.this, true);
        finish();
    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        if (TextUtils.isEmpty(toUserId)) {
            finish();
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", toUserId);
        map.put("token", SPUtilHelpr.getUserToken());
        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                ImUserInfo imUserInfo = new ImUserInfo();
                imUserInfo.setToUserId(toUserId);
                imUserInfo.setUserName(data.getNickname());
                imUserInfo.setLeftImg(MyCdConfig.QINIUURL + data.getPhoto());
                imUserInfo.setRightImg(SPUtilHelpr.getUserQiniuPhoto());
                ChatC2CActivity.open(TxImPushActivity.this, imUserInfo);
                finish();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                finish();
            }

            @Override
            protected void onNoNet(String msg) {
                disMissLoading();
                finish();
            }

            @Override
            protected void onNull() {
                disMissLoading();
                finish();
            }


            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


    @Override
    public void onBackPressed() {

    }
}
