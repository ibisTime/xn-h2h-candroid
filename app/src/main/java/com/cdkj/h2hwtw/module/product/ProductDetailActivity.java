package com.cdkj.h2hwtw.module.product;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.ScrollGridLayoutManager;
import com.cdkj.h2hwtw.PhotoViewPagerActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductCommentsListAdapter;
import com.cdkj.h2hwtw.adapters.ProductImgAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityProductDetailBinding;
import com.cdkj.h2hwtw.model.CommentsModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.module.order.ProductBuyActivity;
import com.cdkj.h2hwtw.module.user.PersonalPageActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
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
//TODO 详情界面适配 产品列表 适配
public class ProductDetailActivity extends AbsBaseLoadActivity {

    private ActivityProductDetailBinding mBinding;
    private String mProductCode;//产品编号
    private ProductListModel.ListBean mProductData;//产品数据

    private ProductImgAdapter mImgAdapter;

    private RefreshHelper mCommentsReshHelper;
    private ProductCommentsListAdapter mpadCommentsListAdapter;

    ArrayList<String> mImgUrlList;


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
        mImgUrlList = new ArrayList<>();
        if (getIntent() != null) {
            mProductCode = getIntent().getStringExtra("productCode");
        }

        initComments();

        initImgAdapter();

        initListener();

