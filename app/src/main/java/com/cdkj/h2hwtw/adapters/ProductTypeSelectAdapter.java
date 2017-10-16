package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 类型界面产品分类适配器
 * Created by cdkj on 2017/10/12.
 */

public class ProductTypeSelectAdapter extends BaseQuickAdapter<ProductTypeModel, BaseViewHolder> {

    private int selectPosition;

    private boolean isLeft;

    private boolean isFirstTouch;

    public void setFirstTouch(boolean firstTouch) {
        isFirstTouch = firstTouch;
    }

    public ProductTypeSelectAdapter(@Nullable List<ProductTypeModel> data, boolean isLeft) {
        super(R.layout.item_textview_type, data);
        this.isLeft = isLeft;
        isFirstTouch = true;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, ProductTypeModel item) {
        if (item == null) return;
        TextView tv = viewHolder.getView(R.id.tv);
        tv.setText(item.getName());

        if (isLeft) {
            if (selectPosition == viewHolder.getLayoutPosition() && !isFirstTouch) {
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
            } else {
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_gray_app));
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray_app));
            }
        } else {
            if (selectPosition == viewHolder.getLayoutPosition() && selectPosition != 0) {
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_gray_app));
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
            } else {
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
            }
        }

    }

    public void setSelect(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }


}
