package com.cdkj.h2hwtw.module.user.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ActivityCenterAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityActivityDetailsBinding;
import com.cdkj.h2hwtw.model.ActivityCenterModel;
import com.cdkj.h2hwtw.module.product.ProductReleaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 活动中心 详情 报名
 * Created by cdkj on 2017/10/21.
 */

public class ActivityCenterDetailsActivity extends AbsBaseLoadActivity {

    private ActivityCenterModel mActivityModel;

    private ActivityActivityDetailsBinding mBinding;

    public static void open(Context context, ActivityCenterModel activityCenterModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActivityCenterDetailsActivity.class);
        intent.putExtra("data", activityCenterModel);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_activity_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("活动详情");

        if (getIntent() != null) {
            mActivityModel = getIntent().getParcelableExtra("data");
        }

        if (mActivityModel != null) {
            mBinding.webView.loadData(mActivityModel.getDescription(), "text/html;charset=UTF-8", "UTF-8");
        }

        mBinding.btnIWantAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivityModel == null) return;
                /*type 1折扣活动 2 运费活动*/
                ProductReleaseActivity.open(ActivityCenterDetailsActivity.this, mActivityModel.getCode(), TextUtils.equals(mActivityModel.getType(), "2"));
            }
        });
    }
}
