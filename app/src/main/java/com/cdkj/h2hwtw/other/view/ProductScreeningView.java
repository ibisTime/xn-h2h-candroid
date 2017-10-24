package com.cdkj.h2hwtw.other.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cdkj.h2hwtw.R;

/**
 * 用于产品筛选
 * Created by cdkj on 2017/10/24.
 */

public class ProductScreeningView extends FrameLayout {

    private LinearLayout mAreaView;//区域View

    private LinearLayout mTabAreaView;//区域点击

    private LinearLayout mTabTypeView;//类型点击

    private LinearLayout mTabPriceView;//价格点击

    private LinearLayout mTabScreeningView;//筛选点击

    private View mDissMisVIew;//黑色背景


    public ProductScreeningView(@NonNull Context context) {
        this(context, null);
    }

    public ProductScreeningView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductScreeningView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        initTabListener();
    }

    private void initTabListener() {

        //区域点击
        mTabAreaView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //类别点击
        mTabTypeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //价格点击
        mTabPriceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //筛选点击
        mTabScreeningView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_screening_view, this, true);

        mTabAreaView = findViewById(R.id.lin_tab_1);
        mTabTypeView = findViewById(R.id.lin_tab_2);
        mTabPriceView = findViewById(R.id.lin_tab_3);
        mTabScreeningView = findViewById(R.id.lin_tab_4);

        mAreaView = findViewById(R.id.lin_area);
        mDissMisVIew = findViewById(R.id.dissmis_view);

        mDissMisVIew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mTabAreaView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAreaView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                mDissMisVIew.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                mAreaView.setVisibility(VISIBLE);
                mDissMisVIew.setVisibility(VISIBLE);
            }
        });
    }


}
