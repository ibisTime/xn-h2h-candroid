package com.cdkj.h2hwtw.module.user.info;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.BackCardListActivity;
import com.cdkj.baselibrary.activitys.FindPwdActivity;
import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.activitys.UpdatePhoneActivity;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.QiNiuUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivitySettingBinding;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.module.user.login.LoginActivity;
import com.qiniu.android.http.ResponseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 个人中心
 */
public class SettingActivity extends AbsBaseLoadActivity {

    private ActivitySettingBinding mBinding;
    private UserInfoModel mUserInfoData;
    public final int PHOTOFLAG = 110;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, UserInfoModel data) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_setting, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mUserInfoData = getIntent().getParcelableExtra("data");
        }


        setShowData();

        mBaseBinding.titleView.setMidTitle("个人中心");

        initListener();
    }

    private void setShowData() {
        if (mUserInfoData == null) {
            return;
        }

        ImgUtils.loadLogo(SettingActivity.this, MyCdConfig.QINIUURL + mUserInfoData.getPhoto(), mBinding.imgPhoto);

        mBinding.txtPhone.setText(mUserInfoData.getMobile());

        if (mUserInfoData.isTradepwdFlag()) {  //设置支付密码
            mBinding.tvPayPassword.setText("修改支付密码");
        } else {
            mBinding.tvPayPassword.setText("设置支付密码");
        }


    }

    /**
     * 事件
     */
    private void initListener() {

        mBinding.layoutPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePhoneActivity.open(SettingActivity.this);
            }
        });

        //修改登录密码
        mBinding.layoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "";
                if (mUserInfoData != null) {
                    phone = mUserInfoData.getMobile();
                }
                FindPwdActivity.open(SettingActivity.this, phone);
            }
        });

        //修改支付密码
        mBinding.linPayPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //支付密码
                if (mUserInfoData == null) {
                    return;
                }
                PayPwdModifyActivity.open(SettingActivity.this, mUserInfoData.isTradepwdFlag(), mUserInfoData.getMobile());
            }
        });

        //头像
        mBinding.layoutPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelectActivity.launch(SettingActivity.this, PHOTOFLAG);
            }
        });
        //退出登录
        mBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDoubleWarnListen("确认退出登录？", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        logOut();
                    }
                });
            }
        });


        mBinding.layoutBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackCardListActivity.open(SettingActivity.this, false);
            }
        });

        mBinding.linPayAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressListActivity.open(SettingActivity.this);
            }
        });


    }

    private void logOut() {
        SPUtilHelpr.logOutClear();
        EventBus.getDefault().post(EventTags.MAINFINISH);
        LoginActivity.open(this, true);
        EventBus.getDefault().post(EventTags.AllFINISH);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PHOTOFLAG) {
            String path = data.getStringExtra(ImageSelectActivity.staticPath);
            LogUtil.E("拍照获取路径" + path);
            new QiNiuUtil(SettingActivity.this).getQiniuURL(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    updateUserPhoto(key);
                }

                @Override
                public void onFal(String info) {
                    showToast(info);
                }
            }, path);

        }
    }

    /**
     * 更新头像
     *
     * @param key
     */
    private void updateUserPhoto(final String key) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("photo", key);
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getJsonToString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(SettingActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast("头像上传成功");
                    ImgUtils.loadLogo(SettingActivity.this, MyCdConfig.QINIUURL + key, mBinding.imgPhoto);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(SettingActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Subscribe
    public void updateDataEvent(EventBusModel e) {
        if (e == null) {
            return;
        }
        if (TextUtils.equals(e.getTag(), EventTags.CHANGEPHONENUMBER_REFRESH)) {  //修改电话成功刷新界面
            mUserInfoData.setMobile(e.getEvInfo());
            setShowData();
        } else if (TextUtils.equals(e.getTag(), EventTags.CHANGE_PAY_PWD_REFRESH)) {  //修改支付密码成功刷新界面
            mUserInfoData.setTradepwdFlag(true);
            setShowData();
        }
    }
}
