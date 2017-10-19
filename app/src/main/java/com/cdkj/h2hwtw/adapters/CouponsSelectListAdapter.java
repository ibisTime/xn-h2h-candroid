package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.CouponsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 发布页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class CouponsSelectListAdapter extends BaseQuickAdapter<CouponsModel, BaseViewHolder> {

    private int selectPostion = -1;

    public CouponsSelectListAdapter(@Nullable List<CouponsModel> data) {
        super(R.layout.item_coupons_select, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CouponsModel item) {
        if (item == null) return;

        viewHolder.setText(R.id.tv_coupon_name, "减免" + MoneyUtils.showPrice(item.getParValue()) + "元");
        CheckBox checkBox = viewHolder.getView(R.id.checkbox_coupon_select);
        checkBox.setChecked(selectPostion == viewHolder.getLayoutPosition());
    }

    public void setSelectPostion(int selectPostion) {
        this.selectPostion = selectPostion;
        notifyDataSetChanged();
    }

    public CouponsModel getSelectItem() {
        if (selectPostion >= 0) {
            return getItem(selectPostion);
        }

        return null;
    }


}
