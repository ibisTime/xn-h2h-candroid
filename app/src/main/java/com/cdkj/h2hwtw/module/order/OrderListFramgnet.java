package com.cdkj.h2hwtw.module.order;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.OrderListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.module.pay.OrderPayActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.ORDERCHANGEREFRESH;
import static com.cdkj.baselibrary.appmanager.EventTags.RELEASESCOMMENTSORDER;

/**
 * Created by 李先俊 on 2017/10/18.
 */

public class OrderListFramgnet extends BaseRefreshHelperFragment<OrderModel> {

    private boolean isFirst = false;//用于faragment懒加载的时候只请求一次数据
    private boolean isChangeRefresh = false;//用于在订单详情界面数据改动后刷新本页面

    private int mState;

    /*  list.add("全部");
        list.add("待支付");
        list.add("待发货");
        list.add("待收货");
        list.add("已完成");
        list.add("已评价");*/

    public static final int ORDERALL = 0;//全部
    public static final int ORDERWAITEPAY = 1;//待支付
    public static final int ORDERWAITESEND = 2;//待发货
    public static final int ORDERWAITEGET = 3;//待收货
    public static final int ORDERSAY = 4;//待评价
    public static final int ORDERDONE = 5;//已完成
    private InputDialog CancelInputDialog;
    private InputDialog CancePaylInputDialog;


