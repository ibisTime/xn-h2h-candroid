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
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCoodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.h2hwtw.MainActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityRegisterBinding;
import com.cdkj.h2hwtw.module.common.MyBaseLoadActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2017/8/8.
 */
public class RegisterActivity extends MyBaseLoadActivity implements SendCodeInterface {

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
        setLeftImg();
        initListener();
    }

    private void initListener() {
        mBinding.btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPhoneNumAndSendCode();
            }
        });


        //注册
        mBinding.btnSureNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mBinding.editUsername.getText().toString())) {
                    showToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editPhoneCode.getText().toString())) {
                    showToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editPassword.getText().toString())) {
                    showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editPasswordRepet.getText().toString())) {
                    showToast("请重新输入密码");
                    return;
                }
                if (!TextUtils.equals(mBinding.editPasswordRepet.getText().toString(), mBinding.editPassword.getText().toString())) {
                    showToast("两次输入密码不一致");
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
        if (TextUtils.isEmpty(mBinding.editUsername.getText().toString())) {
            showToast("请输入手机号");
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("mobile", mBinding.editUsername.getText().toString());
        map.put("kind", MyCdConfig.USERTYPE);
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805040", StringUtils.getJsonToString(map));

        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    mSendCOdePresenter.sendCodeRequest(mBinding.editUsername.getText().toString(), "805041", MyCdConfig.USERTYPE, RegisterActivity.this);
                } else {
                    showToast("手机号已经存在");
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 注册请求
     */
    private void registeRequest() {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("mobile", mBinding.editUsername.getText().toString());
        hashMap.put("loginPwd", mBinding.editPassword.getText().toString());
        hashMap.put("kind", MyCdConfig.USERTYPE);
        hashMap.put("smsCaptcha", mBinding.editPhoneCode.getText().toString());
        hashMap.put("systemCode", MyCdConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyCdConfig.COMPANYCODE);

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
                    SPUtilHelpr.saveUserPhoneNum(mBinding.editUsername.getText().toString());
                    EventBus.getDefault().post(EventTags.AllFINISH);
                    EventBus.getDefault().post(EventTags.MAINFINISH);

                    MainActivity.open(RegisterActivity.this);

                    finish();

                }
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
        ToastUtil.show(this, msg);
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSendCode));//启动倒计时
    }

    @Override
    public void CodeFailed(String code, String msg) {
        showToast(msg);
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

    @Override
    public int getLoadTitleBg() {
        return 1;
    }
}
