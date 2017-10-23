package com.cdkj.h2hwtw.module.user.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityActivityDetailsBinding;
import com.cdkj.h2hwtw.model.ActivityCenterModel;
import com.cdkj.h2hwtw.model.PutMoneySendModel;
import com.cdkj.h2hwtw.module.pay.PutMoneyActivity;
import com.cdkj.h2hwtw.module.product.ProductReleaseActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 充值 详情 报名
 * Created by cdkj on 2017/10/21.
 */

public class ActivityPutMoneyDetailsActivity extends AbsBaseLoadActivity {


    private ActivityActivityDetailsBinding mBinding;

    private PutMoneySendModel mPutMoneySendModel;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActivityPutMoneyDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_activity_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("活动详情");

        mBinding.btnIWantAdd.setText("我要充值");

        mBinding.btnIWantAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPutMoneySendModel == null) return;
                PutMoneyActivity.open(ActivityPutMoneyDetailsActivity.this, mPutMoneySendModel.getCode());
            }
        });
        getListDataRequest(1, 1, true);
    }


    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
        Map<String, String> map = new HashMap();

        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("limit", pageindex + "");
        map.put("start", limit + "");
        map.put("status", "1");
        map.put("type", "4");
        map.put("orderColumn", "start_datetime");
        map.put("orderDir", "desc");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getActivitySendList("801050", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<PutMoneySendModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<PutMoneySendModel> data, String SucMessage) {
                if (data.getList() == null || data.getList().size() < 1) {
                    return;
                }
                mPutMoneySendModel = data.getList().get(0);
                if (mPutMoneySendModel == null) return;
                mBinding.webView.loadData(mPutMoneySendModel.getDescription(), "text/html;charset=UTF-8", "UTF-8");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onNoNet(String msg) {

            }

            @Override
            protected void onFinish() {
                if (isShowDialog) disMissLoading();
            }
        });

    }
}