    @IntDef({ORDERALL, ORDERWAITEPAY, ORDERWAITESEND, ORDERWAITEGET, ORDERDONE, ORDERSAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface loadState {
    }

    private OrderListAdapter mOrderAdapter;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static OrderListFramgnet getInstanse(@loadState int state) {
        OrderListFramgnet fragment = new OrderListFramgnet();
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BaseQuickAdapter getAdapter(List<OrderModel> listData) {
        mOrderAdapter = new OrderListAdapter(listData);

        //状态按钮添加点击事件
        mOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderModel orderModel = mOrderAdapter.getItem(position);
                if (orderModel == null) return;
                switch (view.getId()) {
                    case R.id.tv_order_state:
                        doSomtingByState(orderModel);
                        break;
                    case R.id.tv_cancel_order:
                        doCancelByState(orderModel);
                        break;
                }
            }
        });
        mOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderModel orderModel = mOrderAdapter.getItem(position);
                if (orderModel == null) return;
                OrderDetailsActivity.open(mActivity, orderModel.getCode());
            }
        });

        return mOrderAdapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        Map map = new HashMap<String, String>();

        map.put("applyUser", SPUtilHelpr.getUserId());
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("orderColumn", "apply_datetime");
        map.put("orderDir", "desc");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("statusList", getRequestState());


        Call<BaseResponseModel<ResponseInListModel<OrderModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getOrderList("808068", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<OrderModel>>(mActivity) {
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

    /**
     * 根据订单状态执行相应操作
     *
     * @param state 订单状态
     */
    private void doSomtingByState(OrderModel state) {

        if (TextUtils.equals("1", state.getStatus())) {      //待支付跳转到支付页面
            OrderPayActivity.open(mActivity, MoneyUtils.getShowPriceSign(mOrderAdapter.getAllMoney(state)), state.getCode(), false);
            return;
        }

        if (TextUtils.equals("2", state.getStatus())) {  //待发货 申请催货
            showCuiHuoDialog(state.getCode());
            return;
        }

        if (TextUtils.equals("3", state.getStatus())) {//确认收货
            showSureGetOrderDialog(state.getCode());
            return;
        }

        if (TextUtils.equals("4", state.getStatus())) {//已收货 待评价
            if (state.getProductOrderList() != null && state.getProductOrderList().size() > 0 && state.getProductOrderList().get(0) != null) {
                OrderCommentsEditActivity.open(mActivity, state.getProductOrderList().get(0).getCode(), state.getCode());
            }
            return;
        }
    }

    /**
     * 显示催货弹框
     *
     * @param code
     */
    private void showCuiHuoDialog(final String code) {
        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
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
     * 确认收货弹框
     *
     * @param code
     */
    private void showSureGetOrderDialog(final String code) {
        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
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

        if (!SPUtilHelpr.isLogin(mActivity, false)) {
            return;
        }

        if (TextUtils.isEmpty(code)) {
            return;
        }

        final Map map = new HashMap();

        map.put("code", code);
        map.put("remark", "");
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808057", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(mActivity, "确认收货成功");
                mRefreshHelper.onDefaluteMRefresh(false); //收货成功刷新列表
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
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

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(mActivity, "催货成功");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


    /**
     * 根据订单状态执行相应操作
     *
     * @param state 订单状态
     */
    private void doCancelByState(OrderModel state) {

        if (TextUtils.equals("1", state.getStatus())) {      //待支付取消订单
            showCancelInputDialog(state.getCode());
            return;
        }

        if (TextUtils.equals("2", state.getStatus())) {//待发货 申请退款
            showCancePaylInputDialog(state.getCode());
            return;
        }

    }

    /**
     * 根据传入状态请求数据
     *
     * @return
     */
    private List<String> getRequestState() {
        List<String> stringList = new ArrayList<>();
/*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，  6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/


        switch (mState) {
            case ORDERWAITEPAY:
                stringList.add("1");
                break;
            case ORDERWAITESEND:
                stringList.add("2");
                break;
            case ORDERWAITEGET:
                stringList.add("3");
                break;
            case ORDERSAY:
                stringList.add("4");
                break;
            case ORDERDONE:
                stringList.add("5");
                break;
        }
        return stringList;
    }

    /**
     * 取消待支付订单
     */
    private void cancelOrder(String code, String remark) {

        if (!SPUtilHelpr.isLogin(mActivity, false)) {
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

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                mRefreshHelper.onDefaluteMRefresh(false); //取消成功刷新列表
                UITipDialog.showSuccess(mActivity, "订单取消成功");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 申请退款
     */
    private void cancelPayOrder(String code, String remark) {

        if (!SPUtilHelpr.isLogin(mActivity, false)) {
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

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(mActivity, "退款申请提交成功");
                mRefreshHelper.onDefaluteMRefresh(false); //退款成功刷新列表
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(mActivity, errorMessage);
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

        CancelInputDialog = new InputDialog(mActivity).builder().setTitle("取消订单")
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
     * 显示退款弹框
     */
    public void showCancePaylInputDialog(final String code) {

        CancePaylInputDialog = new InputDialog(mActivity).builder().setTitle("退款申请")
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


    @Override
    protected void lazyLoad() {
        if (mRefreshHelper != null && mRefreshBinding != null && !isFirst) {
            mRefreshHelper.onDefaluteMRefresh(false);
            isFirst = true;
        }
    }

    @Override
    protected void onInit() {
        initRefreshHelper(1, 10);
        if (getArguments() != null) {
            mState = getArguments().getInt("state");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            if (mRefreshHelper != null && mRefreshBinding != null && !isChangeRefresh) {
                isFirst = true;
                isChangeRefresh = true;
                mRefreshHelper.onDefaluteMRefresh(false);
            }
        }
    }

    @Override
    protected String getErrorInfo() {
        return "暂无订单";
    }

    @Override
    protected int getErrorImg() {
        return R.drawable.no_order;
    }

    /**
     * 评价成功
     *
     * @param tag
     */
    @Subscribe
    public void Events(String tag) {
        if (TextUtils.equals(RELEASESCOMMENTSORDER, tag)) {
            if (mRefreshHelper != null) {
                mRefreshHelper.onDefaluteMRefresh(false);
            }
        } else if (TextUtils.equals(ORDERCHANGEREFRESH, tag)) { //数据改变
            isChangeRefresh = false;
        }
    }

}
