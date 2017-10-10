package com.cdkj.h2hwtw.module.common;

import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;


/**
 * 可设置Title背景
 */
public abstract class MyBaseLoadActivity extends AbsBaseLoadActivity {

    private int loadType;

    public void setBGBlue() {
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.title_bg));
        mBaseBinding.titleView.setLeftTitleColor(R.color.white);
        mBaseBinding.titleView.setRightTitleColor(R.color.white);
        mBaseBinding.titleView.setMidTitleColor(R.color.white);
        loadType = 0;
    }

    public void setBGWhite() {
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mBaseBinding.titleView.setLeftTitleColor(R.color.text_black_app);
        mBaseBinding.titleView.setRightTitleColor(R.color.text_black_app);
        mBaseBinding.titleView.setMidTitleColor(R.color.text_black_app);
        loadType = 1;
    }

    public void setLeftImg() {
        if (loadType == 0) {
            mBaseBinding.titleView.setLeftImg(R.drawable.back_img);
        } else {
            mBaseBinding.titleView.setLeftImg(R.drawable.back_img);
        }
    }

}
