package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

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
import com.cdkj.h2hwtw.model.OrderModel;

import org.greenrobot.eventbus.EventBus;

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

    private SellProductListAdapter mOrderListAdapter = new SellProductListAdapter(new ArrayList<OrderModel>());//用户获取状态
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

        mBinding.tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        mBinding.tvOrderCreateData.setText("下单时间:" + DateUtil.formatStringData(data.getApplyDatetime(), DateUtil.DATE_YMD));

        mBinding.tvOrderState.setText("订单状态:" + getStateString(data.getStatus()));

        mBinding.tvPrice.setText("X1");
        mBinding.tvOrderPriceAll.setText(MoneyUtils.getShowPriceSign(data.getAmount1()));
        BigDecimal money = BigDecimalUtils.add(data.getAmount1(), data.getYunfei());
        mBinding.tvOrderPriceAll2.setText(MoneyUtils.getShowPriceSign(money));
        mBinding.tvYunfei.setText(MoneyUtils.getShowPriceSign(data.getYunfei()));

        mBinding.tvName.setText("收货人:" + data.getReceiver());
        mBinding.tvAddress.setText("收货地址:" + data.getReAddress());
        mBinding.tvPhone.setText(data.getReMobile());


        if (TextUtils.isEmpty(data.getLogisticsCode())) {
            mBinding.tvLogisticsCode.setText("物流单号:暂无");
        } else {
            mBinding.tvLogisticsCode.setText("物流单号:" + data.getLogisticsCode());
        }

        if (TextUtils.isEmpty(data.getRemark())) {
            mBinding.tvRemark.setText("买家叮嘱:");
        } else {
            mBinding.tvRemark.setText("买家叮嘱:" + data.getRemark());
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


    public String getStateString(String status) {
        /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (TextUtils.equals("1", status)) {
            return getString(R.string.order_state_1);
        }
        if (TextUtils.equals("2", status)) {
            return getString(R.string.order_state_2);
        }
        if (TextUtils.equals("3", status)) {
            return getString(R.string.order_state_3);
        }
        if (TextUtils.equals("4", status)) {
            return getString(R.string.order_state_4);
        }
        if (TextUtils.equals("5", status)) {
            return getString(R.string.order_state_5);
        }
        if (TextUtils.equals("6", status)) {
            return getString(R.string.order_state_6);
        }
        if (TextUtils.equals("7", status)) {
            return getString(R.string.order_state_7);
        }
        if (TextUtils.equals("8", status)) {
            return getString(R.string.order_state_8);
        }
        if (TextUtils.equals("91", status)) {
            return getString(R.string.order_state_91);
        }
        return "暂无";
    }


    /**
     * 根据状态设置底部按钮
     *
     * @param showState
     */
    public void setShowButtomBtnState(String showState) {

        mBinding.tvCancel.setText(mOrderListAdapter.getDoStateString(showState));
        mBinding.tvState.setText(mOrderListAdapter.getStateString(showState));

        if (mOrderListAdapter.canShowDoBtn(showState)) {
            mBinding.tvCancel.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvCancel.setVisibility(View.GONE);
        }
    }


    /**
     * 根据订单状态执行相应操作
     *
     * @param orderModel 订单状态
     */
    private void doCancelByState(OrderModel orderModel) {

        if (orderModel == null) return;

        if (TextUtils.equals(orderModel.getStatus(), "1")) {//取消订单 待发货
            showCancelInputDialog(orderModel.getCode());
        }
        if (TextUtils.equals(orderModel.getStatus(), "6")) {//退款申请
            showBackPayDialog(orderModel.getCode());
        }
        if (TextUtils.equals(orderModel.getStatus(), "2")) {//发货
            showSendOrderDialog(orderModel.getCode());
        }
        if (TextUtils.equals(orderModel.getStatus(), "5")) {//查看评价
            showCommentDialog(orderModel.getRemark());
        }

    }


    /**
     * 显示查看评论弹框
     *
     * @param remark
     */
    private void showCommentDialog(String remark) {
        showSendOrderDialog(remark);
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

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle("提示").setContentMsg("确认退款?")
                .setPositiveBtn("是", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        backPayRequest(code, true);
                    }
                })
                .setNegativeBtn("否", new CommonDialog.OnNegativeListener() {
                    @Override
                    public void onNegative(View view) {
                        backPayRequest(code, false);
                    }
                });

        commonDialog.setCancelable(true);
        commonDialog.setCanceledOnTouchOutside(true);

        commonDialog.show();

    }

    /**
     * 退款请求
     *
     * @param code
     * @param isSure 是否同意退款
     */
    private void backPayRequest(String code, boolean isSure) {

        /*code（必填） 编号 string
 remark（选填） 备注 string
 result（必填） 审核结果 string 0 不同意，1同意
 updater（必填） 更新人 string
*/
        Map map = new HashMap();

        map.put("code", code);
        map.put("remark", "");
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("result", isSure ? "1" : "0");

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808062", StringUtils.getJsonToString(map));

        showLoadingDialog();

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack(this) {
            @Override
            protected void onSuccess(Object data, String SucMessage) {
                UITipDialog.showSuccess(OrderSellDetailsActivity.this, "处理退款信息成功");
                changeRefreshOrderList();
                getOrderRequest(mOrderCode);
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
                if(!isUse){
                    mBinding.tvLogisticsCompany.setText("物流公司:暂无" );
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderSellDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 当本页面的数据改变时刷新订单列表
     */
    private void changeRefreshOrderList() {
        EventBus.getDefault().post(ORDERCHANGEREFRESH);
    }
}