        getAllData();
    }

    /**
     * 获取所有数据
     */
    private void getAllData() {
        getProductDetail(mProductCode);
        getCommentSum(mProductCode);
        isShowAndGetRequest("3", mProductCode);//请求浏览数据
        getShowSumRequest(mProductCode);//请求浏览总数
        if (mCommentsReshHelper != null) {
            mCommentsReshHelper.onDefaluteMRefresh(false);//请求评论数据
        }

    }

    @Override
    public void topTitleViewRightClick() {
        if (!SPUtilHelpr.isLogin(this, false)) {
            return;
        }

        if (mProductData == null) return;


        if (TextUtils.equals("1", mProductData.getIsCollect())) {
            cancelGetRequest("1", mProductCode);
        } else {
            isShowAndGetRequest("1", mProductCode);//收藏
        }


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

        mBinding.butLayout.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SPUtilHelpr.isLogin(ProductDetailActivity.this, false)) {
                    return;
                }
                ProductBuyActivity.open(ProductDetailActivity.this, mProductData);
            }
        });

        mBinding.userLayout.linUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProductData == null) return;

                PersonalPageActivity.open(ProductDetailActivity.this, mProductData.getUpdater());

            }
        });
    }

    /**
     * 评论设置
     */
    private void initComments() {

        mCommentsReshHelper = new RefreshHelper(this, new BaseRefreshCallBack() {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mBinding.commentsLayout.recyclerComments.setLayoutManager(linearLayoutManager);
        mBinding.commentsLayout.recyclerComments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    /**
     * 展示图片适配器
     */
    private void initImgAdapter() {
        mImgAdapter = new ProductImgAdapter(new ArrayList<String>(), this);

        mImgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mImgUrlList == null || mImgUrlList.size() == 0) {
                    return;
                }
                PhotoViewPagerActivity.open(ProductDetailActivity.this, mImgUrlList, position);
            }
        });

        mBinding.recyclerImg.setAdapter(mImgAdapter);
        //TODO FlexboxLayoutManager也可以实现
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
                getUserInfoRequest(false, mProductData.getUpdater());
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
     * 浏览收藏接口
     *
     * @param type 类型（1、收藏，2、点赞，3、浏览）
     */
    private void isShowAndGetRequest(final String type, String entityCode) {

        if (!SPUtilHelpr.isLoginNoStart() || TextUtils.isEmpty(entityCode)) {
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("type", type);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("category", "P");
        map.put("entityCode", entityCode);
        map.put("interacter", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().codeRequest("801030", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                if (TextUtils.equals(type, "1") && !TextUtils.isEmpty(data.getCode())) {
                    mProductData.setIsCollect("1");
                    mBaseBinding.titleView.setRightImg(R.drawable.want_red);
                }
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
     * 取消收藏
     *
     * @param type 类型（1、收藏，2、点赞，3、浏览）
     */
    private void cancelGetRequest(final String type, String entityCode) {

        if (!SPUtilHelpr.isLoginNoStart() || TextUtils.isEmpty(entityCode)) {
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("type", type);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("category", "P");
        map.put("entityCode", entityCode);
        map.put("interacter", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("801031", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    mProductData.setIsCollect("0");
                    mBaseBinding.titleView.setRightImg(R.drawable.want_un);
                    EventBus.getDefault().post(EventTags.WANTCANCEL);//取消收藏
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ProductDetailActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取浏览数量
     *
     * @param
     */
    private void getShowSumRequest(String entityCode) {

        if (TextUtils.isEmpty(entityCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("type", "3");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("category", "P");
        map.put("entityCode", entityCode);

        Call call = RetrofitUtils.getBaseAPiService().intRequest("801037", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<Integer>(this) {
            @Override
            protected void onSuccess(Integer data, String SucMessage) {
                mBinding.tvShowSum.setText("浏览" + data);
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
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        if (SPUtilHelpr.isLoginNoStart()) {
            map.put("token", SPUtilHelpr.getUserToken());
        }

        Call<BaseResponseModel<ResponseInListModel<CommentsModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getCommentList("801025", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CommentsModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<CommentsModel> data, String SucMessage) {

                mCommentsReshHelper.setData(data.getList());

                if (mpadCommentsListAdapter.getData().isEmpty()) {
                    mBinding.commentsLayout.linCommentsEmpty.setVisibility(View.VISIBLE);
                    mBinding.commentsLayout.recyclerComments.setVisibility(View.GONE);
                    mBinding.commentsLayout.linWantComments.setVisibility(View.GONE);
                    mBinding.refreshLayout.setEnableLoadmore(false);
                } else {
                    mBinding.commentsLayout.recyclerComments.setVisibility(View.VISIBLE);
                    mBinding.commentsLayout.linWantComments.setVisibility(View.VISIBLE);
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

        if (TextUtils.equals(showData.getUpdater(), SPUtilHelpr.getUserId())) {  //是自己查看就隐藏购买按钮
            mBinding.butLayout.linBuy.setVisibility(View.GONE);
        } else {
            mBinding.butLayout.linBuy.setVisibility(View.VISIBLE);
        }

        mImgUrlList.clear();
        mImgUrlList = new ArrayList<>();
        mImgUrlList.addAll(StringUtils.splitAsPicList(showData.getPic()));
        mImgAdapter.replaceData(mImgUrlList);


        if (TextUtils.equals("1", showData.getIsCollect())) {
            mBaseBinding.titleView.setRightImg(R.drawable.want_red);
        } else {
            mBaseBinding.titleView.setRightImg(R.drawable.want_un);
        }

        mBinding.tvProductName.setText(showData.getName());
        mBinding.tvPriceTop.setText(MoneyUtils.showPrice(showData.getPrice()));
        mBinding.butLayout.tvPriceButtom.setText(MoneyUtils.getShowPriceSign(showData.getPrice()));
        mBinding.tvLocation.setText(showData.getCity() + "|" + showData.getArea());
        mBinding.expandabletext.expandTextView.setText(showData.getDescription());
//        原价格100000  运费100
        mBinding.tvOtherInfo.setText("原价格" + getString(R.string.money_sing) + MoneyUtils.showPrice(showData.getOriginalPrice()) + "   运费" + MoneyUtils.showPrice(showData.getYunfei()));

    }


    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog, String userId) {


        Map<String, String> map = new HashMap<>();

        map.put("userId", userId);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                ImgUtils.loadLogo(ProductDetailActivity.this, MyCdConfig.QINIUURL + data.getPhoto(), mBinding.userLayout.imgUserLogo);
                mBinding.userLayout.tvUserName.setText(data.getNickname());
                /*        mBinding.tvFansNum.setText(showData.getTotalFansNum() + "");
        mBinding.tvFollowSum.setText(showData.getTotalFollowNum() + "");*/

                StringBuffer sb = new StringBuffer();
                sb.append(TextUtils.equals(data.getGender(), MyCdConfig.GENDERMAN) ? "男" : "女");
                sb.append(" ");
                sb.append("关注" + data.getTotalFollowNum());
                sb.append(" ");
                sb.append("粉丝" + data.getTotalFansNum());
                mBinding.userLayout.tvInfo.setText(sb.toString());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


    /**
     * 评论刷新
     */
    @Subscribe
    public void EventRefresh(String tag) {
        if (TextUtils.equals(EventTags.RELEASESCOMMENTS, tag)) {//发表留言成功
            if (mCommentsReshHelper != null) {
                mCommentsReshHelper.onDefaluteMRefresh(false);
                getCommentSum(mProductCode);
            }

            return;
        }

        if (TextUtils.equals(EventTags.LOGINREFRESH, tag)) {//登录成功刷新
            getAllData();
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
