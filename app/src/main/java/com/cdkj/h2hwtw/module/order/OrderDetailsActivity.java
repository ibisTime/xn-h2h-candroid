package com.cdkj.h2hwtw.module.order;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.EventBusModel;
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
import com.cdkj.h2hwtw.adapters.OrderListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityOrderDetailsBinding;
import com.cdkj.h2hwtw.model.LookCommenModel;
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.module.pay.OrderPayActivity;
import com.cdkj.h2hwtw.module.product.releasesell.OrderSellDetailsActivity;
import com.cdkj.h2hwtw.module.product.releasesell.SellProductListActivity;
import com.cdkj.h2hwtw.other.OrderHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BUYLINE;
import static com.cdkj.baselibrary.appmanager.EventTags.ORDERCHANGEREFRESH;
import static com.cdkj.baselibrary.appmanager.EventTags.RELEASESCOMMENTSORDER;

/**
 * 订单详情
 * Created by cdkj on 2017/10/19.
 */

public class OrderDetailsActivity extends AbsBaseLoadActivity {

    private ActivityOrderDetailsBinding mBinding;

    private String mOrderCode;//订单编号

    private OrderModel mOrderData;//订单数据

    private OrderListAdapter mOrderListAdapter = new OrderListAdapter(new ArrayList<OrderModel>());//用户获取状态
    private InputDialog CancelInputDialog;
    private InputDialog CancePaylInputDialog;

    /**
     * @param context
     * @param orderCode
     */
    public static void open(Context context, String orderCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OrderDetailsActivity.class);
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

