package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.SignDatetimeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 签到日历
 * Created by cdkj on 2017/10/12.
 */

public class SigninDateAdapter extends BaseQuickAdapter<Date, BaseViewHolder> {

    private boolean isStartShowSignData;

    private List<SignDatetimeModel> datetimeModels;

    public SigninDateAdapter(@Nullable List<Date> data) {
        super(R.layout.item_date, data);
        isStartShowSignData = false;
        datetimeModels = new ArrayList<>();
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Date item) {
        if (item == null) {
            return;
        }
        viewHolder.setText(R.id.tv_data_sign, DateUtil.format(item, "dd"));

        if (isStartShowSignData && isShowSignBgAndImg(item)) {
            viewHolder.setBackgroundRes(R.id.fra_date_bg, R.drawable.sign_in_date_bg);
            viewHolder.setVisible(R.id.img_is_sign, true);
        } else {
            viewHolder.setBackgroundColor(R.id.fra_date_bg, ContextCompat.getColor(mContext, R.color.white));
            viewHolder.setVisible(R.id.img_is_sign, false);
        }

    }

    public void setSignData(List<SignDatetimeModel> das) {
        if (das == null || das.size() == 0) return;
        isStartShowSignData = true;
        datetimeModels.clear();
        datetimeModels.addAll(das);
        notifyDataSetChanged();
    }


    public boolean isShowSignBgAndImg(Date posiDate) {
        for (SignDatetimeModel datetimeModel : datetimeModels) {
            if (datetimeModel == null || TextUtils.isEmpty(datetimeModel.getSignDatetime())) {
                continue;
            }

            if (DateUtil.inSameDay(posiDate, new Date(datetimeModel.getSignDatetime()))) {
                return true;
            }
        }
        return false;
    }

}
