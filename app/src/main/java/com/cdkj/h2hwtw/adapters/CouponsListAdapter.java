package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.CouponsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 发布页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class CouponsListAdapter extends BaseQuickAdapter<CouponsModel, BaseViewHolder> {


    public CouponsListAdapter(@Nullable List<CouponsModel> data) {
        super(R.layout.item_coupons, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CouponsModel item) {
        if (item == null) return;

        viewHolder.setText(R.id.tv_use_can, "减免" + MoneyUtils.showPrice(item.getParValue()) + "元");
        viewHolder.setText(R.id.tv_money, MoneyUtils.showPrice(item.getParValue()));
        viewHolder.setText(R.id.tv_date, "截至日期:" + DateUtil.formatStringData(item.getEndDatetime(), DateUtil.DATE_YMD));

        LinearLayout linearLayout = viewHolder.getView(R.id.layout_coupons_bg);

        if (TextUtils.equals(item.getStatus(), "0")) {
            linearLayout.setBackgroundResource(R.drawable.couponsb_bg);
        } else {
            linearLayout.setBackgroundResource(R.drawable.coupons_cant_use);
        }

    }


}
