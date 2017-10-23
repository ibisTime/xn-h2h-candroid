package com.cdkj.h2hwtw.module.firstpage;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.LayoutRecyclerBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.RELEASESUSS;

/**
 * 类别
 * Created by cdkj on 2017/10/9.
 */

public class HotProductFragment extends BaseRefreshHelperFragment<ProductListModel.ListBean> {


    private LayoutRecyclerBinding mBinding;

    private boolean isFirstRequest;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static HotProductFragment getInstanse() {
        HotProductFragment fragment = new HotProductFragment();
        return fragment;
    }

    @Override
    protected View getCreateView(LayoutInflater inflater) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_recycler, null, false);
        initRefreshHelper(1, 5);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mBinding.recyclerviewBase.setLayoutManager(linearLayoutManager);
        mRefreshHelper.onDefaluteMRefresh(false);
        return mBinding.getRoot();
    }


    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return null;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mBinding.recyclerviewBase;
    }

    @Override
    protected String getErrorInfo() {
        return getString(R.string.no_product);
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshHelper != null && !isFirstRequest) {
            isFirstRequest = true;
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }

    @Override
    public BaseQuickAdapter getAdapter(List<ProductListModel.ListBean> listData) {
        ProductListAdapter mProductAdapter = new ProductListAdapter(listData);
        return mProductAdapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
        Map<String, String> map = new HashMap();
        map.put("limit", limit + "");
        map.put("pageindex", pageindex + "");
        map.put("start", pageindex + "");
//        map.put("location", "1"); //1热门
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
                mRefreshHelper.setData(data.getList());
                EventBus.getDefault().post("99999");
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


    @Subscribe
    public void EventBus(String evbusTag) {
        if (TextUtils.equals(evbusTag, RELEASESUSS)) {//发布成功
            if (mRefreshHelper != null)
                mRefreshHelper.onDefaluteMRefresh(false);
        }

        if (TextUtils.equals(evbusTag, "88888")) {//发布成功
            if (mRefreshHelper != null)
                mRefreshHelper.onMLoadMore(2, 5);
        }

    }

}
