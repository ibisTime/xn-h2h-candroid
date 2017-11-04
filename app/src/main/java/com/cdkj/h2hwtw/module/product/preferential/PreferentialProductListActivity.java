package com.cdkj.h2hwtw.module.product.preferential;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.base.BaseRefreshHelperAndLocationActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductListAdapter;
import com.cdkj.h2hwtw.adapters.ScreeningPriceAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityScreeningBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.model.ScreeningAddressModel;
import com.cdkj.h2hwtw.model.ScreeningPrictModel;
import com.cdkj.h2hwtw.model.ScreeningRightMenuState;
import com.cdkj.h2hwtw.model.ScreeningTypeModel;
import com.cdkj.h2hwtw.model.cityInfo.AddressInfo;
import com.cdkj.h2hwtw.module.search.SearchActivity;
import com.cdkj.h2hwtw.other.view.ProductScreeningView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 产品筛选 优惠活动
 * Created by cdkj on 2017/10/16.
 */

public class PreferentialProductListActivity extends BaseRefreshHelperAndLocationActivity<ProductListModel.ListBean> {

    //类型
    private String mTypeCode;

    private ActivityScreeningBinding mBinding;
    private ScreeningPriceAdapter mScreeningPriceAdapter;//价格筛选
    private ScreeningRightMenuState mRightMenuState;//右边筛选菜单状态


    private ScreeningAddressModel mAddressInfo;
    private ScreeningTypeModel mTypeInfo;
    private boolean mPriceUpstate = true; //默认升序

    private boolean isFirstRequest;//是否进行了第一次筛选

