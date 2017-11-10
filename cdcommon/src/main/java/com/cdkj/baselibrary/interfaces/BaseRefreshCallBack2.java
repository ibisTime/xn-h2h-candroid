package com.cdkj.baselibrary.interfaces;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;

/**
 * 刷新方法回调
 * Created by cdkj on 2017/10/17.
 */

public abstract class BaseRefreshCallBack2<T> implements RefreshInterface2 {

    private EmptyViewBinding emptyViewBinding;

    @Override
    public View getEmptyView(Activity context) {
        emptyViewBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.empty_view, null, false);
        return emptyViewBinding.getRoot();
    }

    @Override
    public void showErrorView(String errorMsg) {
        emptyViewBinding.tv.setText(errorMsg);
    }

    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }

    /**
     * 设置错误情况图片
     *
     * @param img
     */
    public void setErrorImg(int img) {
        emptyViewBinding.img.setImageResource(img);
    }

}
