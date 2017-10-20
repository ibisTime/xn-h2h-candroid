package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.cdkj.baselibrary.activitys.CommonTablayoutActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.EventTags.RELEASESUSS;

/**
 * 发布产品列表
 * Created by cdkj on 2017/10/19.
 */

public class ReleaseProductListActivity extends CommonTablayoutActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ReleaseProductListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaseBinding.titleView.setMidTitle("我发布的");
    }

    @Override
    public List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ReleaseProductListFragment.getInstanse(ReleaseProductListFragment.RELEASETYPE));
        fragments.add(ReleaseProductListFragment.getInstanse(ReleaseProductListFragment.DOWNTYPE));

        return fragments;
    }

    @Override
    public List<String> getFragmentTitles() {
        List<String> strings = new ArrayList<>();
        strings.add("我发布的");
        strings.add("已下架宝贝");
        return strings;
    }

    @Subscribe
    public void EventFinish(String evbusTag) {
        if (TextUtils.equals(evbusTag, RELEASESUSS)) {//产品发布成功
            this.finish();
        }
    }

}