    private AMapLocation mapLocation;//定位信息
    /**
     * @param context
     * @param typeCode 类型编号
     */
    public static void open(Context context, String typeCode, String typeName) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PreferentialProductListActivity.class);
        intent.putExtra("typeCode", typeCode);
        intent.putExtra("typeName", typeName);
        context.startActivity(intent);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {

        initVar();

        initRightScreeningMenu();

        initTopScreeningMenu();

        initListener();

        mRefreshHelper.onDefaluteMRefresh(true);
        startLocation();
    }

    private void initListener() {
        mBinding.fraImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBinding.layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.open(PreferentialProductListActivity.this,true);
            }
        });
    }

    private void initVar() {
        if (getIntent() != null) {
            mTypeCode = getIntent().getStringExtra("typeCode");
            mBinding.screeningView.setTypeSelectName(getIntent().getStringExtra("typeName"));
        }
        mTypeInfo = new ScreeningTypeModel();

        if (!TextUtils.isEmpty(mTypeCode)) {
            mTypeInfo.setCategory(mTypeCode);
        }
    }

    private void initTopScreeningMenu() {
        //顶部筛选监听
        mBinding.screeningView.setListener(new ProductScreeningView.onScreeningListener() {
            @Override
            public void onTabClick(int position, boolean isOpen) {
                switch (position) {
                    case 0:                     //区域点击
                        if (mBinding.screeningView.getmAddressInfo() == null && isOpen) {//当城市的数据为空时获取数据
                            getCityData();
                        }
                        break;
                    case 1:
                        break;
                    case 2:

                        break;
                    case 3:                  //右边筛选
                        setRightMenuState();
                        mBinding.drawer.openDrawer(Gravity.RIGHT);
                        break;

                }
            }

            @Override
            public void onGetTypeData(String partentCode, boolean isLeft) {
                getMenuTypeRequest(partentCode, isLeft);
            }

            @Override
            public void onAddressSelect(ScreeningAddressModel address) {
                mAddressInfo = address;
                mRefreshHelper.onDefaluteMRefresh(true);
            }

            @Override
            public void onLocationClick() {
                if (mapLocation != null) {
                    mAddressInfo = new ScreeningAddressModel();
                    mAddressInfo.setArea(mapLocation.getDistrict());
                    mAddressInfo.setCity(mapLocation.getCity());
                    mAddressInfo.setLatitude(mapLocation.getLatitude() + "");
                    mAddressInfo.setLongitude(mapLocation.getLongitude() + "");
                    mAddressInfo.setProvince(mapLocation.getProvince() + "");
                    mRefreshHelper.onDefaluteMRefresh(true);
                }
            }

            @Override
            public void onRefreshLocation() {
                startLocation();
            }

            @Override
            public void onTypeSelect(ScreeningTypeModel typeModel) {
                mTypeInfo = typeModel;
                mRefreshHelper.onDefaluteMRefresh(true);
            }

            @Override
            public void onPriceSelect(boolean isUpPrice) {
                mPriceUpstate = isUpPrice;
                mRefreshHelper.onDefaluteMRefresh(true);
            }

        });
    }

    /**
     * 初始化右边筛选菜单
     */
    private void initRightScreeningMenu() {

        mRightMenuState = new ScreeningRightMenuState();

        mBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动

        mBinding.rightMenu.linScreeningRightBg.setOnClickListener(new View.OnClickListener() {//防止点击空白处底部响应
            @Override
            public void onClick(View view) {

            }
        });

        //价格筛选
        mScreeningPriceAdapter = new ScreeningPriceAdapter(getScreeningPrictList());
        mBinding.rightMenu.recyclerPrice.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.rightMenu.recyclerPrice.setAdapter(mScreeningPriceAdapter);
        mScreeningPriceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                mBinding.rightMenu.editLowPrice.setText("");
                mBinding.rightMenu.editHeightPrice.setText("");

                if (mScreeningPriceAdapter.getSelectPosition() == position) {//如果当前以经选择过
                    mScreeningPriceAdapter.setSelectPosition(-5);
                    mRightMenuState.setRequestHeightPrice("");
                    return;
                }
                mScreeningPriceAdapter.setSelectPosition(position);
                if (mScreeningPriceAdapter.getItem(position) == null) return;
                mRightMenuState.setRequestHeightPrice(mScreeningPriceAdapter.getItem(position).getPrice());
            }
        });

        mBinding.rightMenu.editHeightPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (TextUtils.isEmpty(charSequence.toString())) {

                    return;
                }

                mScreeningPriceAdapter.setSelectPosition(-5);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBinding.rightMenu.editLowPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {

                    return;
                }
                mScreeningPriceAdapter.setSelectPosition(-5);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //完成
        mBinding.rightMenu.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveRightMenuState();
                mBinding.drawer.closeDrawer(Gravity.RIGHT);
                rightMenuReset();

                mRefreshHelper.onDefaluteMRefresh(true);

            }
        });
        //重置
        mBinding.rightMenu.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightMenuReset();
            }
        });

    }

    /**
     * 保存右边菜单筛选状态
     */
    private void saveRightMenuState() {

        mRightMenuState.setRequestLowPrice("");

        mRightMenuState.setLowPrice(mBinding.rightMenu.editLowPrice.getText().toString());

        if (!TextUtils.isEmpty(mBinding.rightMenu.editLowPrice.getText().toString())) {
            mRightMenuState.setRequestLowPrice(mBinding.rightMenu.editLowPrice.getText().toString() + "000");//所有请求都要*1000
            mRightMenuState.setRequestHeightPrice("");
        }

        mRightMenuState.setHeightPrice(mBinding.rightMenu.editHeightPrice.getText().toString());
        if (!TextUtils.isEmpty(mBinding.rightMenu.editHeightPrice.getText().toString())) {
            mRightMenuState.setRequestHeightPrice(mBinding.rightMenu.editHeightPrice.getText().toString() + "000");//所有请求都要*1000
        }

        mRightMenuState.setSend(mBinding.rightMenu.checkboxIsSend.isChecked());
        mRightMenuState.setNew(mBinding.rightMenu.checkboxIsNew.isChecked());
        mRightMenuState.setPricePositio(mScreeningPriceAdapter.getSelectPosition());
    }

    /**
     * 设置右边菜单筛选状态
     */
    private void setRightMenuState() {
        mBinding.rightMenu.editHeightPrice.setText(mRightMenuState.getHeightPrice());
        mBinding.rightMenu.editLowPrice.setText(mRightMenuState.getLowPrice());
        mBinding.rightMenu.checkboxIsSend.setChecked(mRightMenuState.isSend());
        mBinding.rightMenu.checkboxIsNew.setChecked(mRightMenuState.isNew());
        mScreeningPriceAdapter.setSelectPosition(mRightMenuState.getPricePositio());
    }

    /**
     * 重置右边筛选
     */
    private void rightMenuReset() {

        mBinding.rightMenu.checkboxIsNew.setChecked(false);
        mBinding.rightMenu.checkboxIsSend.setChecked(false);
        mBinding.rightMenu.editHeightPrice.setText("");
        mBinding.rightMenu.editLowPrice.setText("");
        mScreeningPriceAdapter.setSelectPosition(-5);
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
        map.put("start", pageindex + "");
        map.put("status", "3");
        map.put("isJoin", "1");//参加优惠活动
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);

        if (mPriceUpstate) {
            map.put("orderColumn", "price");
            map.put("orderDir", "asc");
        } else {
            map.put("orderColumn", "price");
            map.put("orderDir", "desc");
        }

        if (isFirstRequest) {                 //第一次进入页面时请求所有
            if (mAddressInfo != null) {
                map.put("area", mAddressInfo.getArea());
                map.put("city", mAddressInfo.getCity());
                map.put("province", mAddressInfo.getProvince());
                map.put("longitude", mAddressInfo.getLongitude());
                map.put("latitude", mAddressInfo.getLatitude());
            }

            if (mTypeInfo != null) {
                map.put("category", mTypeInfo.getCategory());
                map.put("type", mTypeInfo.getType());
            }

            if (mRightMenuState != null) {
                map.put("isNew", mRightMenuState.isNew() ? "1" : "0");
                if (mRightMenuState.isSend()) {
                    map.put("yunfei", "0");
                }
                map.put("minPrice", mRightMenuState.getRequestLowPrice());
                map.put("maxPrice", mRightMenuState.getRequestHeightPrice());
            }
        }


        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808025", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ProductListModel>(this) {
            @Override
            protected void onSuccess(ProductListModel data, String SucMessage) {
                isFirstRequest = true;
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


    /**
     * 获取分类请求
     */
    private void getMenuTypeRequest(final String parentCode, final boolean isLeft) {

        Map<String, String> map = new HashMap();

        map.put("status", "1");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("parentCode", parentCode);
        map.put("type", "4");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductType("808007", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<ProductTypeModel>(PreferentialProductListActivity.this) {
            @Override
            protected void onSuccess(List<ProductTypeModel> data, String SucMessage) {
                mBinding.screeningView.setTypeData(data, isLeft, parentCode);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(PreferentialProductListActivity.this, errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                UITipDialog.showFall(PreferentialProductListActivity.this, msg);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 解析本地xml数据获取城市数据
     */
    public void getCityData() {

        showLoadingDialog();
        mSubscription.add(Observable.just(AppUtils.readAssetsTxt(PreferentialProductListActivity.this, "city_info.txt"))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, AddressInfo>() {
                    @Override
                    public AddressInfo apply(@NonNull String s) throws Exception {
                        return JSON.parseObject(s, AddressInfo.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        disMissLoading();
                    }
                })
                .subscribe(new Consumer<AddressInfo>() {
                    @Override
                    public void accept(AddressInfo s) throws Exception {
                        mBinding.screeningView.setmAddressInfo(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.E("地址2" + throwable);
                    }
                }));

    }


    @Override
    protected String getErrorInfo() {
        return getString(R.string.no_product);
    }

    @Override
    protected void locationSuccessful(AMapLocation aMapLocation) {
        if (aMapLocation == null) return;
        mapLocation = aMapLocation;
        mBinding.screeningView.setLocationName(aMapLocation.getCity());
    }

    @Override
    protected void locationFailure() {

    }

    @Override
    protected void onNegativeButton() {

    }

    @Override
    protected boolean canShowTipsDialog() {
        return false;
    }

    @Override
    protected void onDestroy() {
        mBinding.screeningView.clear();
        super.onDestroy();
    }

    public List<ScreeningPrictModel> getScreeningPrictList() {

        List<String> price = new ArrayList<>();

        price.add("100");
        price.add("300");
        price.add("500");
        price.add("1000");
        price.add("2000");
        price.add("3000");

        List<ScreeningPrictModel> screeningPrictModels = new ArrayList<>();

        for (String s : price) {
            ScreeningPrictModel scrollingMovementMethod = new ScreeningPrictModel();
            scrollingMovementMethod.setName(s + "元以下");
            scrollingMovementMethod.setPrice(s + "000"); //发送请求的价格都要乘上1000
            screeningPrictModels.add(scrollingMovementMethod);
        }

        return screeningPrictModels;
    }

    /**
     * 退出
     *
     * @return
     */
    @Override
    public void onBackPressed() {
        if (mBinding.drawer.isDrawerOpen(Gravity.END)) {
            mBinding.drawer.closeDrawer(Gravity.END);
            return;
        }
        if (mBinding.screeningView.isOpen()) {
            mBinding.screeningView.closeAll();
            return;
        }
        super.onBackPressed();
    }


}
