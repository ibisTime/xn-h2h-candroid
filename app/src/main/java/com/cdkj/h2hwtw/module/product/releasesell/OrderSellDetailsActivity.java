package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionDkeyModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.SellProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityOrderDetailsBinding;
import com.cdkj.h2hwtw.model.LookCommenModel;
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.module.order.OrderDetailsActivity;
import com.cdkj.h2hwtw.other.OrderHelper;
import com.cdkj.h2hwtw.other.ProductHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.ORDERCHANGEREFRESH;

/**
 * 已卖出订单详情
 * Created by cdkj on 2017/10/19.
 */

public class OrderSellDetailsActivity extends AbsBaseLoadActivity {

    private ActivityOrderDetailsBinding mBinding;

    private String mOrderCode;//订单编号

    private OrderModel mOrderData;//订单数据

    private InputDialog CancelInputDialog;
//    private InputDialog CancePaylInputDialog;

    /**
     * @param context
     * @param orderCode
     */
    public static void open(Context context, String orderCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OrderSellDetailsActivity.class);
        intent.putExtra("code", orderCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_order_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.contentView.hindAll();
        mBaseBinding.titleView.setMidTitle("订单详情");
        if (getIntent() != null) {
            mOrderCode = getIntent().getStringExtra("code");
        }

        initListener();
    }

    private void initListener() {

        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCancelByState(mOrderData);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderRequest(mOrderCode);
    }

    /**
     * 获取订单数据请求
     */
    private void getOrderRequest(String code) {
        if (TextUtils.isEmpty(code)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);//DD2017101714122392762296

        Call call = RetrofitUtils.createApi(MyApiServer.class).getOrderDetails("808066", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<OrderModel>(this) {
            @Override
            protected void onSuccess(OrderModel data, String SucMessage) {
                setShowData(data);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mBaseBinding.contentView.setShowText(errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                mBaseBinding.contentView.setShowText(msg);
            }

            @Override
            protected void onFinish() {
                mBaseBinding.contentView.showContent(true);
                disMissLoading();
            }
        });

    }

    /**
     * 设置数据
     *
     * @param data
     */
    private void setShowData(OrderModel data) {
        if (data == null) return;

        mOrderData = data;

        getCompnayRequest(data.getLogisticsCompany());

        mBinding.tvOrderCode.setText("订单编号:" + data.getCode());

        mBinding.tvOrderCreateData.setText("下单时间:" + DateUtil.formatStringData(data.getApplyDatetime(), DateUtil.DEFAULT_DATE_FMT));

        mBinding.tvOrderState.setText("订单状态:" + OrderHelper.getOrderStateString(data.getStatus()));

        mBinding.tvPrice.setText("X1");
        mBinding.tvOrderPriceAll.setText(MoneyUtils.getShowPriceSign(data.getAmount1()));
        BigDecimal money = OrderHelper.getAllMoney(data);
        mBinding.tvOrderPriceAll2.setText(MoneyUtils.getShowPriceSign(money));
        mBinding.tvYunfei.setText(MoneyUtils.getShowPriceSign(data.getYunfei()));

        mBinding.tvName.setText("收货人:" + data.getReceiver());
        mBinding.tvAddress.setText("收货地址:" + data.getReAddress());
        mBinding.tvPhone.setText(data.getReMobile());

        if (OrderHelper.isWaitePayOrder(data.getStatus())) {//如果是待支付状态 则隐藏说明
            mBinding.tvPayInfo.setVisibility(View.GONE);
        } else {
            mBinding.tvPayInfo.setVisibility(View.VISIBLE);
            mBinding.tvPayInfo.setText(getPayInfo(data));
        }

        if (TextUtils.isEmpty(data.getLogisticsCode())) {
            mBinding.tvLogisticsCode.setText("物流单号:暂无");
        } else {
            mBinding.tvLogisticsCode.setText("物流单号:" + data.getLogisticsCode());
        }

        if (TextUtils.isEmpty(data.getApplyNote())) {
            mBinding.tvRemark.setText("买家叮嘱:暂无");
        } else {
            mBinding.tvRemark.setText("买家叮嘱:" + data.getApplyNote());
        }


        //产品数据不为空
        if (data.getProductOrderList() != null && data.getProductOrderList().size() > 0 && data.getProductOrderList().get(0) != null) {
            OrderModel.ProductOrderListBean plistBean = data.getProductOrderList().get(0);
            ImgUtils.loadImg(this, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(plistBean.getProductPic()), mBinding.imgGood);
            mBinding.tvOrderName.setText(plistBean.getProductName());
            mBinding.tvOrderInfo.setText(plistBean.getProductDescription());
        }

        setShowButtomBtnState(data.getStatus());

    }


    /**
     * 支付方方式说明
     *
     * @param data
     * @return
     */
    @NonNull
    private String getPayInfo(OrderModel data) {
        StringBuffer sb = new StringBuffer();

        sb.append("支付方式：");

        if (BigDecimalUtils.doubleValue(data.getAmount1()) > 0) {
            sb.append("人民币");
            sb.append(MoneyUtils.showPrice(data.getPayAmount1()));
        }

        if (BigDecimalUtils.doubleValue(data.getPayAmount2()) > 0) {
            if (BigDecimalUtils.doubleValue(data.getPayAmount2()) > 0) {
                sb.append("+积分");
            } else {
                sb.append("积分");
            }
            sb.append(MoneyUtils.showPrice(data.getPayAmount2()));
        }
        if (BigDecimalUtils.doubleValue(data.getPayAmount3()) > 0) {

            if (BigDecimalUtils.doubleValue(data.getAmount1()) > 0 || BigDecimalUtils.doubleValue(data.getPayAmount2()) > 0) {
                sb.append("+优惠券");
            } else {
                sb.append("优惠券");
            }

            sb.append(MoneyUtils.showPrice(data.getPayAmount3()));
        }

        return sb.toString();
    }


    /**
     * 根据状态设置底部按钮
     *
     * @param showState
     */
    public void setShowButtomBtnState(String showState) {


        mBinding.tvCancel.setText(OrderHelper.getDoStateString(showState));

        if (OrderHelper.canShowDoBtn(showState)) {
            mBinding.tvCancel.setVisibility(View.VISIBLE);
            mBinding.linButState.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvCancel.setVisibility(View.GONE);
            mBinding.linButState.setVisibility(View.GONE);
        }

        mBinding.tvCancel.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_click_un_app));
        mBinding.tvCancel.setTextColor(ContextCompat.getColor(this, R.color.white_app));

