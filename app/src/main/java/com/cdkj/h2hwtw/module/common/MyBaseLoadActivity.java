package com.cdkj.h2hwtw.module.common;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;


/**
 * 可设置Title背景
 */
public abstract class MyBaseLoadActivity extends AbsBaseLoadActivity {

    public abstract int getLoadTitleBg();//加载什么颜色的背景 //0 蓝色 白色

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBG();
    }

    public void setBG() {
        switch (getLoadTitleBg()) {
            case 1:
                mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                mBaseBinding.titleView.setLeftTitleColor(R.color.text_black_app);
                mBaseBinding.titleView.setRightTitleColor(R.color.text_black_app);
                mBaseBinding.titleView.setMidTitleColor(R.color.text_black_app);
                break;
            default:
                mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.title_bg));
                mBaseBinding.titleView.setLeftTitleColor(R.color.white);
                mBaseBinding.titleView.setRightTitleColor(R.color.white);
                mBaseBinding.titleView.setMidTitleColor(R.color.white);
        }

    }


    public void setLeftImg() {
        switch (getLoadTitleBg()) {
            case 1:
                mBaseBinding.titleView.setLeftImg(R.drawable.back_img);
                break;
            default:
                mBaseBinding.titleView.setLeftImg(R.drawable.back_img);
        }
    }

}
