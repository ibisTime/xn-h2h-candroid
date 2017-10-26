package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.h2hwtw.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
import java.util.List;

/**
 * 签到日历
 * Created by cdkj on 2017/10/12.
 */

public class SigninDateAdapter extends BaseQuickAdapter<Date, BaseViewHolder> {

    public SigninDateAdapter(@Nullable List<Date> data) {
        super(R.layout.item_date, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Date item) {
        if (item == null) {
            return;
        }
        viewHolder.setText(R.id.tv_data_sign, DateUtil.format(item, "dd"));

    }

}
