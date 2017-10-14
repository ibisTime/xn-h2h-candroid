package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.BillModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 我的账单流水界面
 * Created by cdkj on 2017/10/12.
 */

public class MyAccountBillAdapter extends BaseQuickAdapter<BillModel, BaseViewHolder> {

    public MyAccountBillAdapter(@Nullable List<BillModel> data) {
        super(R.layout.item_account_bill, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, BillModel item) {
        if (item == null) return;


        TextView tvprice = helper.getView(R.id.txt_price);
        TextView tvInfo = helper.getView(R.id.txt_info);
        TextView txtDate = helper.getView(R.id.txt_date);
        TextView txtTime = helper.getView(R.id.txt_time);
        ImageView imgType = helper.getView(R.id.img_type);


        if (BigDecimalUtils.intValue(item.getTransAmount()) > 0) {
            tvprice.setTextColor(ContextCompat.getColor(mContext, R.color.bill_color_orange));
            ImgUtils.loadImg(mContext, R.drawable.bill_get, imgType);
            tvprice.setText("+" + MoneyUtils.showPrice(item.getTransAmount()));
        } else {
            tvprice.setTextColor(ContextCompat.getColor(mContext, R.color.bill_color_orange));
            ImgUtils.loadImg(mContext, R.drawable.bill_pay, imgType);
            tvprice.setText(MoneyUtils.showPrice(item.getTransAmount()));
        }

        tvInfo.setText(item.getBizNote());

        if (!TextUtils.isEmpty(item.getCreateDatetime())) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd日");
            Date date = new Date(item.getCreateDatetime());
            txtDate.setText(dateFormat.format(date));
            txtTime.setText(timeFormat.format(date));
        }


    }

}
