package com.cdkj.h2hwtw.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.pay.PaySucceedInfo;
import com.cdkj.baselibrary.model.pay.WxPayRequestModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.payutils.PayUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityPayBinding;
import com.cdkj.h2hwtw.model.AmountModel;
import com.cdkj.h2hwtw.model.CouponsModel;
import com.cdkj.h2hwtw.module.order.OrderListActivity;
import com.cdkj.h2hwtw.module.order.OrderListFramgnet;
import com.cdkj.h2hwtw.module.user.coupons.CouponsSelectActivity;

import org.greenrobot.eventbus.EventBus;
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

    private boolean isOpenOrderList;

    /**
     * @param context
     * @param price           价格
     * @param orderCode       订单编号
     * @param isOpenOrderList 是否打开订单记录列表
     */
    public static void open(Context context, String price, String orderCode, boolean isOpenOrderList) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("price", price);
        intent.putExtra("orderCode", orderCode);
        intent.putExtra("isOpenOrderList", isOpenOrderList);
        context.startActivity(intent);
    }


    private ActivityPayBinding mBinding;

    private String mPrice;
    private String mOrderCode;

    private CouponsModel mCouponsData;//

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
            isOpenOrderList = getIntent().getBooleanExtra("isOpenOrderList", false);
        }

        mBinding.tvPayPrice.setText(mPrice);

        initListener();
        getJFAmountRequest();
        getAmountRequest(true);
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
        if (mCouponsData != null) {
            map.put("couponCode", mCouponsData.getCode());
        }
        map.put("payType", type);

        showLoadingDialog();

        Call<BaseResponseModel<IsSuccessModes>> call = RetrofitUtils.getBaseAPiService().successRequest("808052", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    doPaySucceed();
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
        map.put("payType", type);
        if (mCouponsData != null) {
            map.put("couponCode", mCouponsData.getCode());
        }
        showLoadingDialog();

        Call<BaseResponseModel<WxPayRequestModel>> call = RetrofitUtils.getBaseAPiService().wxPayRequest("808052", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<WxPayRequestModel>(this) {
            @Override
            protected void onSuccess(WxPayRequestModel data, String SucMessage) {
                if (data.isSuccess()) {
                    doPaySucceed();
                    return;
                }
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
    ，70、积分+微信APP支付+优惠券，69、积分+余额支付+优惠券*/

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
        //积分+余额支付+优惠券
        if (mBinding.checkboxPayWx.isChecked() && !mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 69 积分+微信APP支付+优惠券");
            return 70;
        }

        if (!mBinding.checkboxPayWx.isChecked() && mBinding.checkboxPayYue.isChecked()
                && mBinding.checkboxPayJf.isChecked() && mCouponsData != null) {
            LogUtil.E("payType 70 积分+余额支付+优惠券");
            return 69;
        }
        LogUtil.E("payType 暂无这支付方式");
        return 999;
    }

    private void initListener() {

        mBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!SPUtilHelpr.isLogin(OrderPayActivity.this, false)) {
                    return;
                }

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
                CouponsSelectActivity.open(OrderPayActivity.this);
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
            case 70:
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

        call.enqueue(new BaseResponseListCallBack<AmountModel>(OrderPayActivity.this) {
            @Override
            protected void onSuccess(List<AmountModel> data, String SucMessage) {

                if (data != null && data.size() > 0 && data.get(0) != null) {
                    mBinding.tvYueInfo.setText("余额(当前可用余额" + MoneyUtils.showPrice(data.get(0).getAmount()) + ")");

                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });

    }

    /**
     * 获取积分请求
     */
    private void getJFAmountRequest() {
        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("currency", "JF");
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAmount("802503", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseListCallBack<AmountModel>(this) {
            @Override
            protected void onSuccess(List<AmountModel> data, String SucMessage) {

                if (data != null && data.size() > 0 && data.get(0) != null) {
                    mBinding.tvJfNum.setText("(当前可用积分" + MoneyUtils.showPrice(data.get(0).getAmount()) + ")");
                }

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
            }

            @Override
            protected void onFinish() {
                getJfDkInfo();
            }
        });
    }


    /**
     * 获取积分抵扣详情
     */
    public void getJfDkInfo() {

        Map map = RetrofitUtils.getRequestMap();

        map.put("key", "jf_dk_times");

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("808917", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                mBinding.tvJfInfo.setText("(" + data.getCvalue() + "积分抵扣1元人民币)");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });


    }


    /**
     * 支付成功
     */
    public void doPaySucceed() {
        EventBus.getDefault().post(EventTags.BUYLINE);
        EventBus.getDefault().post(EventTags.PAYSUCC);
        if (isOpenOrderList) {
            OrderListActivity.open(this, OrderListFramgnet.ORDERWAITESEND);
        }
        showToast(getString(R.string.pay_succeed));
        finish();
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
            doPaySucceed();
        }
    }

    /**
     * 优惠券选择
     *
     * @param mo
     */
    @Subscribe
    public void CouponsSelect(CouponsModel mo) {
        mCouponsData = mo;
        if (mo == null) {
            mBinding.tvPayCoupons.setText("选择优惠券");
            return;
        }
        mBinding.tvPayCoupons.setText("减免" + MoneyUtils.showPrice(mo.getParValue()) + "元");
    }

    /**
     * 优惠券没有选择
     */
    @Subscribe
    public void CouponsSelectNo(String tag) {
        if (TextUtils.equals(tag, EventTags.NOSELECTCOUPONS)) {
            mCouponsData = null;
            mBinding.tvPayCoupons.setText("选择优惠券");
        }
    }

}
