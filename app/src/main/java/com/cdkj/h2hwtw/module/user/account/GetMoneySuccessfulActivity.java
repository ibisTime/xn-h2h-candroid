package com.cdkj.h2hwtw.module.user.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityGetmoneySuccessfulBinding;

/**
 * 提现成功
 * Created by  cdkj on 2017/10/14.
 */

public class GetMoneySuccessfulActivity extends AbsBaseLoadActivity {

    private ActivityGetmoneySuccessfulBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, GetMoneySuccessfulActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_getmoney_successful, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("提现成功");

    }
}
