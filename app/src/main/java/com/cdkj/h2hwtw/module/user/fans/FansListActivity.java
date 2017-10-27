package com.cdkj.h2hwtw.module.user.fans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.activitys.CommonTablayoutActivity;
import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.module.user.coupons.CouponsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 粉丝关注列表
 * Created by cdkj on 2017/10/19.
 */

public class FansListActivity extends CommonTablayoutActivity {

    public boolean showFans = false;

    public static void open(Context context, boolean showFans) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FansListActivity.class);
        intent.putExtra("showFans", showFans);
        context.startActivity(intent);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        mBaseBinding.titleView.setMidTitle("关注和粉丝");

        if (getIntent() != null) {
            showFans = getIntent().getBooleanExtra("showFans", false);
        }

        if (showFans) {
            mTabLayoutBinding.viewpager.setCurrentItem(1);
        } else {
            mTabLayoutBinding.viewpager.setCurrentItem(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfoRequest(true);
    }

    @Override
    public List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FansListFragment.getInstanse(false));
        fragments.add(FansListFragment.getInstanse(true));

        return fragments;
    }

    @Override
    public List<String> getFragmentTitles() {
        List<String> strings = new ArrayList<>();
        strings.add("我的关注(0)");
        strings.add("我的粉丝(0)");
        return strings;
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

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(FansListActivity.this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                mTabLayoutBinding.tablayout.getTabAt(0).setText("我的关注(" + data.getTotalFollowNum() + ")");
                mTabLayoutBinding.tablayout.getTabAt(1).setText("我的粉丝(" + data.getTotalFansNum() + ")");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(FansListActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


}
