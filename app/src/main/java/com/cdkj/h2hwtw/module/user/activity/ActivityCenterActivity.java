package com.cdkj.h2hwtw.module.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.adapters.ActivityCenterAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.ActivityCenterModel;
import com.cdkj.h2hwtw.module.product.ProductReleaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 活动中心
 * Created by cdkj on 2017/10/21.
 */

public class ActivityCenterActivity extends BaseRefreshHelperActivity<ActivityCenterModel> {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActivityCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public BaseQuickAdapter getAdapter(List<ActivityCenterModel> listData) {
        final ActivityCenterAdapter mAdapter = new ActivityCenterAdapter(listData);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityCenterDetailsActivity.open(ActivityCenterActivity.this, mAdapter.getItem(position));
            }
        });
        return mAdapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
        Map<String, String> map = new HashMap();

        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("status", "1");
//        map.put("orderColumn", "start_datetime");
//        map.put("orderDir", "desc");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getActivityCenterList("801070", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<ActivityCenterModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<ActivityCenterModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ActivityCenterActivity.this, errorMessage);
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
        mBaseBinding.titleView.setMidTitle("活动中心");
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有优惠活动哦";
    }
}
