package com.cdkj.h2hwtw.module.firstpage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.FragmentFirstPageScollorBinding;
import com.cdkj.h2hwtw.model.BannerModel;
import com.cdkj.h2hwtw.model.MsgListModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.BuyCircleActivity;
import com.cdkj.h2hwtw.module.product.preferential.PreferentialProductListActivity;
import com.cdkj.h2hwtw.module.user.activity.ActivityPutMoneyDetailsActivity;
import com.cdkj.h2hwtw.module.user.activity.PutMoneySendListActivity;
import com.cdkj.h2hwtw.module.user.info.ActivityInvitationFriend;
import com.cdkj.h2hwtw.other.GlideImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 李先俊
 * Created by cdkj on 2017/10/9.
 */
//TODO 附近商品定位参数获取
public class FirstPageFragment extends BaseLazyFragment {

    private FragmentFirstPageScollorBinding mBinding;

    private RefreshHelper mHotRefreshHelper;//热门推荐产品
    private RefreshHelper mAddressProductRefreshHelper;//定位地址产品
    private List<String> mbannerUrlList;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static FirstPageFragment getInstanse() {
        FirstPageFragment fragment = new FirstPageFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_page_scollor, null, false);
        mbannerUrlList = new ArrayList<String>();

        initProductTab();
        initRefreshListtener();
        initAddressProductRefresh();
        initHotRefresh();
        initListtener();
        initBanner();
        getBannerRequest();
        getMsgRequest();
        return mBinding.getRoot();
    }


    private void initBanner() {

        mBinding.topLayout.bannerFirstPage.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBinding.topLayout.bannerFirstPage.setIndicatorGravity(BannerConfig.CENTER);

        mBinding.topLayout.bannerFirstPage.setImageLoader(new GlideImageLoader());

        mBinding.topLayout.bannerFirstPage.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mbannerUrlList.size() <= position) {
                    return;
                }

                if (TextUtils.isEmpty(mbannerUrlList.get(position))) {
                    return;
                }
                WebViewActivity.openURL(mActivity, "详情", mbannerUrlList.get(position));
            }
        });
        mBinding.topLayout.bannerFirstPage.start();
    }

    private void initListtener() {

        //优惠活动
        mBinding.topLayout.linPreferential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferentialProductListActivity.open(mActivity);
            }
        });
        //充值
        mBinding.topLayout.linPreferentialPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityPutMoneyDetailsActivity.open(mActivity);
            }
        });
        //邀请
        mBinding.topLayout.linPreferentialFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityInvitationFriend.open(mActivity);
            }
        });
        //消息中心
        mBinding.topLayout.linMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgListActivity.open(mActivity);
            }
        });
        //交易圈子
        mBinding.topLayout.imgBuyCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCircleActivity.open(mActivity);
            }
        });

    }

    /**
     * 刷新监听
     */
    private void initRefreshListtener() {
        mBinding.refreshLayout.setEnableAutoLoadmore(false);//禁用惯性

        mBinding.refreshLayout.setEnableLoadmoreWhenContentNotFull(true);

        mBinding.refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mHotRefreshHelper.onDefaluteMRefresh(false);
                mAddressProductRefreshHelper.onDefaluteMRefresh(false);
                getBannerRequest();
                getMsgRequest();
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (mBinding.viewHot.getVisibility() == View.VISIBLE) {
                    mHotRefreshHelper.onDefaluteMLoadMore(false);
                } else {
                    mAddressProductRefreshHelper.onDefaluteMLoadMore(false);
                }
            }
        });
    }


    private void initAddressProductRefresh() {
        mAddressProductRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return null;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerAddressProduct;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                ProductListAdapter mProductAdapter = new ProductListAdapter(listData);
                return mProductAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
                Map<String, String> map = new HashMap();
                map.put("limit", limit + "");
                map.put("pageindex", pageindex + "");
                map.put("start", pageindex + "");
//                map.put("location", "1"); //1热门
                map.put("status", "3");
                map.put("isJoin", "0");
                map.put("companyCode", MyCdConfig.COMPANYCODE);
                map.put("systemCode", MyCdConfig.SYSTEMCODE);
                Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808025", StringUtils.getJsonToString(map));

                addCall(call);

                if (isShowDialog) showLoadingDialog();

                call.enqueue(new BaseResponseModelCallBack<ProductListModel>(mActivity) {
                    @Override
                    protected void onSuccess(ProductListModel data, String SucMessage) {
                        if (mBinding.refreshLayout.isRefreshing())
                            mBinding.refreshLayout.finishRefresh();
                        if (mBinding.refreshLayout.isLoading())
                            mBinding.refreshLayout.finishLoadmore();
                        mAddressProductRefreshHelper.setData(data.getList());
                    }

                    @Override
                    protected void onReqFailure(String errorCode, String errorMessage) {
                        mAddressProductRefreshHelper.loadError(errorMessage);
                    }

                    @Override
                    protected void onNoNet(String msg) {
                        mHotRefreshHelper.loadError(msg);
                    }

                    @Override
                    protected void onFinish() {
                        if (isShowDialog) disMissLoading();

                    }
                });

            }
        });
        mAddressProductRefreshHelper.setErrorInfo("暂无产品");
        mAddressProductRefreshHelper.init(1, 10);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mBinding.recyclerAddressProduct.setLayoutManager(linearLayoutManager);

        mAddressProductRefreshHelper.onDefaluteMRefresh(false);

    }

    private void initHotRefresh() {

        mHotRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return null;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerHotProduct;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                ProductListAdapter mProductAdapter = new ProductListAdapter(listData);
                return mProductAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
                Map<String, String> map = new HashMap();
                map.put("limit", limit + "");
                map.put("pageindex", pageindex + "");
                map.put("start", pageindex + "");
//                map.put("location", "1"); //1热门
                map.put("status", "3");
                map.put("isJoin", "0");
                map.put("companyCode", MyCdConfig.COMPANYCODE);
                map.put("systemCode", MyCdConfig.SYSTEMCODE);
                Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808025", StringUtils.getJsonToString(map));

                addCall(call);

                if (isShowDialog) showLoadingDialog();

                call.enqueue(new BaseResponseModelCallBack<ProductListModel>(mActivity) {
                    @Override
                    protected void onSuccess(ProductListModel data, String SucMessage) {
                        if (mBinding.refreshLayout.isRefreshing())
                            mBinding.refreshLayout.finishRefresh();
                        if (mBinding.refreshLayout.isLoading())
                            mBinding.refreshLayout.finishLoadmore();
                        mHotRefreshHelper.setData(data.getList());
                    }

                    @Override
                    protected void onReqFailure(String errorCode, String errorMessage) {
                        mHotRefreshHelper.loadError(errorMessage);
                    }

                    @Override
                    protected void onNoNet(String msg) {
                        mHotRefreshHelper.loadError(msg);
                    }

                    @Override
                    protected void onFinish() {
                        if (isShowDialog) disMissLoading();

                    }
                });

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mHotRefreshHelper.setErrorInfo("暂无产品");
        mHotRefreshHelper.init(1, 10);
        mBinding.recyclerHotProduct.setLayoutManager(linearLayoutManager);
        mHotRefreshHelper.onDefaluteMRefresh(false);

    }

    /**
     * 底部产品类型切换
     */
    private void initProductTab() {

        mBinding.lineHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBinding.viewHot.getVisibility() == View.VISIBLE) {
                    return;
                }

                mBinding.tvHot.setTextColor(ContextCompat.getColor(mActivity, R.color.text_select_app));
                mBinding.tvAddress.setTextColor(ContextCompat.getColor(mActivity, R.color.text_black_app));
                mBinding.viewHot.setVisibility(View.VISIBLE);
                mBinding.viewAddress.setVisibility(View.INVISIBLE);
                mBinding.recyclerHotProduct.setVisibility(View.VISIBLE);
                mBinding.recyclerAddressProduct.setVisibility(View.GONE);
            }
        });

        mBinding.linAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBinding.viewAddress.getVisibility() == View.VISIBLE) {
                    return;
                }
                mBinding.tvAddress.setTextColor(ContextCompat.getColor(mActivity, R.color.text_select_app));
                mBinding.tvHot.setTextColor(ContextCompat.getColor(mActivity, R.color.text_black_app));
                mBinding.viewAddress.setVisibility(View.VISIBLE);
                mBinding.viewHot.setVisibility(View.INVISIBLE);
                mBinding.recyclerAddressProduct.setVisibility(View.VISIBLE);
                mBinding.recyclerHotProduct.setVisibility(View.GONE);
            }
        });

    }


    /**
     * 获取广告
     */
    public void getBannerRequest() {/*{"systemCode":"CD-WTW000016","companyCode":"CD-WTW000016","token":"undefined","type":2,"belong":0}*/
        Map<String, String> map = new HashMap<String, String>();
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("type", "2");
        map.put("belong", "0");
        Call call = RetrofitUtils.createApi(MyApiServer.class).getBannerList("805806", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {
            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                List<String> bannerList = new ArrayList<String>();
                mbannerUrlList.clear();
                for (BannerModel bannerModel : data) {
                    if (bannerModel == null) continue;
                    bannerList.add(bannerModel.getPic());
                    mbannerUrlList.add(bannerModel.getUrl());
                }
                mBinding.topLayout.bannerFirstPage.setImages(bannerList);
                mBinding.topLayout.bannerFirstPage.start();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });

    }

    public void getMsgRequest() {


        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", "1");
        map.put("limit", "1");
        map.put("pushType", "41");
        map.put("toKind", "C");
        map.put("status", "1");
        map.put("fromSystemCode", MyCdConfig.SYSTEMCODE);
        map.put("toSystemCode", MyCdConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMsgList("804040", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(mActivity) {
            @Override
            protected void onSuccess(MsgListModel data, String SucMessage) {
                if (data.getList() == null || data.getList().size() < 1) {
                    return;
                }

                mBinding.topLayout.tvMsg.setText(data.getList().get(0).getSmsTitle());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }


            @Override
            protected void onFinish() {
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (mBinding != null) {
            mBinding.topLayout.bannerFirstPage.start();
        }

    }

    @Override
    protected void onInvisible() {
        if (mBinding != null) {
            mBinding.topLayout.bannerFirstPage.stopAutoPlay();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.topLayout.bannerFirstPage.stopAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            mBinding.topLayout.bannerFirstPage.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.topLayout.bannerFirstPage.stopAutoPlay();
    }
}
