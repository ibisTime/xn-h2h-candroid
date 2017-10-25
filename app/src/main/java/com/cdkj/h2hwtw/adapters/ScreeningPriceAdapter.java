package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.ScreeningPrictModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 发布页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class ScreeningPriceAdapter extends BaseQuickAdapter<ScreeningPrictModel, BaseViewHolder> {

    private int selectPosition = -1;

    public ScreeningPriceAdapter(@Nullable List<ScreeningPrictModel> data) {
        super(R.layout.item_screening_menu, data);
        selectPosition = -1;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ScreeningPrictModel item) {
        if (item == null) return;

        TextView tv = viewHolder.getView(R.id.tv_menu);
        tv.setText(item.getName());

        if (selectPosition == viewHolder.getLayoutPosition()) {
            tv.setBackgroundResource(R.drawable.screening_bg_blue);
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.screening_text_selet));
        } else {
            tv.setBackgroundResource(R.drawable.screening_bg_gray);
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
