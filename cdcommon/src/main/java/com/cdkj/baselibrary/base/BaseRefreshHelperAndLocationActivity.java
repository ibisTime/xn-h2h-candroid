package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.interfaces.RefreshInterface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 实现下拉刷新 上拉加载 分页逻辑
 * Created by 李先俊 on 2017/7/19.
 */
public abstract class BaseRefreshHelperAndLocationActivity<T> extends BaseLocationActivity implements RefreshInterface<T> {

    protected LayoutCommonRecyclerRefreshBinding mRefreshBinding;
    protected RefreshHelper mRefreshHelper;


    //第一次加载
    protected abstract void onInit(Bundle savedInstanceState);

    protected abstract String getErrorInfo();

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        if (mRefreshBinding == null) return null;
        return mRefreshBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        if (mRefreshBinding == null) return null;
        return mRefreshBinding.rv;
    }

    @Override
    public boolean loadDeflutEmptyView() {
        return true;
    }

    @Override
    public EmptyViewBinding getEmptyViewBindin() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.empty_view, null, false);
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

    @Override
    public View addMainView() {
        mRefreshBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_common_recycler_refresh, null, false);

        return mRefreshBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initRefreshHelper(1, 10);
        onInit(savedInstanceState);
    }

    private void initRefreshHelper(int start, int limit) {
        mRefreshHelper = new RefreshHelper(this, this);
        mRefreshHelper.setErrorInfo(getErrorInfo());
        mRefreshHelper.init(start, limit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.clear();
        }
    }
}
