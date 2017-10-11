package com.cdkj.h2hwtw.module.product.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityReleaseBinding;
import com.cdkj.h2hwtw.module.common.MyBaseLoadActivity;
import com.cdkj.h2hwtw.pop.PriceKeyboardPop;

/**
 * 产品发布
 * Created by cdkj on 2017/10/10.
 */

public class ProductReleaseActivity extends MyBaseLoadActivity {

    private ActivityReleaseBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductReleaseActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_release, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setBGWhite();
        setLeftImg();
        mBaseBinding.titleView.setMidTitle(getString(R.string.text_release));
        mBaseBinding.titleView.setRightTitle(getString(R.string.text_cancel));
        initListener();
    }

    @Override
    public void topTitleViewRightClick() {
        finish();
    }

    private void initListener() {

        mBinding.layoutPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new PriceKeyboardPop(ProductReleaseActivity.this).showPopupWindow();
            }
        });


    }
}
