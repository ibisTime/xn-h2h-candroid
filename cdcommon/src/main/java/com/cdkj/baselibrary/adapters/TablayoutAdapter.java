package com.cdkj.baselibrary.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * tabLayout适配器
 */
public class TablayoutAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TablayoutAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(List<Fragment> fragment, List<String> title) {
        if (fragment == null || title == null) {
            return;
        }
        if (fragment.isEmpty() || title.isEmpty()) {
            return;
        }
        if (fragment.size() != title.size()) {
            return;
        }
        mFragmentList.addAll(fragment);
        mFragmentTitleList.addAll(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
