package com.cdkj.h2hwtw.module.user.coupons;

import android.os.Bundle;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.CouponsListAdapter;
import com.cdkj.h2hwtw.adapters.CouponsSelectListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.CouponsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2017/10/19.
 */

public class CouponsFragment extends BaseRefreshHelperFragment {

    private boolean isFirstLoad;

    private int mState = 0;

    public static CouponsFragment getInstanse(int state) {
        CouponsFragment fragment = new CouponsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        return new CouponsListAdapter(listData);
    }

    @Override
    protected void onInit() {
        super.onInit();
        initRefreshHelper(1, 10);
        mRefreshHelper.setErrorImg(R.drawable.no_coments);

        if (getArguments() != null) {
            mState = getArguments().getInt("state");
        }

        if (getUserVisibleHint()) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (!isFirstLoad && mRefreshHelper != null && mRefreshBinding != null) {
            isFirstLoad = true;
            mRefreshHelper.onDefaluteMRefresh(true);
        }

    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
        Map map = new HashMap();
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("orderColumn", "par_value");
        map.put("orderDir", "desc");
        map.put("status", mState);
        map.put("toUser", SPUtilHelpr.getUserId());

        if (isShowDialog) showLoadingDialog();
        Call<BaseResponseModel<ResponseInListModel<CouponsModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getCouponsList("801118", StringUtils.getJsonToString(map));
        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CouponsModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CouponsModel> data, String SucMessage) {
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

    @Override
    protected String getErrorInfo() {
        return "暂无优惠券";
    }
}