        mBinding.tvStateBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSomtingByState(mOrderData);
            }
        });

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
        map.put("code", mOrderCode);//DD2017101714122392762296

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
                UITipDialog.showFall(OrderDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 根据状态设置底部按钮
     *
     * @param showState
     */
    public void setShowButtomBtnState(String showState) {

        mBinding.tvCancel.setText(mOrderListAdapter.getStateCancelString(showState));
        mBinding.tvStateBottom.setText(OrderHelper.getOrderDoStateString(showState));

        if (mOrderListAdapter.canShowCancel(showState)) {
            mBinding.tvCancel.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvCancel.setVisibility(View.GONE);
        }
    }


    /**
     * 根据订单状态执行相应操作
     *
     * @param state 订单状态
     */
    private void doSomtingByState(OrderModel state) {

        if (state == null) {
            return;
        }

        if (OrderHelper.isWaitePayOrder(state.getStatus())) {      //待支付跳转到支付页面
            OrderPayActivity.open(this, MoneyUtils.getShowPriceSign(OrderHelper.getAllMoney(state)), state.getCode(), false);
            return;
        }

        if (OrderHelper.isPayDoneOrder(state.getStatus())) {  //待发货 申请催货
            showCuiHuoDialog(state.getCode());
            return;
        }

        if (OrderHelper.isSendDoneOrder(state.getStatus())) {//确认收货
            showSureGetOrderDialog(state.getCode());
            return;
        }

    }

    /**
     * 根据订单状态执行相应操作
     *
     * @param state 订单状态
     */
    private void doCancelByState(OrderModel state) {

        if (state == null) return;

        if (OrderHelper.isWaitePayOrder(state.getStatus())) {      //待支付取消订单
            showCancelInputDialog(state.getCode());
            return;
        }

        if (OrderHelper.isPayDoneOrder(state.getStatus())) {//待发货 申请退款
            showCancePaylInputDialog(state.getCode());
            return;
        }

        if (OrderHelper.isGetDoneOrder(state.getStatus())) {//已收货 待评价
            if (state.getProductOrderList() != null && state.getProductOrderList().size() > 0 && state.getProductOrderList().get(0) != null) {
                OrderCommentsEditActivity.open(this, state.getProductOrderList().get(0).getCode(), state.getCode());
            }
            return;
        }

        if (OrderHelper.isCommentsDoneOrder(state.getStatus())) {//查看评价
            showCommentDialog(state.getCode());
            return;
        }

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
                    UITipDialog.showInfo(OrderDetailsActivity.this, "该订单还没有评价");
                    return;
                }

                if (!StringUtils.isFilterCommentsByState(data.getStatus())) {
                    UITipDialog.showInfo(OrderDetailsActivity.this, "该评价存在敏感词，平台审核中.");
                    return;
                }

                CommonDialog commonDialog = new CommonDialog(OrderDetailsActivity.this).builder()
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

    /**
     * 显示退款弹框
     */
    public void showCancePaylInputDialog(final String code) {

        CancePaylInputDialog = new InputDialog(this).builder().setTitle("退款申请")
                .setPositiveBtn("确定", new InputDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view, String inputMsg) {
                        cancelPayOrder(code, CancePaylInputDialog.getContentView().getText().toString());
                    }
                })
                .setNegativeBtn("取消", null)
                .setContentMsg("");
        CancePaylInputDialog.getContentView().setHint("请输入退款备注");

        CancePaylInputDialog.show();
    }

    /**
     * 申请退款
     */
    private void cancelPayOrder(String code, String remark) {

        if (!SPUtilHelpr.isLogin(this, false)) {
            return;
        }

        if (TextUtils.isEmpty(code)) {
            return;
        }

        final Map map = new HashMap();

        map.put("code", code);
        map.put("remark", remark);
        map.put("updater", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808061", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(OrderDetailsActivity.this, "退款申请提交成功");
                getOrderRequest(mOrderCode); //退款成功刷新列表
                changeRefreshOrderList();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

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
                UITipDialog.showSuccess(OrderDetailsActivity.this, "订单取消成功");
                getOrderRequest(mOrderCode);//取消成功刷新列表
                changeRefreshOrderList();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 显示催货弹框
     *
     * @param code
     */
    private void showCuiHuoDialog(final String code) {
        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle("催货").setContentMsg("每个订单只能催货一次。")
                .setPositiveBtn("确定", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        cuiHuoRequest(code);
                    }
                })
                .setNegativeBtn("取消", null, false);

        commonDialog.show();
    }


    /**
     * 催货请求
     *
     * @param code
     */
    private void cuiHuoRequest(String code) {
        if (TextUtils.isEmpty(code)) {
            return;
        }

        final Map map = new HashMap();

        map.put("code", code);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808058", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(OrderDetailsActivity.this, "催货成功");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 确认收货弹框
     *
     * @param code
     */
    private void showSureGetOrderDialog(final String code) {
        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle("提示").setContentMsg("确认已经收到货物？")
                .setPositiveBtn("确定", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        sureGetOrderRequest(code);
                    }
                })
                .setNegativeBtn("取消", null, false);

        commonDialog.show();
    }

    /**
     * 确认收货请求
     *
     * @param code
     */
    private void sureGetOrderRequest(String code) {

        if (!SPUtilHelpr.isLogin(this, false)) {
            return;
        }

        if (TextUtils.isEmpty(code)) {
            return;
        }

        final Map map = new HashMap();


        map.put("code", code);
        map.put("remark", "");
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808057", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(OrderDetailsActivity.this, "收货成功");
                getOrderRequest(mOrderCode); //收货成功刷新列表
                changeRefreshOrderList();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 评价成功
     *
     * @param tag
     */
    @Subscribe
    public void Events(String tag) {
        if (TextUtils.equals(RELEASESCOMMENTSORDER, tag)) {
            getOrderRequest(mOrderCode);
        }
        if (TextUtils.equals(BUYLINE, tag)) {//支付成功
            changeRefreshOrderList();
        }
    }

    /**
     * 当本页面的数据改变时刷新订单列表
     */
    private void changeRefreshOrderList() {
        EventBus.getDefault().post(ORDERCHANGEREFRESH);
    }
}
