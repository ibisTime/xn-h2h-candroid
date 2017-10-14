package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.ReleasePagePhotoModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

/**
 * 发布页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressModel, BaseViewHolder> {

    public AddressListAdapter(@Nullable List<AddressModel> data) {
        super(R.layout.item_address, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, AddressModel item) {
        if (item == null) return;
        TextView txtName = viewHolder.getView(R.id.txt_name);
        TextView txtPhone = viewHolder.getView(R.id.txt_phone);
        TextView txtAddress = viewHolder.getView(R.id.txt_address);
        ImageView imgChoose = viewHolder.getView(R.id.img_choose);


        if (item.isDefaultAddress()) {
            imgChoose.setBackgroundResource(R.drawable.check_on);
        } else {
            imgChoose.setBackgroundResource(R.drawable.check_off);
        }

        txtName.setText(item.getAddressee());
        txtPhone.setText(item.getMobile());
        txtAddress.setText(item.getProvince() + " " + item.getCity() + " " + item.getDistrict() + "" + item.getDetailAddress());

        viewHolder.addOnClickListener(R.id.layout_delete);
        viewHolder.addOnClickListener(R.id.layout_edit);
        viewHolder.addOnClickListener(R.id.real_address);


    }

}
