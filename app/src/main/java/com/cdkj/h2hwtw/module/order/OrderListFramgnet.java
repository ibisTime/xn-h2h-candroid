package com.cdkj.h2hwtw.module.order;

import android.os.Bundle;
import android.support.annotation.IntDef;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.OrderListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.OrderModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2017/10/18.
 */

public class OrderListFramgnet extends BaseRefreshHelperFragment<OrderModel> {

    private boolean isFirst = false;

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


    @IntDef({ORDERALL, ORDERWAITEPAY, ORDERWAITESEND, ORDERWAITEGET, ORDERDONE, ORDERSAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface loadState {
    }


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
        return new OrderListAdapter(listData);
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
            protected void onFinish() {
                if (isShowDialog) disMissLoading();
            }
        });


    }

    private List<String> getRequestState() {

        List<String> stringList = new ArrayList<>();
/*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，  6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (getArguments() != null) {
            switch (getArguments().getInt("state")) {
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
        }
        return stringList;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshHelper != null && mRefreshBinding != null && !isFirst) {
            mRefreshHelper.onDefaluteMRefresh(false);
            isFirst = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getUserVisibleHint()) {
            if (mRefreshHelper != null && mRefreshBinding != null) {
                mRefreshHelper.onDefaluteMRefresh(false);
                isFirst = true;
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
}