        mBinding.tvStateBottom.setVisibility(View.GONE);
//        mBinding.tvStateBottom.setText(OrderHelper.getSellStateString(showState));

    }


    /**
     * 根据订单状态执行相应操作
     *
     * @param orderModel 订单状态
     */
    private void doCancelByState(OrderModel orderModel) {

        if (orderModel == null) return;

        if (OrderHelper.isWaitePayOrder(orderModel.getStatus())) {//取消订单
            showCancelInputDialog(orderModel.getCode());
        }
        if (OrderHelper.isBackPayOrder(orderModel.getStatus())) {//退款申请
            showBackPayDialog(orderModel.getCode());
        }
        if (OrderHelper.isPayDoneOrder(orderModel.getStatus())) {//发货
            showSendOrderDialog(orderModel.getCode());
        }
        if (OrderHelper.isCommentsDoneOrder(orderModel.getStatus())) {//查看评价
            showCommentDialog(orderModel.getCode());
        }

    }


    /**
     * 显示发货弹框
     *
     * @param code
     */
    private void showSendOrderDialog(String code) {
        SureSendOrderActivity.open(this, code);
    }

    /**
     * 显示是否退款弹框
     */
    public void showBackPayDialog(final String code) {
        SureBackPayActivity.open(OrderSellDetailsActivity.this, code);

    }


    /**
     * 显示取消弹框
     */
    public void showCancelInputDialog(final String code) {

        CancelInputDialog = new InputDialog(this).builder().setTitle("取消订单")
                .setPositiveBtn("确定", new InputDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view, String inputMsg) {
                        cancelOrder(code, CancelInputDialog.getContentView().getText().toString());
                    }
                })
                .setNegativeBtn("取消", null)
                .setContentMsg("");
        CancelInputDialog.getContentView().setHint("请输入取消备注");

        CancelInputDialog.show();
    }


    /**
     * 取消待支付订单
     */
    private void cancelOrder(String code, String remark) {

        if (!SPUtilHelpr.isLogin(this, false)) {
            return;
        }

        if (TextUtils.isEmpty(code)) {
            return;
        }

        final Map map = new HashMap();

        map.put("code", code);
        map.put("remark", remark);
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808053", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(OrderSellDetailsActivity.this, "订单取消成功");
                getOrderRequest(mOrderCode);//取消成功刷新列表
                changeRefreshOrderList();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderSellDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取物流公司
     */
    private void getCompnayRequest(final String key) {

        Map<String, String> map = new HashMap<>();

        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("parentKey", "back_kd_company");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getdKeyListInfo("801907", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseListCallBack<IntroductionDkeyModel>(this) {
            @Override
            protected void onSuccess(List<IntroductionDkeyModel> data, String SucMessage) {

                boolean isUse = false;
                for (IntroductionDkeyModel model : data) {
                    if (TextUtils.equals(model.getDkey(), key)) {
                        mBinding.tvLogisticsCompany.setText("物流公司:" + model.getDvalue());
                        isUse = true;
                        break;
                    }
                }
                if (!isUse) {
                    mBinding.tvLogisticsCompany.setText("物流公司:暂无");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mBinding.tvLogisticsCompany.setText("物流公司:暂无");
                UITipDialog.showFall(OrderSellDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
            }
        });
    }


    /**
     * 显示查看评论弹框
     *
     * @param orderCode
     */
    private void showCommentDialog(String orderCode) {

        Map map = RetrofitUtils.getRequestMap();
        map.put("orderCode", orderCode);
        map.put("status", "AB");
        Call call = RetrofitUtils.createApi(MyApiServer.class).getCommenDetails("801029", StringUtils.getJsonToString(map));

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<LookCommenModel>(this) {
            @Override
            protected void onSuccess(LookCommenModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getContent())) {
                    UITipDialog.showInfo(OrderSellDetailsActivity.this, "该订单还没有评价");
                    return;
                }
                if (!StringUtils.isFilterCommentsByState(data.getStatus())) {
                    UITipDialog.showInfo(OrderSellDetailsActivity.this, "该评价存在敏感词，平台审核中.");
                    return;
                }

                CommonDialog commonDialog = new CommonDialog(OrderSellDetailsActivity.this).builder()
                        .setTitle("评价").setContentMsg(data.getContent())
                        .setPositiveBtn("确定", null);
                commonDialog.getContentView().setGravity(Gravity.CENTER);

                commonDialog.show();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    @Subscribe
    public void EventRefreshList(String tag) {
        if (TextUtils.equals(tag, EventTags.BACKPAYSUCC)) { //处理退款信息成功
            getOrderRequest(mOrderCode);
            changeRefreshOrderList();
            return;
        }
    }


    /**
     * 当本页面的数据改变时刷新订单列表
     */
    private void changeRefreshOrderList() {
        EventBus.getDefault().post(ORDERCHANGEREFRESH);
    }
}
