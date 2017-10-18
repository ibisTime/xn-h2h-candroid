package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.OrderModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单列表
 * Created by cdkj on 2017/10/12.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderModel, BaseViewHolder> {


    public OrderListAdapter(@Nullable List<OrderModel> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, OrderModel item) {
        if (item == null) return;

        viewHolder.setText(R.id.txt_orderId, item.getCode());

        viewHolder.setText(R.id.tv_order_state, getStateString(item.getStatus()));
        viewHolder.setText(R.id.tv_quantity, "X1");
        viewHolder.setText(R.id.tv_price, MoneyUtils.showPrice(item.getAmount1()));
        BigDecimal allMoney = BigDecimalUtils.add(item.getAmount1(), item.getYunfei());//价格加运费 折扣后台已经计算
        viewHolder.setText(R.id.tv_price_all, MoneyUtils.showPrice(allMoney));
        viewHolder.setText(R.id.txt_time, DateUtil.formatStringData(item.getApplyDatetime(), DateUtil.DATE_YMD));

        if (item.getProductOrderList() != null && item.getProductOrderList().size() > 0) {
            viewHolder.setText(R.id.tv_name, item.getProductOrderList().get(0).getProductName());
            ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getProductOrderList().get(0).getProductPic()), (ImageView) viewHolder.getView(R.id.img_good));
        }

        if (canShowCancel(item.getStatus())) {
            viewHolder.setVisible(R.id.tv_cancel_order, true);
        } else {
            viewHolder.setVisible(R.id.tv_cancel_order, false);
        }

    }

    private boolean canShowCancel(String status) {

        if (TextUtils.equals("1", status) || TextUtils.equals("2", status)) {
            return true;
        }
        return false;
    }

    public String getStateString(String status) {
        /*1 待支付"，2 已支付，3 已发货，4 已收货，5 已评论，6 退款申请，7 退款失败，8 退款成功 ，91取消订单*/

        if (TextUtils.equals("1", status)) {
            return "待支付";
        }
        if (TextUtils.equals("2", status)) {
            return "待发货";
        }
        if (TextUtils.equals("3", status)) {
            return "待收货";
        }
        if (TextUtils.equals("4", status)) {
            return "已完成";
        }
        if (TextUtils.equals("5", status)) {
            return "已评论";
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

}
