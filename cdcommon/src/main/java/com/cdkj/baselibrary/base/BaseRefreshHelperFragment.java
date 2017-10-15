package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.databinding.FragmentRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.interfaces.RefreshInterface;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现下拉刷新 上拉加载 分页逻辑
 * Created by 李先俊 on 2017/7/19.
 */

public abstract class BaseRefreshHelperFragment<T> extends BaseLazyFragment implements RefreshInterface<T> {

    protected FragmentRecyclerRefreshBinding mBinding;
    protected RefreshHelper mRefreshHelper;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return getCreateView();
    }

    protected View getCreateView() {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_refresh, null, false);
        mRefreshHelper = new RefreshHelper(mActivity, this);
        mRefreshHelper.setErrorInfo(getErrorInfo());
        mRefreshHelper.init(0, 10);
        return mBinding.getRoot();
    }

    protected abstract String getErrorInfo();

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mBinding.rv;
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
