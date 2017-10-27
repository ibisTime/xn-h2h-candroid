package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.QiNiuUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.FansModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 粉丝列表
 * Created by cdkj on 2017/10/12.
 */

public class FansListAdapter extends BaseQuickAdapter<FansModel, BaseViewHolder> {

    public FansListAdapter(@Nullable List<FansModel> data) {
        super(R.layout.item_fans, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, FansModel item) {
        if (item == null) return;

        ImgUtils.loadLogo(mContext, MyCdConfig.QINIUURL + item.getPhoto(), (ImageView) viewHolder.getView(R.id.img_user_logo));
        viewHolder.setText(R.id.tv_user_name, item.getNickname());
    }

}
