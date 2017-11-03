package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.WantProductModel;
import com.cdkj.h2hwtw.module.product.ProductDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 收藏适配器
 * Created by cdkj on 2017/10/12.
 */

public class WantProductListAdapter extends BaseQuickAdapter<WantProductModel, BaseViewHolder> {


    public WantProductListAdapter(@Nullable List<WantProductModel> data) {
        super(R.layout.item_goods, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final WantProductModel item) {
        if (item == null || item.getProduct() == null) return;


        ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getProduct().getPic()), (ImageView) viewHolder.getView(R.id.img_goods));

        viewHolder.setText(R.id.tv_goods_name, item.getProduct().getName());

        viewHolder.setGone(R.id.tv_is_new, TextUtils.equals(item.getProduct().getIsNew(), "1"));
        viewHolder.setText(R.id.tv_goods_price, MoneyUtils.showPrice(item.getProduct().getPrice()));

        viewHolder.setText(R.id.tv_goods_price_old, MoneyUtils.showPrice(item.getProduct().getOriginalPrice()));
        viewHolder.setText(R.id.tv_type_name, "来自"+item.getProduct().getTypeName());
        viewHolder.setText(R.id.tv_address, item.getProduct().getProvince() + " " + item.getProduct().getCity() + " " + item.getProduct().getArea());

        viewHolder.setGone(R.id.fra_isjoin, TextUtils.equals(item.getProduct().getIsJoin(), "1"));//是否参加了活动 1参加 0否

        viewHolder.setOnClickListener(R.id.lin_goods, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.open(mContext, item.getProduct().getCode());
            }
        });

    }

}
