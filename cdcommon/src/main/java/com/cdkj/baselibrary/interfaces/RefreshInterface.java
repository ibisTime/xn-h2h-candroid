package com.cdkj.baselibrary.interfaces;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * Created by 李先俊 on 2017/8/8.
 */
// TODO 去除去除EmptyViewBinding
public interface RefreshInterface<T> {


    SmartRefreshLayout getRefreshLayout();

    RecyclerView getRecyclerView();

    BaseQuickAdapter getAdapter(List<T> listData);

    boolean loadDeflutEmptyView();

    EmptyViewBinding getEmptyViewBindin();

    View getEmptyView();

    void onRefresh(int pageindex, int limit);

    void onLoadMore(int pageindex, int limit);

    void getListDataRequest(int pageindex, int limit, boolean isShowDialog);

}
