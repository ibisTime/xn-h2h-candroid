package com.cdkj.h2hwtw.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.adapters.WantProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.WantProductModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 浏览记录列表 （我的足迹）
 * Created by cdkj on 2017/10/21.
 */

public class BrowseProductListActivity extends BaseRefreshHelperActivity<WantProductModel> {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BrowseProductListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List<WantProductModel> listData) {
        return new WantProductListAdapter(listData);
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();
        map.put("category", "P");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("type", "3");
        map.put("userId", SPUtilHelpr.getUserId());

        Call<BaseResponseModel<ResponseInListModel<WantProductModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getWantList("808950", StringUtils.getJsonToString(map));

        if (isShowDialog) showLoadingDialog();
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<WantProductModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<WantProductModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.loadError(errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                mRefreshHelper.loadError(msg);
            }

            @Override
            protected void onFinish() {
                if (isShowDialog) disMissLoading();
            }
        });

    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我想要的");
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有足迹哦";
    }


}
