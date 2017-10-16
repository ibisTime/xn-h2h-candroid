package com.cdkj.h2hwtw.module.user.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.FragmentMyBinding;
import com.cdkj.h2hwtw.model.AmountModel;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.module.user.account.MyAccountActivity;
import com.cdkj.h2hwtw.module.user.account.MyJfListActivity;
import com.cdkj.h2hwtw.module.user.info.SettingActivity;
import com.cdkj.h2hwtw.module.user.info.UserInfoEditActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的
 * Created by cdkj on 2017/10/9.
 */

public class MyFragment extends BaseLazyFragment {

    private FragmentMyBinding mBinding;

    private UserInfoModel mUserInfoMode;//用户信息


    private String mAmountaccountNumber;//账户


    /**
     * 获得fragment实例
     *
     * @return
     */
    public static MyFragment getInstanse() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.fragment_my, null, false);

        initListener();

        return mBinding.getRoot();
    }

    /**
     * 初始化事件监听
     */
    private void initListener() {

        mBinding.linUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserInfoMode == null) return;
                UserInfoEditActivity.open(mActivity, mUserInfoMode);
            }
        });

        mBinding.linMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAccountActivity.open(mActivity);
            }
        });

        mBinding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.open(mActivity, mUserInfoMode);
            }
        });


        mBinding.linJfList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyJfListActivity.open(mActivity, mAmountaccountNumber);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            getUserInfoRequest(false);
            getAmountRequest(false);
            getJFAmountRequest(false);
        }
    }

    @Override
    protected void lazyLoad() {
        getAmountRequest(false);
        getJFAmountRequest(false);
        getUserInfoRequest(true);
    }

    @Override
    protected void onInvisible() {

    }


    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(mActivity) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                mUserInfoMode = data;
                SPUtilHelpr.saveisTradepwdFlag(data.isTradepwdFlag());
                SPUtilHelpr.saveUserPhoneNum(data.getMobile());
                setShowData(mUserInfoMode);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


    /**
     * 获取余额请求
     */
    private void getAmountRequest(final boolean isShowdialog) {
        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("currency", MyCdConfig.MONEYTYPE);
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAmount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AmountModel>(mActivity) {
            @Override
            protected void onSuccess(List<AmountModel> data, String SucMessage) {

                if (data != null && data.size() > 0 && data.get(0) != null) {
                    mBinding.tvAmount.setText(MoneyUtils.showPrice(data.get(0).getAmount()));
                    mAmountaccountNumber = data.get(0).getAccountNumber();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });

    }

    /**
     * 获取积分请求
     */
    private void getJFAmountRequest(final boolean isShowdialog) {
        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("currency", "JF");
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAmount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AmountModel>(mActivity) {
            @Override
            protected void onSuccess(List<AmountModel> data, String SucMessage) {

                if (data != null && data.size() > 0 && data.get(0) != null) {
                    mBinding.tvAmountJf.setText(MoneyUtils.showPrice(data.get(0).getAmount()));
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });

    }

    /**
     * 设置数据显示
     *
     * @param showData
     */
    public void setShowData(UserInfoModel showData) {
        if (showData == null) return;

        ImgUtils.loadLogo(this, MyCdConfig.QINIUURL + showData.getPhoto(), mBinding.imgUserLogo);

        mBinding.tvFansNum.setText(showData.getTotalFansNum() + "");
        mBinding.tvFollowSum.setText(showData.getTotalFollowNum() + "");

    }
}
