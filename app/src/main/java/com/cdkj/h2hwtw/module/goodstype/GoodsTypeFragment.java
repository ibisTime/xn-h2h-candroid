package com.cdkj.h2hwtw.module.goodstype;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductListAdapter;
import com.cdkj.h2hwtw.adapters.ProductTypeAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.FragmentTypeBinding;
import com.cdkj.h2hwtw.databinding.LayoutRecyclerviewBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.module.product.ProductScreeningActivity;
import com.cdkj.h2hwtw.module.search.SearchActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.MAINFINISH;
import static com.cdkj.baselibrary.appmanager.EventTags.RELEASESUSS;

/**
 * 类别
 * Created by cdkj on 2017/10/9.
 */

public class GoodsTypeFragment extends BaseRefreshHelperFragment<ProductListModel.ListBean> {


    private FragmentTypeBinding mBinding;

    private LayoutRecyclerviewBinding mHeadBinding;//头部分类菜单
    private ProductTypeAdapter mTypeMenuAdapter;

    private boolean isFirstRequest;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static GoodsTypeFragment getInstanse() {
        GoodsTypeFragment fragment = new GoodsTypeFragment();
        return fragment;
    }

    @Override
    protected View getCreateView(LayoutInflater inflater) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_type, null, false);
        initHeadMenuView(inflater);
        initRefreshHelper(1, 10);
        mBinding.search.layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.open(mActivity, false);
            }
        });
        return mBinding.getRoot();
    }

    /**
     * 初始化头部分类菜单
     *
     * @param inflater
     */
    private void initHeadMenuView(LayoutInflater inflater) {
        mHeadBinding = DataBindingUtil.inflate(inflater, R.layout.layout_recyclerview, null, false);
        mHeadBinding.recyclerview.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mTypeMenuAdapter = new ProductTypeAdapter(new ArrayList<ProductTypeModel>());
        mHeadBinding.recyclerview.setAdapter(mTypeMenuAdapter);

        mTypeMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductTypeModel productTypeModel = mTypeMenuAdapter.getItem(position);
                if (productTypeModel == null) return;
                if (productTypeModel.isAllMenu()) { //判断是否点击全部
                    ProductMenuListActivity.open(mActivity);
                } else {
                    ProductScreeningActivity.open(mActivity, productTypeModel.getCode(), productTypeModel.getName());
                }

            }
        });

    }


    @Override
    public void onRefresh(int pageindex, int limit) {
        getMenuTypeRequest();
    }

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
//        mBinding.rvType.addItemDecoration(new MyDividerItemDecoration(mActivity, MyDividerItemDecoration.VERTICAL_LIST));
        return mBinding.rvType;
    }

    @Override
    protected String getErrorInfo() {
        return getString(R.string.no_product);
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshHelper != null && !isFirstRequest) {
            isFirstRequest = true;
            getMenuTypeRequest();
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }

    @Override
    public BaseQuickAdapter getAdapter(List<ProductListModel.ListBean> listData) {
        ProductListAdapter mProductAdapter = new ProductListAdapter(listData);
        mProductAdapter.addHeaderView(mHeadBinding.getRoot());
        mProductAdapter.setHeaderAndEmpty(true);
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


    /**
     * 获取分类菜单请求
     */
    private void getMenuTypeRequest() {

        Map<String, String> map = new HashMap();

        map.put("status", "1");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("parentCode", "0");
        map.put("type", "4");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductType("808007", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<ProductTypeModel>(mActivity) {
            @Override
            protected void onSuccess(List<ProductTypeModel> data, String SucMessage) {
                setTypeMenuData(data);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
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
     * 设置菜单数据
     *
     * @param data
     */
    private void setTypeMenuData(List<ProductTypeModel> data) {

        if (data.size() < 8) {     //不满8个时不用显示全部按钮
            mTypeMenuAdapter.replaceData(data);
            return;
        }

        List<ProductTypeModel> newdadas = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            ProductTypeModel productTypeModel = data.get(i);
            if (productTypeModel == null) continue;
            if (i > 6) {                    //只添加前七位
                break;
            }
            newdadas.add(productTypeModel);
        }

        ProductTypeModel productTypeModel = new ProductTypeModel();
        productTypeModel.setAllMenu(true);
        productTypeModel.setName("全部");
        productTypeModel.setPic("");
        newdadas.add(productTypeModel);

        mTypeMenuAdapter.replaceData(newdadas);
    }

    @Subscribe
    public void EventBus(String evbusTag) {
        if (TextUtils.equals(evbusTag, RELEASESUSS)) {//发布成功
            if (mRefreshHelper != null)
                mRefreshHelper.onDefaluteMRefresh(false);
        }

    }

}
