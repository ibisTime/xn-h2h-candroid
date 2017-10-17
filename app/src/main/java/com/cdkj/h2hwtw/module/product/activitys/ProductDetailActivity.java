package com.cdkj.h2hwtw.module.product.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshmethods;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.ScrollGridLayoutManager;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductCommentsListAdapter;
import com.cdkj.h2hwtw.adapters.ProductImgAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityProductDetailBinding;
import com.cdkj.h2hwtw.model.CommentsModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 产品详情
 * Created by cdkj on 2017/10/17.
 */

public class ProductDetailActivity extends AbsBaseLoadActivity {

    private ActivityProductDetailBinding mBinding;
    private String mProductCode;//产品编号
    private ProductListModel.ListBean mProductData;//产品数据

    private ProductImgAdapter mImgAdapter;

    private RefreshHelper mCommentsReshHelper;
    private ProductCommentsListAdapter mpadCommentsListAdapter;


    public static void open(Context context, String productCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("productCode", productCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_product_detail, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.contentView.hindAll();
        mBaseBinding.titleView.setMidTitle("产品详情");
        if (getIntent() != null) {
            mProductCode = getIntent().getStringExtra("productCode");
        }

        initComments();

        initImgAdapter();

        getProductDetail(mProductCode);

        initListener();

    }

    private void initListener() {

        /**
         * 我要留言 没有留言时
         */
        mBinding.commentsLayout.btnWantCommentsEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SPUtilHelpr.isLogin(ProductDetailActivity.this, false)) {
                    return;
                }
                ProductCommentsEditActivity.open(ProductDetailActivity.this, mProductCode);
            }
        });
        /**
         * 我要留言 有留言时
         */
        mBinding.commentsLayout.btnWantComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SPUtilHelpr.isLogin(ProductDetailActivity.this, false)) {
                    return;
                }
                ProductCommentsEditActivity.open(ProductDetailActivity.this, mProductCode);
            }
        });

    }

    /**
     * 评论设置
     */
    private void initComments() {

        mCommentsReshHelper = new RefreshHelper(this, new BaseRefreshmethods() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                mBinding.refreshLayout.setEnableRefresh(false);//禁用刷新
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.commentsLayout.recyclerComments;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                mpadCommentsListAdapter = new ProductCommentsListAdapter(listData);
                return mpadCommentsListAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getCommentListRequest(pageindex, limit);
            }
        });

        mCommentsReshHelper.init(1, 10);

    }

    /**
     * 展示图片适配器
     */
    private void initImgAdapter() {
        mImgAdapter = new ProductImgAdapter(new ArrayList<String>(), this);
        mBinding.recyclerImg.setAdapter(mImgAdapter);

        ScrollGridLayoutManager layoutManager = new ScrollGridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mImgAdapter.isHalfWidth(position) ? 1 : 2;
            }
        });

        mBinding.recyclerImg.setLayoutManager(layoutManager);

    }

    /**
     * 获取产品详情
     */
    private void getProductDetail(String code) {
        if (TextUtils.isEmpty(code)) {
            mBaseBinding.contentView.showContent(true);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("code", code);

        if (SPUtilHelpr.isLoginNoStart()) {
            map.put("userId", SPUtilHelpr.getUserId());
        }

        showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductDetails("808026", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ProductListModel.ListBean>(this) {
            @Override
            protected void onSuccess(ProductListModel.ListBean data, String SucMessage) {
                mProductData = data;
                setShowData(mProductData);
                mCommentsReshHelper.onDefaluteMRefresh(false);//请求评论数据
                getCommentSum(mProductCode);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mBaseBinding.contentView.setShowText(errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                mBaseBinding.contentView.setShowText(msg);
            }

            @Override
            protected void onFinish() {
                mBaseBinding.contentView.showContent(true);
                disMissLoading();
            }
        });

    }

    /**
     * 获取取评论总数
     */
    private void getCommentSum(String code) {
        if (TextUtils.isEmpty(code)) {
            mBaseBinding.contentView.showContent(true);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("entityCode", code);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().intRequest("801027", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<Integer>(this) {
            @Override
            protected void onSuccess(Integer data, String SucMessage) {
                mBinding.commentsLayout.tvCommentsSum.setText("互动(" + data + ")");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
            }

            @Override
            protected void onNoNet(String msg) {
            }

            @Override
            protected void onFinish() {

            }
        });

    }

    /**
     * 获取评论列表
     *
     * @param pageIndex
     * @param limit
     */
    public void getCommentListRequest(int pageIndex, int limit) {

        if (TextUtils.isEmpty(mProductCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("limit", limit + "");
        map.put("start", pageIndex + "");
        map.put("status", "AB");
        map.put("orderDir", "desc");
        map.put("orderColumn", "comment_datetime");
        map.put("entityCode", mProductCode);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call<BaseResponseModel<ResponseInListModel<CommentsModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getCommentList("801025", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CommentsModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<CommentsModel> data, String SucMessage) {

                mCommentsReshHelper.setData(data.getList());

                if (mpadCommentsListAdapter.getData().isEmpty()) {
                    mBinding.commentsLayout.linCommentsEmpty.setVisibility(View.VISIBLE);
                    mBinding.commentsLayout.recyclerComments.setVisibility(View.GONE);
                    mBinding.refreshLayout.setEnableLoadmore(false);
                } else {
                    mBinding.commentsLayout.recyclerComments.setVisibility(View.VISIBLE);
                    mBinding.commentsLayout.btnWantComments.setVisibility(View.VISIBLE);
                    mBinding.commentsLayout.linCommentsEmpty.setVisibility(View.GONE);
                    mBinding.refreshLayout.setEnableLoadmore(true);
                }

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });

    }


    /**
     * 设置数据
     *
     * @param showData
     */

    public void setShowData(ProductListModel.ListBean showData) {
        if (showData == null) return;

        List<String> dd = new ArrayList<>();
        dd.addAll(StringUtils.splitAsPicList(showData.getPic()));
//        dd.add("68b53042-304a-4166-af67-821aa2be04cc.JPG");
        mImgAdapter.replaceData(dd);

        mBinding.tvProductName.setText(showData.getName());
        mBinding.tvPriceTop.setText(MoneyUtils.showPrice(showData.getPrice()));
        mBinding.butLayout.tvPriceButtom.setText(MoneyUtils.showPrice(showData.getPrice()));
        mBinding.tvLocation.setText(showData.getCity() + "|" + showData.getArea());
        mBinding.expandabletext.expandTextView.setText(showData.getDescription());
//        原价格￥100000  运费100
        mBinding.tvOtherInfo.setText("原价格￥" + MoneyUtils.showPrice(showData.getOriginalPrice()) + " 运费" + MoneyUtils.showPrice(showData.getYunfei()));

    }

    /**
     * 评论刷新
     */
    @Subscribe
    public void EventRefresh(String tag) {
        if (TextUtils.equals(EventTags.RELEASESCOMMENTS, tag)) {
            if (mCommentsReshHelper != null) {
                mCommentsReshHelper.onDefaluteMRefresh(false);
                getCommentSum(mProductCode);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCommentsReshHelper != null) {
            mCommentsReshHelper.clear();
        }
    }
}
