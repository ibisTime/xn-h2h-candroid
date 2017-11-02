package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.product.ProductDetailActivity;
import com.cdkj.h2hwtw.other.ProductHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 类型界面产品分类适配器
 * Created by cdkj on 2017/10/12.
 */

public class ProductListAdapter extends BaseQuickAdapter<ProductListModel.ListBean, BaseViewHolder> {


    public ProductListAdapter(@Nullable List<ProductListModel.ListBean> data) {
        super(R.layout.item_goods, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final ProductListModel.ListBean item) {
        if (item == null) return;


        ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getPic()), (ImageView) viewHolder.getView(R.id.img_goods));

        viewHolder.setText(R.id.tv_goods_name, item.getName());

        viewHolder.setGone(R.id.tv_is_new, TextUtils.equals(item.getIsNew(), "1"));
        viewHolder.setText(R.id.tv_goods_price, MoneyUtils.showPrice(item.getPrice()));

        viewHolder.setText(R.id.tv_goods_price_old, MoneyUtils.showPrice(item.getOriginalPrice()));
        viewHolder.setText(R.id.tv_type_name, "来自" + item.getTypeName());
        viewHolder.setText(R.id.tv_address, item.getProvince() + " " + item.getCity() + " " + item.getArea());

        viewHolder.setGone(R.id.fra_isjoin, TextUtils.equals(item.getIsJoin(), "1"));//是否参加了活动 1参加 0否

        viewHolder.setText(R.id.tv_zhekou, ProductHelper.getShowDiscount(item.getDiscount()) + "");

        viewHolder.setOnClickListener(R.id.lin_goods, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.open(mContext, item.getCode());
            }
        });

    }

}
