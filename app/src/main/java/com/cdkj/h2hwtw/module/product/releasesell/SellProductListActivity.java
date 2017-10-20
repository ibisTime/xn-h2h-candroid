package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.SellProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 出售列表 我卖出的
 * Created by cdkj on 2017/10/20.
 */

public class SellProductListActivity extends BaseRefreshHelperActivity {


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SellProductListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        SellProductListAdapter seAdapter = new SellProductListAdapter(listData);
        seAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        return seAdapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        /*{"systemCode":"CD-WTW000016"
        ,"companyCode":"CD-WTW000016",
        "token":"TSYS_USER_WTWTK201710130935341077766"
        ,"start":1,"limit":10,"toUser":"U201710131355098586852",
        "orderColumn":"apply_datetime","orderDir":"desc"}*/
        Map map = new HashMap<>();
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("orderColumn", "apply_datetime");
        map.put("orderDir", "desc");
        map.put("toUser", SPUtilHelpr.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<OrderModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getOrderList("808065", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<OrderModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<OrderModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.loadError(errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowDialog) disMissLoading();
            }
        });


    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我卖出的");
        mRefreshHelper.setErrorImg(R.drawable.no_order);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有卖出宝贝哦";
    }
}
