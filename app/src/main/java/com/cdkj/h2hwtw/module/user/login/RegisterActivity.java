package com.cdkj.h2hwtw.module.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseApiServer;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCoodePresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.MainActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityRegisterBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2017/8/8.
 */
public class RegisterActivity extends AbsBaseLoadActivity implements SendCodeInterface {

    private ActivityRegisterBinding mBinding;

    private SendPhoneCoodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, RegisterActivity.class));
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_register, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mSendCOdePresenter = new SendPhoneCoodePresenter(this);
        mBaseBinding.titleView.setMidTitle("注册");
        initListener();
    }

    private void initListener() {
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPhoneNumAndSendCode();
            }
        });


        //注册
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
                    UITipDialog.showFall(RegisterActivity.this, "请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                    UITipDialog.showFall(RegisterActivity.this, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString())) {
                    UITipDialog.showFall(RegisterActivity.this, "请输入密码");
                    return;
                }
                if (mBinding.edtPassword.getText().toString().length() < 6) {
                    UITipDialog.showFall(RegisterActivity.this, "密码不能小于6位数");
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtRepassword.getText().toString())) {
                    UITipDialog.showFall(RegisterActivity.this, "请重新输入密码");
                    return;
                }
                if (!TextUtils.equals(mBinding.edtRepassword.getText().toString(), mBinding.edtPassword.getText().toString())) {
                    UITipDialog.showFall(RegisterActivity.this, "两次输入密码不一致");
                    return;
                }

                registeRequest();
            }
        });


    }

    /**
     * 检车手机号是否可以注册然后发送验证码
     */
    private void checkPhoneNumAndSendCode() {
        if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
            UITipDialog.showFall(RegisterActivity.this, "请输入手机号");
            return;
        }
        mSendCOdePresenter.sendCodeRequest(mBinding.edtPhone.getText().toString(), "805041", MyCdConfig.USERTYPE, RegisterActivity.this);
    }


    /**
     * 注册请求
     */
    private void registeRequest() {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("mobile", mBinding.edtPhone.getText().toString());
        hashMap.put("loginPwd", mBinding.edtPassword.getText().toString());
        hashMap.put("kind", MyCdConfig.USERTYPE);
        hashMap.put("smsCaptcha", mBinding.edtCode.getText().toString());
        hashMap.put("systemCode", MyCdConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyCdConfig.COMPANYCODE);
        hashMap.put("isRegHx", "2");//是否注册聊天 2tengxIM

        Call call = RetrofitUtils.createApi(BaseApiServer.class).userRegister("805041", StringUtils.getJsonToString(hashMap));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {
            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) && !TextUtils.isEmpty(data.getUserId())) {

                    showToast("注册成功,已自动登录");

                    SPUtilHelpr.saveUserId(data.getUserId());
                    SPUtilHelpr.saveUserToken(data.getToken());
                    SPUtilHelpr.saveUserPhoneNum(mBinding.edtPhone.getText().toString());
                    EventBus.getDefault().post(EventTags.MAINFINISH);
                    EventBus.getDefault().post(EventTags.AllFINISH);

                    MainActivity.open(RegisterActivity.this);

                    finish();

                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(RegisterActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        UITipDialog.showSuccess(RegisterActivity.this, msg);
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));//启动倒计时
    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showFall(RegisterActivity.this, msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCOdePresenter != null) {
            mSendCOdePresenter.clear();
            mSendCOdePresenter = null;
        }
    }
}
