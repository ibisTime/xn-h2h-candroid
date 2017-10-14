package com.cdkj.h2hwtw.module.user.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.FragmentMyBinding;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.module.user.account.MyAccountActivity;
import com.cdkj.h2hwtw.module.user.info.SettingActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的
 * Created by cdkj on 2017/10/9.
 */

public class MyFragment extends BaseLazyFragment {

    private FragmentMyBinding mBinding;

    private UserInfoModel mUserInfoMode;//用户信息

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

        mBinding.imgUserLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.open(mActivity, mUserInfoMode);
            }
        });

        mBinding.layoutMyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAccountActivity.open(mActivity);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            getUserInfoRequest(false);
        }
    }

    @Override
    protected void lazyLoad() {
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
//                setShowData(mUserInfoMode);
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

}
