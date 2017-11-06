package com.cdkj.h2hwtw.other;

import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.h2hwtw.MyApplication;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.OrderModel;

import java.math.BigDecimal;

/**
 * 订单状态辅助
 * Created by cdkj on 2017/11/6.
 */

public class OrderHelper {

         /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/


    public static final String OrderStateWaitePay = "1";
    public static final String OrderStatePayDone = "2";
    public static final String OrderStateWaiteGet = "3";
    public static final String OrderStateGetDone = "4";
    public static final String OrderStateCommentsDone = "5";
    public static final String OrderStateBackPay = "6";
    public static final String OrderStateBackPayFall = "7";
    public static final String OrderStateBackPaySucc = "8";
    public static final String OrderStateCancel = "91";

    /**
     * 获取已卖出订单状态
     *
     * @param status
     * @return
     */
    public static String getSellStateString(String status) {
        /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (TextUtils.equals("1", status)) {
            return "待支付";
        }
        if (TextUtils.equals("2", status)) {
            return "已支付";
        }
        if (TextUtils.equals("3", status)) {
            return "已发货";
        }
        if (TextUtils.equals("4", status)) {
            return "待评价";
        }
        if (TextUtils.equals("5", status)) {
            return "已评论";
        }
        if (TextUtils.equals("6", status)) {
            return "退款申请";
        }
        if (TextUtils.equals("7", status)) {
            return "退款失败";
        }
        if (TextUtils.equals("8", status)) {
            return "退款成功";
        }
        if (TextUtils.equals("91", status)) {
            return "订单取消";
        }
        return "";
    }


    /**
     * 获取订单操作按钮状态
     *
     * @param status
     * @return
     */
    public static String getOrderDoStateString(String status) {
        /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (TextUtils.equals("1", status)) {
            return "立即支付";
        }
        if (TextUtils.equals("2", status)) {
            return "催货";
        }
        if (TextUtils.equals("3", status)) {
            return "确认收货";
        }
        if (TextUtils.equals("4", status)) {
            return "待评价";
        }
        if (TextUtils.equals("5", status)) {
            return "已完成";
        }
        if (TextUtils.equals("6", status)) {
            return "退款中";
        }
        if (TextUtils.equals("7", status)) {
            return "退款失败";
        }
        if (TextUtils.equals("8", status)) {
            return "已退款";
        }
        if (TextUtils.equals("91", status)) {
            return "已取消";
        }
        return "";
    }


    public static String getOrderStateString(String status) {
        /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (TextUtils.equals("1", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_1);
        }
        if (TextUtils.equals("2", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_2);
        }
        if (TextUtils.equals("3", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_3);
        }
        if (TextUtils.equals("4", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_4);
        }
        if (TextUtils.equals("5", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_5);
        }
        if (TextUtils.equals("6", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_6);
        }
        if (TextUtils.equals("7", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_7);
        }
        if (TextUtils.equals("8", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_8);
        }
        if (TextUtils.equals("91", status)) {
            return MyApplication.getInstance().getString(R.string.order_state_91);
        }
        return "暂无";
    }


    /**
     * 获取相应操作状态名
     *
     * @param status
     * @return
     */
    public static String getDoStateString(String status) {
        /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (TextUtils.equals(status, "1")) {
            return "取消订单";
        }
        if (TextUtils.equals(status, "2")) {
            return "发货";
        }
        if (TextUtils.equals(status, "6")) {
            return "确认退款";
        }
        if (TextUtils.equals(status, "5")) {
            return "查看评价";
        }

        return "";
    }


    /**
     * 根据状态显示是否已卖出图标
     *
     * @return
     */
    public static boolean canShowSellSing(String status) {

        if (TextUtils.equals(status, "3") || TextUtils.equals(status, "4") || TextUtils.equals(status, "5")) {
            return true;
        }

        return false;
    }

    /**
     * 根据状态显示是否状态相应操作
     *
     * @return
     */
    public static boolean canShowDoBtn(String status) {

        if (TextUtils.equals(status, "1") || TextUtils.equals(status, "2") || TextUtils.equals(status, "6")
                || TextUtils.equals(status, "5")) {
            return true;
        }

        return false;
    }


    /**
     * //价格加运费 折扣后台已经计算
     *
     * @param item
     * @return
     */
    public static BigDecimal getAllMoney(OrderModel item) {
        return BigDecimalUtils.add(item.getAmount1(), item.getYunfei());
    }


    /**
     * 是不是待支付订单
     *
     * @param state
     * @return
     */
    public static boolean isWaitePayOrder(String state) {
        return TextUtils.equals(OrderStateWaitePay, state);
    }

    /**
     * 是不是已支付订单
     *
     * @param state
     * @return
     */
    public static boolean isPayDoneOrder(String state) {
        return TextUtils.equals(OrderStatePayDone, state);
    }

    /**
     * 是不是已发货订单
     *
     * @param state
     * @return
     */
    public static boolean isSendDoneOrder(String state) {
        return TextUtils.equals(OrderStateWaiteGet, state);
    }

    /**
     * 是不是已收货订单
     *
     * @param state
     * @return
     */
    public static boolean isGetDoneOrder(String state) {
        return TextUtils.equals(OrderStateGetDone, state);
    }
    /**
     * 是不是已评论订单
     *
     * @param state
     * @return
     */
    public static boolean isCommentsDoneOrder(String state) {
        return TextUtils.equals(OrderStateCommentsDone, state);
    }
    /**
     * 是不是申请退款订单
     *
     * @param state
     * @return
     */
    public static boolean isBackPayOrder(String state) {
        return TextUtils.equals(OrderStateBackPay, state);
    }

}
