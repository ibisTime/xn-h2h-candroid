package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.cdkj.baselibrary.activitys.CommonTablayoutActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.ReleaseNumModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

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
        strings.add("我发布的(0)");
        strings.add("已下架宝贝(0)");
        return strings;
    }


    /**
     * 获取发布数量
     */
    public void getReleaseSumRequest() {

        if (TextUtils.isEmpty(SPUtilHelpr.getUserId())) {
            return;
        }
        Map map = RetrofitUtils.getRequestMap();
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getReleaseSum("808018", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<ReleaseNumModel>(this) {
            @Override
            protected void onSuccess(ReleaseNumModel data, String SucMessage) {

                mTabLayoutBinding.tablayout.getTabAt(0).setText("我发布的(" + data.getTotalOnProduct() + ")");

                if (data.getTotalProduct() > data.getTotalOnProduct()) {
                    mTabLayoutBinding.tablayout.getTabAt(1).setText("已下架宝贝(" + (data.getTotalProduct() - +data.getTotalOnProduct()) + ")");
                } else {
                    mTabLayoutBinding.tablayout.getTabAt(1).setText("已下架宝贝(0)");
                }


            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
            }

            @Override
            protected void onFinish() {
            }
        });
    }

}
