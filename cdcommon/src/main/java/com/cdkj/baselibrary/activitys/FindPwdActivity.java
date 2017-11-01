package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPasswordBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCoodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;

/**
 * 找回密码
 */
public class FindPwdActivity extends AbsBaseLoadActivity implements SendCodeInterface {

    private ActivityModifyPasswordBinding mBinding;

    private String mPhoneNumber;

    private SendPhoneCoodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String mPhoneNumber) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FindPwdActivity.class);
        intent.putExtra("phonenumber", mPhoneNumber);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_password, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("修改密码");

        mSendCOdePresenter = new SendPhoneCoodePresenter(this);
        if (getIntent() != null) {
            mPhoneNumber = getIntent().getStringExtra("phonenumber");
        }

        if (!TextUtils.isEmpty(mPhoneNumber)) {
            mBinding.edtPhone.setText(mPhoneNumber);
            mBinding.edtPhone.setSelection(mBinding.edtPhone.getText().toString().length());
        }

        initListener();
    }

    /**
     *
     */
    private void initListener() {

        //发送验证码
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendCOdePresenter.sendCodeRequest(mBinding.edtPhone.getText().toString(), "805063", MyCdConfig.USERTYPE, FindPwdActivity.this);
            }
        });


        //确定
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
                    UITipDialog.showFall(FindPwdActivity.this, "请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                    UITipDialog.showFall(FindPwdActivity.this, "请输入验证码");
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString())) {
                    UITipDialog.showFall(FindPwdActivity.this, "请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtRepassword.getText().toString())) {
                    UITipDialog.showFall(FindPwdActivity.this, "请重新输入密码");
                    return;
                }

                if (mBinding.edtPassword.getText().length() < 6) {
                    UITipDialog.showFall(FindPwdActivity.this, "密码不少于6位");
                    return;
                }

                if (!mBinding.edtPassword.getText().toString().equals(mBinding.edtRepassword.getText().toString())) {
                    UITipDialog.showFall(FindPwdActivity.this, "两次密码输入不一致");
                    return;
                }

                findPwdReqeust();
            }
        });
    }


    /**
     * 找回密码请求
     */
    private void findPwdReqeust() {

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("mobile", mBinding.edtPhone.getText().toString());
        hashMap.put("newLoginPwd", mBinding.edtPassword.getText().toString());
        hashMap.put("smsCaptcha", mBinding.edtCode.getText().toString());
        hashMap.put("kind", MyCdConfig.USERTYPE);
        hashMap.put("systemCode", MyCdConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805063", StringUtils.getJsonToString(hashMap));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(FindPwdActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(FindPwdActivity.this, "密码修改成功", new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });
                } else {
                    UITipDialog.showFall(FindPwdActivity.this, "密码修改失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(FindPwdActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    @Override
    public void CodeSuccess(String msg) {
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showFall(FindPwdActivity.this, msg);
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
