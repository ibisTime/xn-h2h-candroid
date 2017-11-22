package com.cdkj.baselibrary.interfaces;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * 根据需求自己定义
 * Created by 李先俊 on 2017/8/8.
 */
public interface RefreshInterface2<T> {

    SmartRefreshLayout getRefreshLayout();

    RecyclerView getRecyclerView();

    BaseQuickAdapter getAdapter(List<T> listData);

    View getEmptyView(Activity context);

    void showErrorState(String errorMsg,@DrawableRes int errorImg);

    void showEmptyState(String errorMsg,@DrawableRes int errorImg);

    void onRefresh(int pageindex, int limit);

    void onLoadMore(int pageindex, int limit);

    void getListDataRequest(int pageindex, int limit, boolean isShowDialog);

}
