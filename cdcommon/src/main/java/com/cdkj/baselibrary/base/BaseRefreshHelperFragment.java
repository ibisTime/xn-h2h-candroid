package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.databinding.FragmentRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.interfaces.RefreshInterface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 实现下拉刷新 上拉加载 分页逻辑
 * Created by 李先俊 on 2017/7/19.
 */

public abstract class BaseRefreshHelperFragment<T> extends BaseLazyFragment implements RefreshInterface<T> {

    protected FragmentRecyclerRefreshBinding mRefreshBinding;
    protected RefreshHelper mRefreshHelper;
    protected LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return getCreateView(inflater);
    }

    protected View getCreateView(LayoutInflater inflater) {
        mRefreshBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_refresh, null, false);
        onInit();
        return mRefreshBinding.getRoot();
    }

    protected void initRefreshHelper(int start,int limit) {
        if (mRefreshHelper == null) {
            mRefreshHelper = new RefreshHelper(mActivity, this);
            mRefreshHelper.setErrorInfo(getErrorInfo());
            mRefreshHelper.setErrorImg(getErrorImg());
            mRefreshHelper.init(start, limit);
        }
    }

    protected void onInit() {

    }

    protected abstract String getErrorInfo();

    protected
    @DrawableRes
    int getErrorImg() {
        return 0;
    }

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRefreshBinding.rv;
    }

    @Override
    public boolean loadDeflutEmptyView() {
        return true;
    }

    @Override
    public EmptyViewBinding getEmptyViewBindin() {
        return DataBindingUtil.inflate(inflater, R.layout.empty_view, null, false);
    }


    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }

    @Override
    public View getEmptyView() {
        return null;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.clear();
        }
    }
}
