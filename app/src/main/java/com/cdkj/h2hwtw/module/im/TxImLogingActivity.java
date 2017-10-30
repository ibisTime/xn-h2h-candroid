package com.cdkj.h2hwtw.module.im;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityTxlogingBinding;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.model.immodel.getTxKeyModel;
import com.cdkj.h2hwtw.module.user.login.LoginActivity;
import com.cdkj.h2hwtw.other.TXImManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 用于腾讯im聊天
 * Created by cdkj on 2017/10/30.
 */

public class TxImLogingActivity extends AbsBaseLoadActivity {


    private boolean canOpenMain;
    private boolean isFromLogin;

    private ActivityTxlogingBinding binding;
    private ImUserInfo imUserInfo;

    /**
     * @param context
     * @param canOpenMain 如果未登录，登录后能否打开主页
     * @param isFromLogin 是否来自登录页面
     */
    public static void open(android.content.Context context, ImUserInfo imUserInfo, boolean canOpenMain, boolean isFromLogin) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TxImLogingActivity.class);
        intent.putExtra("canOpenMain", canOpenMain);
        intent.putExtra("isFromLogin", isFromLogin);
        intent.putExtra("imUserInfo", imUserInfo);
        context.startActivity(intent);
    }


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
        if (getIntent() != null) {
            canOpenMain = getIntent().getBooleanExtra("canOpenMain", false);
            isFromLogin = getIntent().getBooleanExtra("isFromLogin", false);
            imUserInfo = getIntent().getParcelableExtra("imUserInfo");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SPUtilHelpr.isLoginNoStart()) { //如果没有登录的话先登录
            LoginActivity.open(TxImLogingActivity.this, canOpenMain);
            finish();
        } else if (TXImManager.getInstance().isLogin()) {//如果腾讯云已经登录 直接打开聊天界面
            ChatC2CActivity.open(TxImLogingActivity.this, imUserInfo);
            finish();
        } else {
            getUserInfoRequest(false); //登录--> 获取用户信息 -->获取腾讯签名-->登录腾讯--->登录成功
        }
    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                SPUtilHelpr.saveisTradepwdFlag(data.isTradepwdFlag());
                SPUtilHelpr.saveUserPhoneNum(data.getMobile());
                SPUtilHelpr.saveUserName(data.getNickname());
                SPUtilHelpr.saveUserPhoto(data.getPhoto());
                getTxKeyRequest();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                finish();
            }

            @Override
            protected void onNoNet(String msg) {
                super.onNoNet(msg);
                disMissLoading();
                finish();
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


    /**
     * 获取腾讯签名
     */
    public void getTxKeyRequest() {


        Map map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelpr.getUserId());

        Call<BaseResponseModel<getTxKeyModel>> call = RetrofitUtils.createApi(MyApiServer.class).getTxSing("805953", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<getTxKeyModel>(this) {
            @Override
            protected void onSuccess(getTxKeyModel data, String SucMessage) {

                TXImManager.getInstance().init(data.getTxAppCode());

                TXImManager.getInstance().login(SPUtilHelpr.getUserId(), data.getSig(), new TXImManager.LoginBallBack() {
                    @Override
                    public void onError(int i, String s) {
                        disMissLoading();
                        finish();
                    }

                    @Override
                    public void onSuccess() {
                /*        if (canOpenMain) {
                            EventBus.getDefault().post(EventTags.MAINFINISH);
                            EventBus.getDefault().post(EventTags.AllFINISH);
                            MainActivity.open(TxImLogingActivity.this);
                        } else {
                            EventBus.getDefault().post(LOGINREFRESH);
                        }
*/
                        disMissLoading();
                        if (!isFromLogin) {
                            ChatC2CActivity.open(TxImLogingActivity.this, imUserInfo);
                        }
                        finish();
                    }
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                finish();
            }

            @Override
            protected void onNoNet(String msg) {
                super.onNoNet(msg);
                disMissLoading();
                finish();
            }

            @Override
            protected void onFinish() {

            }
        });
    }


}
