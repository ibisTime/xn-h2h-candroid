package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPhoneBinding;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCoodePresenter;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 更换手机号码
 * Created by 李先俊 on 2017/6/16.
 */

public class UpdatePhoneActivity extends AbsBaseActivity implements SendCodeInterface {

    private ActivityModifyPhoneBinding mBinding;

    private SendPhoneCoodePresenter mSendCodePresenter;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UpdatePhoneActivity.class);

        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_phone, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle("修改手机号");
        setSubLeftImgState(true);

        mSendCodePresenter = new SendPhoneCoodePresenter(this);

        initListener();
    }

    private void initListener() {
        //发送验证码
        mBinding.btnSendNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendCodePresenter.sendCodeRequest(mBinding.edtPhoneNew.getText().toString(), "805061", MyCdConfig.USERTYPE, UpdatePhoneActivity.this);
            }
        });

        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneNew.getText().toString())) {
                    showToast("请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCodeNew.getText().toString())) {
                    showToast("请输入验证码");
                    return;
                }

                updatePhone();

            }
        });

    }


    /**
     * 更换手机号
     */
    private void updatePhone() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("newMobile", mBinding.edtPhoneNew.getText().toString());
        map.put("smsCaptcha", mBinding.edtCodeNew.getText().toString());
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805061", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    showToast("修改成功");

                    EventBusModel eventBusModel=new EventBusModel();      //刷新上一页数据
                    eventBusModel.setTag(EventTags.CHANGEPHONENUMBER_REFRESH);
                    eventBusModel.setEvInfo(mBinding.edtPhoneNew.getText().toString());
                    EventBus.getDefault().post(eventBusModel);
                   finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    public void CodeSuccess(String msg) {
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSendNew));
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
        if(mSendCodePresenter!=null){
            mSendCodePresenter.clear();
            mSendCodePresenter=null;
        }
    }
}
