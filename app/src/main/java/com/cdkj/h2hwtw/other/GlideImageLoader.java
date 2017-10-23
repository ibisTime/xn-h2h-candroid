package com.cdkj.h2hwtw.other;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by 李先俊 on 2017/6/10.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(MyCdConfig.QINIUURL + path).error(com.cdkj.baselibrary.R.drawable.default_pic).into(imageView);
    }
}