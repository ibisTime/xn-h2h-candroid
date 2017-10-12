package com.cdkj.h2hwtw.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ReleasePagePhotoModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

/**
 * 发布页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class ReleasePagePhotoAdapter extends BaseQuickAdapter<ReleasePagePhotoModel, BaseViewHolder> {

    public ReleasePagePhotoAdapter(@Nullable List<ReleasePagePhotoModel> data) {
        super(R.layout.item_release_photo, data);
        setMultiTypeDelegate(new MultiTypeDelegate<ReleasePagePhotoModel>() {
            @Override
            protected int getItemType(ReleasePagePhotoModel releasePagePhotoModel) {
                if (releasePagePhotoModel.isHeart()) {
                    return 0;
                }
                return 1;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(0, R.layout.layout_realease_heard)
                .registerItemType(1, R.layout.item_release_photo);
    }


    @Override
    protected void convert(BaseViewHolder helper, ReleasePagePhotoModel item) {

        switch (helper.getItemViewType()) {
            case 0:
                helper.addOnClickListener(R.id.fra_add_photo);//添加点击事件
                break;
            case 1:
                ImgUtils.loadImg(mContext, item.getImgPath(), (ImageView) helper.getView(R.id.img_photo));
                helper.addOnClickListener(R.id.img_delete);//添加点击事件
                if (helper.getLayoutPosition() == 1) {
                    helper.setVisible(R.id.tv_main_img, true);
                } else {
                    helper.setVisible(R.id.tv_main_img, false);
                }
                break;
        }


    }

}
