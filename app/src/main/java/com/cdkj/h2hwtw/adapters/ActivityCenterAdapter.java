package com.cdkj.h2hwtw.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ActivityCenterModel;
import com.cdkj.h2hwtw.model.AddressModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 活动中心适配器
 * Created by cdkj on 2017/10/12.
 */

public class ActivityCenterAdapter extends BaseQuickAdapter<ActivityCenterModel, BaseViewHolder> {

    public ActivityCenterAdapter(@Nullable List<ActivityCenterModel> data) {
        super(R.layout.item_activity_center, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, ActivityCenterModel item) {
        if (item == null) return;

        ImgUtils.loadImg(mContext, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(item.getAdvPic()), (ImageView) viewHolder.getView(R.id.img_activity));

        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getEndDatetime(), DateUtil.DATE_YMD));

    }

}
