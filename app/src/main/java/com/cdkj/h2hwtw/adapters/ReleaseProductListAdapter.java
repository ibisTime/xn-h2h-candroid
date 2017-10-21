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
import com.cdkj.h2hwtw.module.product.ProductDetailActivity;
import com.cdkj.h2hwtw.module.product.releasesell.ReleaseProductListFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 已发布产品列表适配器
 * Created by cdkj on 2017/10/12.
 */

public class ReleaseProductListAdapter extends BaseQuickAdapter<ProductListModel.ListBean, BaseViewHolder> {


    public ReleaseProductListAdapter(@Nullable List<ProductListModel.ListBean> data) {
        super(R.layout.item_i_release_product, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final ProductListModel.ListBean item) {
        if (item == null) return;


        ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getPic()), (ImageView) viewHolder.getView(R.id.img_goods));

        viewHolder.setText(R.id.tv_goods_name, item.getName());

        viewHolder.setGone(R.id.tv_is_new, TextUtils.equals(item.getIsNew(), "1"));
        viewHolder.setText(R.id.tv_goods_price, MoneyUtils.showPrice(item.getPrice()));

        viewHolder.setText(R.id.tv_goods_price_old, MoneyUtils.showPrice(item.getOriginalPrice()));
        viewHolder.setText(R.id.tv_type_name, item.getTypeName());
        viewHolder.setText(R.id.tv_address, item.getProvince() + " " + item.getCity() + " " + item.getArea());

        viewHolder.addOnClickListener(R.id.tv_down_product);
        viewHolder.addOnClickListener(R.id.tv_edit_product);

        if (TextUtils.equals(item.getStatus(), ReleaseProductListFragment.RELEASETYPE)) {
            viewHolder.setText(R.id.tv_down_product, "下架");
        } else {
            viewHolder.setText(R.id.tv_down_product, "上架");
        }
        /*3=已上架 4=已卖出，5=已下架 ,6=强制下架*/
        if (TextUtils.equals("5", item.getStatus()) || TextUtils.equals("3", item.getStatus())) {
            viewHolder.setGone(R.id.tv_down_product, true);
            viewHolder.setGone(R.id.tv_edit_product, true);
        } else {
            viewHolder.setGone(R.id.tv_down_product, false);
            viewHolder.setGone(R.id.tv_edit_product, false);
        }


        viewHolder.setOnClickListener(R.id.lin_goods, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.open(mContext, item.getCode());
            }
        });

    }

}
