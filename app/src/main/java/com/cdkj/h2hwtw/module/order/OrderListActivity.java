package com.cdkj.h2hwtw.module.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.activitys.CommonTablayoutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表
 * Created by cdkj  on 2017/10/18.
 */

public class OrderListActivity extends CommonTablayoutActivity {


    public static void open(Context context, @OrderListFramgnet.loadState int showIndex) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra("showIndex", showIndex);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        mBaseBinding.titleView.setMidTitle("我的订单");
        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置滑动模式 /TabLayout.MODE_SCROLLABLE 可滑动 ，TabLayout.MODE_FIXED表示不可滑动
        if (getIntent() != null) {
            mTabLayoutBinding.viewpager.setCurrentItem(getIntent().getIntExtra("showIndex", 0));
        }
    }

    @Override
    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(OrderListFramgnet.getInstanse(OrderListFramgnet.ORDERALL));
        fragments.add(OrderListFramgnet.getInstanse(OrderListFramgnet.ORDERWAITEPAY));
        fragments.add(OrderListFramgnet.getInstanse(OrderListFramgnet.ORDERWAITESEND));
        fragments.add(OrderListFramgnet.getInstanse(OrderListFramgnet.ORDERWAITEGET));
        fragments.add(OrderListFramgnet.getInstanse(OrderListFramgnet.ORDERSAY));
        fragments.add(OrderListFramgnet.getInstanse(OrderListFramgnet.ORDERDONE));
        return fragments;
    }

    @Override
    public List<String> getFragmentTitles() {
        List<String> list = new ArrayList<>();
        list.add("全部");
        list.add("待支付");
        list.add("待发货");
        list.add("待收货");
        list.add("待评价");
        list.add("已评价");
        return list;
    }
}
