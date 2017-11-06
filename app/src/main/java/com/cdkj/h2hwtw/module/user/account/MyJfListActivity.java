package com.cdkj.h2hwtw.module.user.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.adapters.MyAccountBillAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.BillListMode;
import com.cdkj.h2hwtw.model.BillModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 积分流水
 * Created by cdkj on 2017/10/14.
 */

public class MyJfListActivity extends BaseRefreshHelperActivity<BillModel> {


    private String mCode;

    /**
     * @param context
     * @param code    //用户账户号
     */
    public static void open(Context context, String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyJfListActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List<BillModel> listData) {
        return new MyAccountBillAdapter(listData);
    }

    @Override
    public void getListDataRequest(int pageIndex, int limit, final boolean canShowDialog) {
        if (TextUtils.isEmpty(mCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("accountNumber", mCode);
        map.put("accountType", "C");
        map.put("currency", "JF");
        map.put("start", pageIndex + "");
        map.put("limit", "10");
        Call call = RetrofitUtils.createApi(MyApiServer.class).getBillList("802524", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<BillListMode>(this) {

            @Override
            protected void onSuccess(BillListMode data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.loadError(errorMessage);
            }

            @Override
            protected void onFinish() {
                if (canShowDialog) disMissLoading();
            }
        });
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("积分账单");
        if (getIntent() != null) {
            mCode = getIntent().getStringExtra("code");
        }
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "暂无流水";
    }
}
