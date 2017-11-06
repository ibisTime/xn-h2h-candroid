package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.SellProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.LookCommenModel;
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.order.OrderDetailsActivity;
import com.cdkj.h2hwtw.other.OrderHelper;
import com.cdkj.h2hwtw.pop.SureSendOrderPop;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 出售列表 我卖出的
 * Created by cdkj on 2017/10/20.
 */

public class SellProductListActivity extends BaseRefreshHelperActivity {


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SellProductListActivity.class);
        context.startActivity(intent);
    }

    private InputDialog CancelInputDialog;

    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        final SellProductListAdapter seAdapter = new SellProductListAdapter(listData);
        seAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderModel orderModel = seAdapter.getItem(position);
                OrderSellDetailsActivity.open(SellProductListActivity.this, orderModel.getCode());
            }
        });

        seAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                OrderModel orderModel = seAdapter.getItem(position);
                if (orderModel == null) return;
                switch (view.getId()) {
                    case R.id.tv_cancel_order:
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

                        break;
                }
            }
        });

        return seAdapter;
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
                    UITipDialog.showInfo(SellProductListActivity.this, "该订单还没有评价");
                    return;
                }
                if (!StringUtils.isFilterCommentsByState(data.getStatus())) {
                    UITipDialog.showInfo(SellProductListActivity.this, "该评价存在敏感词，平台审核中.");
                    return;
                }

                CommonDialog commonDialog = new CommonDialog(SellProductListActivity.this).builder()
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

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        /*{"systemCode":"CD-WTW000016"
        ,"companyCode":"CD-WTW000016",
        "token":"TSYS_USER_WTWTK201710130935341077766"
        ,"start":1,"limit":10,"toUser":"U201710131355098586852",
        "orderColumn":"apply_datetime","orderDir":"desc"}*/
        Map map = new HashMap<>();
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("orderColumn", "apply_datetime");
        map.put("orderDir", "desc");
        map.put("toUser", SPUtilHelpr.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<OrderModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getOrderList("808065", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<OrderModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<OrderModel> data, String SucMessage) {
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
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我卖出的");
        mRefreshHelper.setErrorImg(R.drawable.no_order);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有卖出宝贝哦";
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
     * 取消待支付订单
     */
    private void cancelOrder(String code, String remark) {

        if (!SPUtilHelpr.isLogin(this, false)) {
            return;
        }

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map map = new HashMap();

        map.put("code", code);
        map.put("remark", remark);
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808053", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(SellProductListActivity.this, "订单取消成功");
                mRefreshHelper.onDefaluteMRefresh(false);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(SellProductListActivity.this, errorMessage);
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
        CancelInputDialog = new InputDialog(SellProductListActivity.this).builder().setTitle("取消订单")
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
     * 显示是否退款弹框
     */
    public void showBackPayDialog(final String code) {
        SureBackPayActivity.open(SellProductListActivity.this, code);
    }


    @Subscribe
    public void EventListener(String tag) {
        if (TextUtils.equals(EventTags.SENDORDERSUSS, tag)) { //发货成功
            if (mRefreshHelper != null) {
                mRefreshHelper.onDefaluteMRefresh(false);
            }

            return;
        }
        if (TextUtils.equals(EventTags.ORDERCHANGEREFRESH, tag)) {//订单详情修改成功
            if (mRefreshHelper != null) {
                mRefreshHelper.onDefaluteMRefresh(false);
            }
        }

        if (TextUtils.equals(tag, EventTags.BACKPAYSUCC)) {//退款成功
            mRefreshHelper.onDefaluteMRefresh(false);
            return;
        }
    }


}
