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
//        super(R.layout.item_i_sell_product, data);
        super(R.layout.item_order, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final OrderModel item) {
        if (item == null) return;

        viewHolder.setText(R.id.txt_orderId, item.getCode());

        viewHolder.setText(R.id.tv_quantity, "X1");
        viewHolder.setText(R.id.tv_price, MoneyUtils.getShowPriceSign(item.getAmount1()));
        BigDecimal allMoney = getAllMoney(item);//价格加运费 折扣后台已经计算
        viewHolder.setText(R.id.tv_price_all, MoneyUtils.getShowPriceSign(allMoney));
        viewHolder.setText(R.id.txt_time, DateUtil.formatStringData(item.getApplyDatetime(), DateUtil.DATE_YMD));

        if (item.getProductOrderList() != null && item.getProductOrderList().size() > 0 && item.getProductOrderList().get(0) != null) {
            viewHolder.setText(R.id.tv_name, item.getProductOrderList().get(0).getProductName());
            ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getProductOrderList().get(0).getProductPic()), (ImageView) viewHolder.getView(R.id.img_good));
        }


        viewHolder.setGone(R.id.tv_order_state, false);
        viewHolder.setGone(R.id.tv_cancel_order, false);

    }

    public BigDecimal getAllMoney(OrderModel item) {
        return BigDecimalUtils.add(item.getAmount1(), item.getYunfei());
    }

}
