package com.cdkj.h2hwtw.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.pay.PaySucceedInfo;
import com.cdkj.baselibrary.model.pay.WxPayRequestModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.payutils.PayUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityPayBinding;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 订单支付
 * Created by cdkj on 2017/10/18.
 */

public class OrderPayActivity extends AbsBaseLoadActivity {

    private final String OrderPayTag = "OrderPay";

    public static void open(Context context, String price, String orderCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("price", price);
        intent.putExtra("orderCode", orderCode);
        context.startActivity(intent);
    }


    private ActivityPayBinding mBinding;

    private String mPrice;
    private String mOrderCode;

    private Object mCouponsData;//

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_pay, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.text_pay));

        if (getIntent() != null) {
            mPrice = getIntent().getStringExtra("price");
            mOrderCode = getIntent().getStringExtra("orderCode");
        }

        mBinding.tvPayPrice.setText(mPrice);

        initListener();
    }


    /**
     * 支付支请求
     */
    private void payRequest(String type) {

        if (TextUtils.isEmpty(mOrderCode)) {
            return;
        }

        Map map = new HashMap<>();

        List<String> codeList = new ArrayList<>();

        codeList.add(mOrderCode);

        map.put("codeList", codeList);
        map.put("couponCode", "");
        map.put("payType", type);

        showLoadingDialog();

        Call<BaseResponseModel<IsSuccessModes>> call = RetrofitUtils.getBaseAPiService().successRequest("808052", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderPayActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 微信支付请求
     */
    private void wxPayRequest(String type) {

        if (TextUtils.isEmpty(mOrderCode)) {
            return;
        }

        Map map = new HashMap<>();

        List<String> codeList = new ArrayList<>();

        codeList.add(mOrderCode);

        map.put("codeList", codeList);
        map.put("couponCode", "");
        map.put("payType", type);

        showLoadingDialog();

        Call<BaseResponseModel<WxPayRequestModel>> call = RetrofitUtils.getBaseAPiService().wxPayRequest("808052", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<WxPayRequestModel>(this) {
            @Override
            protected void onSuccess(WxPayRequestModel data, String SucMessage) {
                PayUtil.callWXPay(OrderPayActivity.this, data, OrderPayTag);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderPayActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 用于判断是什么支付
     *
     * @return
     */
    private int getPayType() {
   /*0、积分支付，1、余额支付，2、微信APP支付，5、微信H5支付，60、优惠券支付，61、积分+H5支付
    ，62、积分+余额支付，63、积分+优惠券支付，64、微信H5+优惠券支付，65、余额+优惠券支付
    ，66、积分+微信APP支付，67、优惠券+微信APP支付，68、积分+微信H5支付+优惠券
    ，69、积分+微信APP支付+优惠券，70、积分+余额支付+优惠券*/

        //积分支付
        if (!mBinding.checkboxPayYue.isChecked() && !mBinding.checkboxPayWx.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData == null) {
            LogUtil.E("payType 0 积分支付");
            return 0;
        }   //余额支付
        if (mBinding.checkboxPayYue.isChecked() && !mBinding.checkboxPayWx.isChecked()
                && !mBinding.checkboxPayJf.isChecked() && mCouponsData == null) {
            LogUtil.E("payType 1余额支付");
            return 1;
        }
        //微信支付
        if (mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && !mBinding.checkboxPayJf.isChecked() && mCouponsData == null) {
            LogUtil.E("payType 2 微信支付");
            return 2;
        }
        //优惠券支付
        if (!mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && !mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 60 优惠券支付");
            return 60;
        }
        //积分+余额支付
        if (!mBinding.checkboxPayWx.isChecked() && mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData == null) {
            LogUtil.E("payType 62 积分+余额支付");
            return 62;
        }
        //积分+优惠券支付
        if (!mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 63 积分+优惠券支付");
            return 63;
        }

        //积分+优惠券支付
        if (!mBinding.checkboxPayWx.isChecked() && mBinding.checkboxPayYue.isChecked()
                && !mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 65 余额+优惠券支付");
            return 65;
        }
        //积分+微信APP支付
        if (mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData == null) {
            LogUtil.E("payType 66 积分+微信APP支付");
            return 66;
        }
        //优惠券+微信APP支付
        if (mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && !mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 67 优惠券+微信APP支付");
            return 67;
        }
        //积分+微信APP支付+优惠券
        if (mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 69 积分+微信APP支付+优惠券");
            return 69;
        }

        //积分+余额支付+优惠券
        if (!mBinding.checkboxPayWx.isChecked() && mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 70 积分+余额支付+优惠券");
            return 70;
        }
        LogUtil.E("payType 暂无这支付方式");
        return 999;
    }

    private void initListener() {

        mBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySwitch();
            }
        });

        //微信支付
        mBinding.linPayWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.checkboxPayYue.setChecked(false);
                mBinding.checkboxPayWx.setChecked(!mBinding.checkboxPayWx.isChecked());
            }
        });
        //余额支付
        mBinding.linPayYue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.checkboxPayWx.setChecked(false);
                mBinding.checkboxPayYue.setChecked(!mBinding.checkboxPayYue.isChecked());
            }
        });

        //积分支付
        mBinding.linPayJf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.checkboxPayJf.setChecked(!mBinding.checkboxPayJf.isChecked());
            }
        });

        mBinding.linPayCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCouponsData = new EventTags();
            }
        });

    }

    /**
     * 支付方式选择
     */
    private void paySwitch() {
        if (!mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && !mBinding.checkboxPayJf.isChecked() && mCouponsData == null) {
            UITipDialog.showFall(OrderPayActivity.this, "请选择支付方式");
            return;
        }

        int payType = getPayType();
        switch (payType) {
            case 2:
            case 66:
            case 67:
            case 68:
            case 69:
                wxPayRequest(payType + "");     //微信支付
                break;
            case 999:
                UITipDialog.showFall(OrderPayActivity.this, "暂无此支付方式");
                break;
            default:
                payRequest(payType + ""); //普通支付
        }
    }

    /**
     * 支付成功
     */
    public void doPaySucceed() {

    }

    /**
     * 支付回调
     *
     * @param mo
     */
    @Subscribe
    public void PayState(PaySucceedInfo mo) {
        if (mo == null || !TextUtils.equals(mo.getTag(), OrderPayTag)) {
            return;
        }

        if (mo.getCallType() == PayUtil.ALIPAY && mo.isPaySucceed()) { //支付宝支付成功

        } else if (mo.getCallType() == PayUtil.WEIXINPAY && mo.isPaySucceed()) {//微信支付成功

        }
    }

}
