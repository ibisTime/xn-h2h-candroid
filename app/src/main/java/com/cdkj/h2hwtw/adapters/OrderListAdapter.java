package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
import com.cdkj.h2hwtw.other.OrderHelper;
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

        viewHolder.setText(R.id.tv_quantity, "X1");
        viewHolder.setText(R.id.tv_price, MoneyUtils.getShowPriceSign(item.getAmount1()));
        BigDecimal allMoney = OrderHelper.getAllMoney(item);//价格加运费 折扣后台已经计算
        viewHolder.setText(R.id.tv_price_all, MoneyUtils.getShowPriceSign(allMoney));
        viewHolder.setText(R.id.txt_time, DateUtil.formatStringData(item.getApplyDatetime(), DateUtil.DATE_YMD));

        if (item.getProductOrderList() != null && item.getProductOrderList().size() > 0 && item.getProductOrderList().get(0) != null) {
            viewHolder.setText(R.id.tv_name, item.getProductOrderList().get(0).getProductName());
            ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getProductOrderList().get(0).getProductPic()), (ImageView) viewHolder.getView(R.id.img_good));
        }


        viewHolder.setText(R.id.tv_order_state, OrderHelper.getOrderDoStateString(item.getStatus()));
        viewHolder.setText(R.id.tv_cancel_order, getStateCancelString(item.getStatus()));

        viewHolder.setGone(R.id.tv_cancel_order, canShowCancel(item.getStatus()));
        viewHolder.setGone(R.id.tv_order_state, canShowState(item.getStatus()));

        viewHolder.addOnClickListener(R.id.tv_order_state);
        viewHolder.addOnClickListener(R.id.tv_cancel_order);

    }

    public String getStateCancelString(String status) {
        if (TextUtils.equals("1", status)) {
            return "取消订单";
        }
        if (TextUtils.equals("2", status)) {
            return "申请退款";
        }
        if (TextUtils.equals("4", status)) {
            return "前往评价";
        }
        if (TextUtils.equals("5", status)) {
            return "查看评价";
        }
        return "";
    }

    /**
     * 能否显示取消按钮
     *
     * @param status
     * @return
     */
    public boolean canShowCancel(String status) {
        if (TextUtils.equals("1", status)) {
            return true;
        }
        if (TextUtils.equals("2", status)) {
            return true;
        }
        if (TextUtils.equals("4", status)) {
            return true;
        }
        if (TextUtils.equals("5", status)) {
            return true;
        }
        return false;
    }

    /**
     * 能否显示状态按钮
     *
     * @param status
     * @return
     */
    private boolean canShowState(String status) {
//        if (TextUtils.equals("2", status)) {
//            return false;
//        }
        return true;
    }


}
