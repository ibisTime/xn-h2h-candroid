package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.cityInfo.City;
import com.cdkj.h2hwtw.model.cityInfo.District;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 地区筛选适配器
 * Created by cdkj on 2017/10/12.
 */

public class ScreeningDIstrictSelectAdapter extends BaseQuickAdapter<District, BaseViewHolder> {

    private int selectPosition = -1;


    public ScreeningDIstrictSelectAdapter(@Nullable List<District> data) {
        super(R.layout.item_textview_type, data);
        selectPosition = -1;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, District item) {
        if (item == null) return;
        TextView tv = viewHolder.getView(R.id.tv);
        if (selectPosition == viewHolder.getLayoutPosition()) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
        } else {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray_app));
        }
        tv.setText(item.getName());

    }

    public void setSelect(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }


}
