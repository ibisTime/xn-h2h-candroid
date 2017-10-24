package com.cdkj.h2hwtw.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.adapters.BuyCircleProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.product.ProductDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 交易圈子
 * Created by cdkj on 2017/10/24.
 */

public class BuyCircleActivity extends BaseRefreshHelperActivity {

    private BuyCircleProductListAdapter mProductAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BuyCircleActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        mProductAdapter = new BuyCircleProductListAdapter(this, listData);
        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductListModel.ListBean pListBean = mProductAdapter.getItem(position);
                if (pListBean == null) return;
                ProductDetailActivity.open(BuyCircleActivity.this, pListBean.getCode());
            }
        });
        mProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ProductListModel.ListBean pListBean = mProductAdapter.getItem(position);
                if (pListBean == null) return;
                ProductDetailActivity.open(BuyCircleActivity.this, pListBean.getCode());
            }
        });

        return mProductAdapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
        Map map = RetrofitUtils.getRequestMap();
        map.put("status", "3");
        map.put("isJoin", "0");
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("isPublish", "1");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808025", StringUtils.getJsonToString(map));

        if (isShowDialog) showLoadingDialog();

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ProductListModel>(this) {
            @Override
            protected void onSuccess(ProductListModel data, String SucMessage) {
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
        mBaseBinding.titleView.setMidTitle("交易圈子");
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有交易产品哦";
    }
}
