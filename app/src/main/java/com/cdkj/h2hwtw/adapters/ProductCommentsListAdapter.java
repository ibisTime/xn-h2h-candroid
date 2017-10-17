package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.CommentsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 发布页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class ProductCommentsListAdapter extends BaseQuickAdapter<CommentsModel, BaseViewHolder> {

    public ProductCommentsListAdapter(@Nullable List<CommentsModel> data) {
        super(R.layout.item_comments, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CommentsModel item) {
        if (item == null) return;

        ImgUtils.loadLogo(mContext, MyCdConfig.QINIUURL + item.getPhoto(), (ImageView) viewHolder.getView(R.id.img_user_logo));
        viewHolder.setText(R.id.tv_comments_content, item.getContent());
        viewHolder.setText(R.id.tv_comments_name, item.getNickname());


    }

}
