package com.cdkj.h2hwtw.module.user.coupons;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.activitys.CommonTablayoutActivity;
import com.cdkj.baselibrary.activitys.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券列表
 * Created by cdkj on 2017/10/19.
 */

public class CouponsAllActivity extends CommonTablayoutActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CouponsAllActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void topTitleViewRightClick() {
        WebViewActivity.openkey(this,"优惠券说明","cardsTradition");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaseBinding.titleView.setMidTitle("我的优惠券");
        mBaseBinding.titleView.setRightTitle("使用说明");
    }

    @Override
    public List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CouponsFragment.getInstanse(0));
        fragments.add(CouponsFragment.getInstanse(1));
        fragments.add(CouponsFragment.getInstanse(3));

        return fragments;
    }

    @Override
    public List<String> getFragmentTitles() {
        List<String> strings = new ArrayList<>();
        strings.add("未使用");
        strings.add("已使用");
        strings.add("已过期");
        return strings;
    }
}
