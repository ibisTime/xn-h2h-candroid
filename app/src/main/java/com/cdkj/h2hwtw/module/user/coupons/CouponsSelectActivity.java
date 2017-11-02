package com.cdkj.h2hwtw.module.user.coupons;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.CouponsSelectListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.PopCouponsSelectBinding;
import com.cdkj.h2hwtw.model.CouponsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 优惠券选择
 * Created by cdkj on 2017/10/19.
 */

public class CouponsSelectActivity extends AbsBaseLoadActivity {

    private CouponsSelectListAdapter mSelectAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CouponsSelectActivity.class);
        context.startActivity(intent);
    }

    private RefreshHelper mRfreshHelper;

    private PopCouponsSelectBinding mBinding;

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.pop_coupons_select, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTitle("");

        initNoSelect();
        initRefresh();

        mBinding.btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBinding.noselectt.checkboxCouponSelect.isChecked()) {
                    CouponsModel couponsModel = mSelectAdapter.getSelectItem();
                    if (couponsModel == null) {
                        EventBus.getDefault().post(EventTags.NOSELECTCOUPONS);
                    } else {
                        EventBus.getDefault().post(couponsModel);
                    }
                } else {
                    EventBus.getDefault().post(EventTags.NOSELECTCOUPONS);
                }

                finish();
            }
        });
    }

    /**
     * 不使用优惠券
     */
    private void initNoSelect() {
        mBinding.noselectt.tvCouponName.setText("不使用优惠券");
        mBinding.noselectt.linNoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.noselectt.checkboxCouponSelect.setChecked(true);
                mSelectAdapter.setSelectPostion(-1);
            }
        });
    }

    /**
     * 刷新初始化
     */
    private void initRefresh() {
        mRfreshHelper = new RefreshHelper(this, new BaseRefreshCallBack() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                mBinding.refreshLayout.setEnableRefresh(false);//禁用下拉
                return mBinding.refreshLayout;
            }


            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                mSelectAdapter = new CouponsSelectListAdapter(listData);
                mSelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mBinding.noselectt.checkboxCouponSelect.setChecked(false);
                        mSelectAdapter.setSelectPostion(position);
                    }
                });
                return mSelectAdapter;
            }


            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getCouponsRequest(pageindex, limit, isShowDialog);
            }
        });
        mRfreshHelper.init(1, 10);
        mRfreshHelper.setErrorInfo("暂无可用优惠券");
        mRfreshHelper.onDefaluteMRefresh(false);
    }


    private void getCouponsRequest(int pageindex, int limit, final boolean isShowDialog) {

        Map map = new HashMap();
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("orderColumn", "par_value");
        map.put("orderDir", "desc");
        map.put("status", "0");
        map.put("toUser", SPUtilHelpr.getUserId());

        if (isShowDialog) showLoadingDialog();
        Call<BaseResponseModel<ResponseInListModel<CouponsModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getCouponsList("801118", StringUtils.getJsonToString(map));
        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CouponsModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<CouponsModel> data, String SucMessage) {
                mRfreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                if (isShowDialog) disMissLoading();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRfreshHelper != null) {
            mRfreshHelper.clear();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
