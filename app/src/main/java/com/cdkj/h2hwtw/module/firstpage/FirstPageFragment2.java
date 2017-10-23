package com.cdkj.h2hwtw.module.firstpage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.FragmentFirstPageScollor2Binding;
import com.cdkj.h2hwtw.module.goodstype.GoodsTypeFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 李先俊
 * Created by cdkj on 2017/10/9.
 */

public class FirstPageFragment2 extends BaseLazyFragment {

    private FragmentFirstPageScollor2Binding mBinding;


    /**
     * 获得fragment实例
     *
     * @return
     */
    public static FirstPageFragment2 getInstanse() {
        FirstPageFragment2 fragment = new FirstPageFragment2();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_page_scollor2, null, false);

        mBinding.refreshLayout.setEnableLoadmoreWhenContentNotFull(true);

        mBinding.refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                EventBus.getDefault().post("88888");
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });

        mBinding.linAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.customViewPager.setCurrentItem(1);
            }
        });
        mBinding.lineHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.customViewPager.setCurrentItem(0);
            }
        });

        mBinding.customViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), getFragments()));
        mBinding.customViewPager.setOffscreenPageLimit(2);
//        mBinding.customViewPager.resetHeight(0);
        return mBinding.getRoot();
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    public List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HotProductFragment.getInstanse());
        fragments.add(GoodsTypeFragment.getInstanse());
        return fragments;
    }

    @Subscribe
    public void EventBus(String evbusTag) {

        if (TextUtils.equals(evbusTag, "99999")) {//发布成功
            mBinding.refreshLayout.finishLoadmore();
        }

    }
}
