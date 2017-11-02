package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityAboutUsBinding;

/**关于我们
 * Created by cdkj on 2017/11/2.
 */

public class AboutAsActivity extends AbsBaseLoadActivity {

    private ActivityAboutUsBinding mbinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AboutAsActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mbinding= DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_about_us, null, false);
        return mbinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("关于我们");
    }




}
