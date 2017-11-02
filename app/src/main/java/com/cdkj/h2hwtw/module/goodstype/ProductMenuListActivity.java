package com.cdkj.h2hwtw.module.goodstype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.adapters.ProductTypeListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.module.product.ProductScreeningActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2017/11/2.
 */

public class ProductMenuListActivity extends BaseRefreshHelperActivity {

    private ProductTypeListAdapter adapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductMenuListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        adapter = new ProductTypeListAdapter(listData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductTypeModel productTypeModel = (ProductTypeModel) adapter.getItem(position);
                if (productTypeModel == null) return;
                ProductScreeningActivity.open(ProductMenuListActivity.this, productTypeModel.getCode(), productTypeModel.getName());
            }
        });
        return adapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap();

        map.put("status", "1");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("parentCode", "0");
        map.put("type", "4");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductType("808007", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<ProductTypeModel>(this) {
            @Override
            protected void onSuccess(List<ProductTypeModel> data, String SucMessage) {
                mRefreshHelper.setData(data);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.setErrorInfo(errorMessage);
                UITipDialog.showFall(ProductMenuListActivity.this, errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                mRefreshHelper.setErrorInfo(msg);
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("商品分类");

        mRefreshBinding.refreshLayout.setEnableRefresh(false);
        mRefreshBinding.refreshLayout.setEnableLoadmore(false);

        mRefreshBinding.rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRefreshHelper.onDefaluteMRefresh(false);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有菜单分类";
    }
}
