package com.cdkj.h2hwtw.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.h2hwtw.MainActivity;
import com.cdkj.h2hwtw.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 启动页
 * Created by 李先俊 on 2017/6/8.
 */

public class WelcomeAcitivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity

        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {
        }
        setContentView(R.layout.activity_welcom);
        ImageView img = (ImageView) findViewById(R.id.img_start);
        img.setImageResource(R.drawable.welcome);
        mSubscription.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        MainActivity.open(WelcomeAcitivity.this);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.E("welcome error");
                    }
                }));
    }


}
