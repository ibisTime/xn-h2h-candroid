package com.cdkj.h2hwtw.module.product;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityScreeningBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.pop.PriceKeyboardPop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 产品筛选
 * Created by cdkj on 2017/10/16.
 */

public class ProductScreeningActivity extends BaseRefreshHelperActivity<ProductListModel.ListBean> {

    //类型
    private String mTypeCode;

    private ActivityScreeningBinding mBinding;

    /**
     * @param context
     * @param typeCode 类型编号
     */
    public static void open(Context context, String typeCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductScreeningActivity.class);
        intent.putExtra("typeCode", typeCode);
        context.startActivity(intent);
    }


    @Override
    protected void onInit(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mTypeCode = getIntent().getStringExtra("typeCode");
        }

        mRefreshHelper.onDefaluteMRefresh(true);

        mBinding.fraImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBinding.linScreeningMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PriceKeyboardPop(ProductScreeningActivity.this, null).showPopupWindow(view);
            }
        });

    }

    @Override
    public BaseQuickAdapter getAdapter(List<ProductListModel.ListBean> listData) {
        ProductListAdapter mProductAdapter = new ProductListAdapter(listData);
        return mProductAdapter;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_screening, null, false);
        return mBinding.getRoot();
    }


    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mBinding.rv;
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        Map<String, String> map = new HashMap();
        map.put("limit", limit + "");
        map.put("pageindex", pageindex + "");
        map.put("start", pageindex + "");
        map.put("location", "1");
        map.put("status", "3");
        map.put("category", mTypeCode);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808025", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

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
            protected void onFinish() {
                if (isShowDialog) disMissLoading();

            }
        });

    }


    @Override
    protected String getErrorInfo() {
        return getString(R.string.no_product);
    }
}
