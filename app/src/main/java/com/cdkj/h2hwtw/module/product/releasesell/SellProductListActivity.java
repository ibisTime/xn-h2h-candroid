package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
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
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.order.OrderDetailsActivity;
import com.cdkj.h2hwtw.pop.SureSendOrderPop;
import com.chad.library.adapter.base.BaseQuickAdapter;

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
                showSendOrderDialog(null);
            }
        });

        seAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                OrderModel orderModel = seAdapter.getItem(position);
                if (orderModel == null) return;
                switch (view.getId()) {
                    case R.id.tv_cancel_order:
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

                        }

                        break;
                }
            }
        });

        return seAdapter;
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
        new SureSendOrderPop(this).showPopupWindow();
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
                UITipDialog.showSuccess(SellProductListActivity.this, "处理退款信息成功");
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

}
