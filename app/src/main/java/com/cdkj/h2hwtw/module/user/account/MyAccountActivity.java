package com.cdkj.h2hwtw.module.user.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityMyAccountBinding;
import com.cdkj.h2hwtw.model.AmountModel;
import com.cdkj.h2hwtw.model.TotalAmountModel;
import com.cdkj.h2hwtw.module.pay.PutMoneyActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的账户
 * Created by cdjk on 2017/10/14.
 */

public class MyAccountActivity extends AbsBaseLoadActivity {

    private ActivityMyAccountBinding mBinding;

    private String mAmountaccountNumber;//账户

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyAccountActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_my_account, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        getAmountRequest(true);
        mBaseBinding.titleView.setMidTitle("我的账户");
        initListener();
    }

    private void initListener() {
        mBinding.layoutAccountList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAccountBillListActivity.open(MyAccountActivity.this, mAmountaccountNumber);
            }
        });

        mBinding.tvGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMoneyActivity.open(MyAccountActivity.this, mBinding.tvAmount.getText().toString(),mAmountaccountNumber);
            }
        });

        mBinding.tvPutMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PutMoneyActivity.open(MyAccountActivity.this,"");
            }
        });
    }


    /**
     * 获取余额请求
     */
    private void getAmountRequest(final boolean isShowdialog) {
        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("currency", MyCdConfig.MONEYTYPE);
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAmount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AmountModel>(this) {
            @Override
            protected void onSuccess(List<AmountModel> data, String SucMessage) {

                if (data != null && data.size() > 0 && data.get(0) != null) {
                    mBinding.tvAmount.setText(MoneyUtils.showPrice(data.get(0).getAmount()));
                    mAmountaccountNumber = data.get(0).getAccountNumber();
                    gettotaleRequest(mAmountaccountNumber);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(MyAccountActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }

    /**
     * 获取统计请求
     */
    private void gettotaleRequest(String code) {
        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("accountNumber", code);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getTotaleAmount("802903", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<TotalAmountModel>(this) {
            @Override
            protected void onSuccess(TotalAmountModel data, String SucMessage) {

                mBinding.tvInAmount.setText(MoneyUtils.showPrice(data.getInTotalAmount()));
                mBinding.tvOutAmount.setText(MoneyUtils.showPrice(data.getOutTotalAmount()));
                mBinding.tvTxAmount.setText(MoneyUtils.showPrice(data.getTxTotalAmount()));
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(MyAccountActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
            }
        });
    }


}
