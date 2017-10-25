package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.cityInfo.City;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 省份筛选适配器
 * Created by cdkj on 2017/10/12.
 */

public class ScreeningCitySelectAdapter extends BaseQuickAdapter<City, BaseViewHolder> {

    private int selectPosition = -1;


    public ScreeningCitySelectAdapter(@Nullable List<City> data) {
        super(R.layout.item_textview_type, data);
        selectPosition = -1;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, City item) {
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
