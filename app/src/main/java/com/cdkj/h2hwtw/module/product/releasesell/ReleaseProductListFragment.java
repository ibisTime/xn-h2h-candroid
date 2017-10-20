package com.cdkj.h2hwtw.module.product.releasesell;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ReleaseProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.LayoutReleaseProductEmptyBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.product.ProductReleaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 发布产品
 * Created by cdkj on 2017/10/20.
 */

public class ReleaseProductListFragment extends BaseRefreshHelperFragment {


    public static final String RELEASETYPE = "3";//已发布
    public static final String DOWNTYPE = "4";//已下架

    private String mType = RELEASETYPE;
    private ReleaseProductListAdapter mListadapter;
    private LayoutReleaseProductEmptyBinding emptyTipsBinding;

    public static ReleaseProductListFragment getInstanse(String requestType) {
        ReleaseProductListFragment fragment = new ReleaseProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", requestType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }

    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        mListadapter = new ReleaseProductListAdapter(listData);

        mListadapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ProductListModel.ListBean listBean = mListadapter.getItem(position);

                if (listBean == null) return;

                switch (view.getId()) {
                    case R.id.tv_down_product:
                        if (TextUtils.equals(RELEASETYPE, listBean.getStatus())) {
                            showDownDialog(listBean.getCode(), position);
                        } else {
                            showPutDialog(listBean.getCode(), position);
                        }

                        break;
                    case R.id.tv_edit_product:
                        if (TextUtils.equals(RELEASETYPE, listBean.getStatus())) {
                            UITipDialog.showInfo(mActivity, "请先下架产品再进行编辑");
                        } else {
                            ReleaseProductEditActivity.open(mActivity, listBean);
                        }
                        break;
                }
            }
        });

        return mListadapter;
    }

    /**
     * 显示下架确认弹框
     *
     * @param code
     * @param position
     */
    private void showDownDialog(final String code, final int position) {

        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
                .setTitle("提示").setContentMsg("确认下架该产品？")
                .setPositiveBtn("确定", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        downProductRequest(code, position);
                    }
                })
                .setNegativeBtn("取消", null, false);

        commonDialog.show();

    }

    /**
     * 显示下架确认弹框
     *
     * @param code
     * @param position
     */
    private void showPutDialog(final String code, final int position) {

        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
                .setTitle("提示").setContentMsg("确认上架架该产品？")
                .setPositiveBtn("确定", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        putProductRequest(code, position);
                    }
                })
                .setNegativeBtn("取消", null, false);

        commonDialog.show();

    }

    @Override
    protected void onInit() {
        super.onInit();
        if (getArguments() != null) {
            mType = getArguments().getString("type");
        }
        initRefreshHelper(1, 10);
        mRefreshHelper.onDefaluteMRefresh(true);
    }


    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        Map map = new HashMap<>();

        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("orderColumn", "update_datetime");
        map.put("orderDir", "desc");
        map.put("userId", SPUtilHelpr.getUserId());

        List<String> statusList = new ArrayList<>();
        if (mType == RELEASETYPE) {
            statusList.add("3");
        } else {
            statusList.add("4");
            statusList.add("5");
            statusList.add("6");
        }

        map.put("statusList", statusList);

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808021", StringUtils.getJsonToString(map));

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
     * 下架产品请求
     */
    private void downProductRequest(String productCode, final int position) {
        if (TextUtils.isEmpty(productCode)) return;

        Map<String, String> map = new HashMap<String, String>();
        map.put("code", productCode);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        Call call = RetrofitUtils.getBaseAPiService().successRequest("808014", StringUtils.getJsonToString(map));

        addCall(call);
        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(mActivity, "产品下架成功");
                    mListadapter.remove(position);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showSuccess(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 上架产品请求
     */
    private void putProductRequest(String productCode, final int position) {
        if (TextUtils.isEmpty(productCode)) return;

        Map<String, String> map = new HashMap<String, String>();
        map.put("code", productCode);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("location", "0");
        map.put("orderNo", "0");
        Call call = RetrofitUtils.getBaseAPiService().successRequest("808013", StringUtils.getJsonToString(map));

        addCall(call);
        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(mActivity, "产品上架成功");
                    mListadapter.remove(position);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showSuccess(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    public boolean loadDeflutEmptyView() {
        return false;
    }

    @Override
    public View getEmptyView() {

        emptyTipsBinding = DataBindingUtil.inflate(inflater, R.layout.layout_release_product_empty, null, false);

        emptyTipsBinding.btnGoRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductReleaseActivity.open(mActivity);
            }
        });

        return emptyTipsBinding.getRoot();
    }

    @Override
    protected String getErrorInfo() {
        return "";
    }


}
