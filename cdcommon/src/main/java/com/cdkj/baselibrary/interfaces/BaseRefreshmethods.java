package com.cdkj.baselibrary.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * Created by 李先俊 on 2017/10/17.
 */

public abstract class BaseRefreshmethods<T> implements RefreshInterface {


    @Override
    public boolean loadDeflutEmptyView() {
        return false;
    }

    @Override
    public EmptyViewBinding getEmptyViewBindin() {
        return null;
    }

    @Override
    public View getEmptyView() {
        return null;
    }

    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }

}
