package com.cdkj.h2hwtw.module.user.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityMyAccountBinding;

/**
 * 我的账户
 * Created by cdjk on 2017/10/14.
 */

public class MyAccountActivity extends AbsBaseLoadActivity {

    private ActivityMyAccountBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyAccountActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_my_account, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我的账户");
    }
}
