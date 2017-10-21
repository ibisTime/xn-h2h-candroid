package com.cdkj.h2hwtw.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.EventTags;
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

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 收藏
 * Created by 李先俊 on 2017/10/21.
 */

public class WantProductListActivity extends BaseRefreshHelperActivity<WantProductModel> {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WantProductListActivity.class);
        context.startActivity(intent);
    }

    private boolean canRefresh = true;


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
        map.put("type", "1");
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canRefresh && mRefreshHelper != null) {
            canRefresh = false;
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    protected String getErrorInfo() {
        return "还没有想要的产品";
    }


    @Subscribe
    public void eventListener(String tag) {
        if (TextUtils.equals(EventTags.WANTCANCEL, tag)) {//用户取消收藏 返回界面时刷新数据
            canRefresh = true;
        }
    }

}
