package com.cdkj.h2hwtw.module.firstpage;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivitySignInBinding;

/**
 * 签到
 * Created by cdkj on 2017/10/26.
 */

public class SignInActivity extends AbsBaseLoadActivity {

    private ActivitySignInBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("每日签到");
        mBaseBinding.titleView.setRightTitle("签到规则");
    }

    @Override
    public void topTitleViewRightClick() {
        WebViewActivity.openkey(SignInActivity.this, "签到规则", "signRegulation");
    }
}
