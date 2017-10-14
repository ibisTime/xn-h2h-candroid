package com.cdkj.h2hwtw.module.user.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.activitys.BackCardListActivity;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityGetMoneyBinding;

/**
 * 提现
 * Created by  cdkj on 2017/10/14.
 */

public class GetMoneyActivity extends AbsBaseLoadActivity {

    private ActivityGetMoneyBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, GetMoneyActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_get_money, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("提现");
        mBinding.linSelectBankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackCardListActivity.open(GetMoneyActivity.this, true);
            }
        });

    }
}
