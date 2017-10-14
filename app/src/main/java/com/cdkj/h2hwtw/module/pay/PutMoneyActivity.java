package com.cdkj.h2hwtw.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.pay.WxPayRequestModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.payutils.PayUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityPutMoneyBinding;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 充值
 * Created by cdkj on 2017/10/14.
 */

public class PutMoneyActivity extends AbsBaseLoadActivity {

    private final String CALLPAYTAG = "PutMoneyPAY";

    private ActivityPutMoneyBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PutMoneyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_put_money, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("充值");

        StringUtils.setEditMoneyInput(mBinding.editPrice);

        mBinding.btnPutMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mBinding.editPrice.getText().toString())) {
                    UITipDialog.showFall(PutMoneyActivity.this, "请输入充值金额");
                    return;
                }
                if (new BigDecimal(mBinding.editPrice.getText().toString()).doubleValue() <= 1) {
                    UITipDialog.showFall(PutMoneyActivity.this, "金额必须大于1");
                    return;
                }
                UITipDialog.showFall(PutMoneyActivity.this, "微信支付未开通");
//                rechargeWXRequest();
            }
        });
    }

    /**
     * 微信充值请求
     */
    private void rechargeWXRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("applyUser", SPUtilHelpr.getUserId());
        map.put("channelType", "36");//36微信app支付 30支付宝支付
        map.put("amount", MoneyUtils.getRequestPrice(mBinding.editPrice.getText().toString()));
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companycode", MyCdConfig.COMPANYCODE);

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().wxPayRequest("802710", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<WxPayRequestModel>(this) {
            @Override
            protected void onSuccess(WxPayRequestModel data, String SucMessage) {
                PayUtil.callWXPay(PutMoneyActivity.this, data, CALLPAYTAG);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(PutMoneyActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}
