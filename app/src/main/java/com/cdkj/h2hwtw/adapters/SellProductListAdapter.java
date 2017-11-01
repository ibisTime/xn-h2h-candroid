package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.OrderModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.product.ProductDetailActivity;
import com.cdkj.h2hwtw.module.product.releasesell.ReleaseProductListFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 已卖出产品列表适配器
 * Created by cdkj on 2017/10/12.
 */

public class SellProductListAdapter extends BaseQuickAdapter<OrderModel, BaseViewHolder> {


    public SellProductListAdapter(@Nullable List<OrderModel> data) {
        super(R.layout.item_i_sell_product, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final OrderModel item) {
        if (item == null) return;


        viewHolder.setText(R.id.txt_orderId, item.getCode());

        viewHolder.setText(R.id.tv_order_state, getStateString(item.getStatus()));
        viewHolder.setText(R.id.tv_cancel_order, getDoStateString(item.getStatus()));

        viewHolder.setGone(R.id.img_sell_state, canShowSellSing(item.getStatus()));
        viewHolder.setGone(R.id.tv_cancel_order, canShowDoBtn(item.getStatus()));

        viewHolder.setText(R.id.tv_price, MoneyUtils.getShowPriceSign(item.getAmount1()));
        BigDecimal allMoney = getAllMoney(item);//价格加运费 折扣后台已经计算
        viewHolder.setText(R.id.tv_price_all, MoneyUtils.getShowPriceSign(allMoney));
        viewHolder.setText(R.id.txt_time, DateUtil.formatStringData(item.getApplyDatetime(), DateUtil.DATE_YMD));

        if (item.getProductOrderList() != null && item.getProductOrderList().size() > 0 && item.getProductOrderList().get(0) != null) {
            viewHolder.setText(R.id.tv_name, item.getProductOrderList().get(0).getProductName());
            ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getProductOrderList().get(0).getProductPic()), (ImageView) viewHolder.getView(R.id.img_good));
        }

        viewHolder.addOnClickListener(R.id.tv_cancel_order);


    }

    public BigDecimal getAllMoney(OrderModel item) {
        return BigDecimalUtils.add(item.getAmount1(), item.getYunfei());
    }


    /**
     * 根据状态显示是否已卖出图标
     *
     * @return
     */
    public boolean canShowSellSing(String status) {

        if (TextUtils.equals(status, "3")
                || TextUtils.equals(status, "4") || TextUtils.equals(status, "5")) {
            return true;
        }

        return false;
    }

    /**
     * 根据状态显示是否状态相应操作
     *
     * @return
     */
    public boolean canShowDoBtn(String status) {

        if (TextUtils.equals(status, "1") || TextUtils.equals(status, "2") || TextUtils.equals(status, "6")
                || TextUtils.equals(status, "5")) {
            return true;
        }

        return false;
    }

    /**
     * 根据状态显示是否状态相应操作
     *
     * @return
     */
    public boolean canShowStateBtn(String status) {

        if (TextUtils.equals(status, "2")) {
            return true;
        }

        return false;
    }

    /**
     * 获取相应操作状态名
     *
     * @param status
     * @return
     */
    public String getDoStateString(String status) {
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


    public String getStateString(String status) {
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


}
