package com.cdkj.h2hwtw.module.product.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityProductDetailBinding;

/**
 * 产品详情
 * Created by cdkj on 2017/10/17.
 */

public class ProductDetailActivity extends AbsBaseLoadActivity {

    private ActivityProductDetailBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductDetailActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_product_detail, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("产品详情");
        mBinding.expandabletext.expandTextView.setText("产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详情产产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详产品详情产品详情产品详情产品详情产品详情产品详情产产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详产品详情产品详情产品详情产品详情产品详情产品详情产产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详产品详情产品详情产品详情产品详情产品详情产品详情产产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详产品详情产品详情产品详情产品详情产品详情产品详情产产品详情产品详情产品详情产品详情产品详情产品详情产品详情产品详");
    }
}
