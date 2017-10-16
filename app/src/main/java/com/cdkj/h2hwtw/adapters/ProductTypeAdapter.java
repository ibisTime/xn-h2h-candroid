package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 类型界面产品分类适配器
 * Created by cdkj on 2017/10/12.
 */

public class ProductTypeAdapter extends BaseQuickAdapter<ProductTypeModel, BaseViewHolder> {


    public ProductTypeAdapter(@Nullable List<ProductTypeModel> data) {
        super(R.layout.item_type_menu, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, ProductTypeModel item) {
        if (item == null) return;
        TextView tv = viewHolder.getView(R.id.tv_menu);
        ImageView img = viewHolder.getView(R.id.img_menu);

        tv.setText(item.getName());
        ImgUtils.loadImgNoPlaceholder(mContext, MyCdConfig.QINIUURL + item.getPic(), img);

    }

}
