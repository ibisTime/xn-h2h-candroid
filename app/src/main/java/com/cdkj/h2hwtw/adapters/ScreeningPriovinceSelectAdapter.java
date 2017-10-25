package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.model.cityInfo.Province;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 省份筛选适配器
 * Created by cdkj on 2017/10/12.
 */

public class ScreeningPriovinceSelectAdapter extends BaseQuickAdapter<Province, BaseViewHolder> {

    private int selectPosition = -1;


    public ScreeningPriovinceSelectAdapter(@Nullable List<Province> data) {
        super(R.layout.item_textview_type, data);
        selectPosition = -1;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Province item) {
        if (item == null) return;
        TextView tv = viewHolder.getView(R.id.tv);
        tv.setText(item.getName());

        if (selectPosition == viewHolder.getLayoutPosition()) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
            tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray_app));
            tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_gray_app));
        }

    }

    public void setSelect(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }


}
