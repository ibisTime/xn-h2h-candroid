package com.cdkj.h2hwtw.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.MsgListModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.product.ProductDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 交易圈适配器
 * Created by cdkj on 2017/10/12.
 */

public class BuyCircleProductListAdapter extends BaseQuickAdapter<ProductListModel.ListBean, BaseViewHolder> {


    private Activity mActivity;

    public BuyCircleProductListAdapter(Activity activity, @Nullable List<ProductListModel.ListBean> data) {
        super(R.layout.item_buy_circle, data);
        this.mActivity = activity;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final ProductListModel.ListBean item) {
        if (item == null) return;


        ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + item.getPhoto(), (ImageView) viewHolder.getView(R.id.img_user_logo));

        viewHolder.setText(R.id.tv_product_name, item.getName());
        viewHolder.setText(R.id.tv_user_name, item.getNickName());
        viewHolder.setText(R.id.tv_from_info, "来自" + item.getTypeName());
        viewHolder.setText(R.id.tv_price, "来自" + item.getTypeName());
        viewHolder.setText(R.id.tv_price, MoneyUtils.showPrice(item.getPrice()));
        viewHolder.setText(R.id.tv_fans_num, item.getTotalInteract() + "");
        viewHolder.setText(R.id.tv_comments_num, item.getTotalComment() + "");
        viewHolder.setText(R.id.tv_info, item.getCity() + " | " + DateUtil.getLoginDataInfo(item.getLoginLog()));


        RecyclerView recyclerView = viewHolder.getView(R.id.recycler_img);

        viewHolder.addOnClickListener(R.id.recycler_img);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(getImgAdapter(StringUtils.splitAsPicList(item.getPic())));

    }

    public RecyclerView.Adapter getImgAdapter(List<String> picList) {
        return new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_buy_circle_img, picList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                if (item == null) return;
                ImgUtils.loadImg(mActivity, MyCdConfig.QINIUURL + item, (ImageView) helper.getView(R.id.img_buy_circle_));
            }
        };
    }
}
