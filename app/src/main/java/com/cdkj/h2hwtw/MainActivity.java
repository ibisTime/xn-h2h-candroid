package com.cdkj.h2hwtw;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.update.UpdateManager;
import com.cdkj.h2hwtw.databinding.ActivityMainBinding;
import com.cdkj.h2hwtw.module.firstpage.fragments.FirstPageFragment;
import com.cdkj.h2hwtw.module.goodstype.fragments.GoodsTypeFragment;
import com.cdkj.h2hwtw.module.im.fragments.ImFragment;
import com.cdkj.h2hwtw.module.product.activitys.ProductReleaseActivity;
import com.cdkj.h2hwtw.module.user.fragments.MyFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.EventTags.MAINFINISH;
import static com.cdkj.baselibrary.appmanager.EventTags.RELEASESUSS;

/**
 * 主页
 */
public class MainActivity extends AbsBaseLoadActivity {

    private ActivityMainBinding mBinding;


    public static final int SHOWFIRST = 0;//显示首页
    public static final int SHOWTYPE = 1;//显示类别
    public static final int SHOWIM = 2;//显示消息界面
    public static final int SHOWMY = 3;//显示我的界面


    @IntDef({SHOWFIRST, SHOWTYPE, SHOWIM, SHOWMY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface showType {
    }

    private
    @MainActivity.showType
    int mShowIndex = SHOWFIRST;//显示相应页面 默认首页


    private List<Fragment> fragments;
    private UpdateManager updateManager;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        initViewPager();

        initListener();
//
//        updateManager = new UpdateManager(getString(R.string.app_name));
//        updateManager.checkNewApp(this);
    }

    /**
     * 初始化事件
     */
    private void initListener() {

        //首页
        mBinding.radioMainTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowIndex(SHOWFIRST);
            }
        });


        //类别
        mBinding.radioMainTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowIndex(SHOWTYPE);
            }
        });


        //消息
        mBinding.radioMainTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowIndex(SHOWIM);
            }
        });

        //我的
        mBinding.radioMainTab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowIndex(SHOWMY);
            }
        });

        //发布
        mBinding.layoutRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductReleaseActivity.open(MainActivity.this);
            }
        });

    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(FirstPageFragment.getInstanse());//首页
        fragments.add(GoodsTypeFragment.getInstanse());//商品类别
        fragments.add(ImFragment.getInstanse());//消息
        fragments.add(MyFragment.getInstanse());//我的

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }


    /**
     * 设置要显示的界面
     *
     * @param index
     */
    private void setShowIndex(@showType int index) {
        if (index < 0 && index >= fragments.size()) {
            return;
        }

        switch (index) {
            case SHOWFIRST:
                mBinding.radioMainTab1.setChecked(true);
                break;
            case SHOWTYPE:
                mBinding.radioMainTab2.setChecked(true);
                break;
            case SHOWIM:
                mBinding.radioMainTab3.setChecked(true);
                break;
            case SHOWMY:
                if (!SPUtilHelpr.isLogin(MainActivity.this, false)) {
                    return;
                }
                mBinding.radioMainTab4.setChecked(true);
                break;
        }
        mShowIndex = index;
        mBinding.pagerMain.setCurrentItem(index, false);


    }

    @Subscribe
    public void MainEventBus(@showType int eventBusModel) {
        setShowIndex(eventBusModel);
    }

    @Subscribe
    public void MainEventBusFinish(String evbusTag) {

        if (TextUtils.equals(evbusTag, MAINFINISH)) { //结束主页
            finish();
            return;
        }
        if (TextUtils.equals(evbusTag, RELEASESUSS)) {//发布成功
            setShowIndex(SHOWTYPE);
            UITipDialog.showSuccess(this, "发布成功");
        }

    }

    @Override
    protected boolean canEvenFinish() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateManager != null) {
            updateManager.clear();
            updateManager = null;
        }
    }

    @Override
    public void onBackPressed() {
        showDoubleWarnListen("确认退出" + getString(R.string.app_name) + "？", new CommonDialog.OnPositiveListener() {
            @Override
            public void onPositive(View view) {
                EventBus.getDefault().post(EventTags.AllFINISH);
                EventBus.getDefault().post(EventTags.MAINFINISH);
                finish();
            }
        });
    }
}
