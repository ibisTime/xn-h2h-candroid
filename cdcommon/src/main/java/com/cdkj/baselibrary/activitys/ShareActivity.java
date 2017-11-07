package com.cdkj.baselibrary.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.ActivityShareBinding;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.WxUtil;


/**
 * Created by 李先俊 on 2017/8/1.
 */

public class ShareActivity extends Activity {

    private ActivityShareBinding mbinding;

    private String mShareUrl;//需要分享的URL
    private String mShareInfo;//需要分享的说明
    private String mShareTile;//需要分享的说明

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String shareUrl, String shareInfo, String shareTitle) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("shareInfo", shareInfo);
        intent.putExtra("shareTitle", shareTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_share);

        if (getIntent() != null) {
            mShareUrl = getIntent().getStringExtra("shareUrl");
            mShareInfo = getIntent().getStringExtra("shareInfo");
            mShareTile = getIntent().getStringExtra("shareTitle");
        }

        LogUtil.E("分享"+mShareUrl);

        initListener();

    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mbinding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mbinding.imgPyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WxUtil.shareToPYQ(ShareActivity.this, mShareUrl, mShareTile, mShareInfo);
                finish();
            }
        });

        mbinding.imgWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WxUtil.shareToWX(ShareActivity.this,
                        mShareUrl, mShareTile, mShareInfo);
                finish();
            }
        });

    }
}
