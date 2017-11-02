package com.cdkj.h2hwtw.other.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.h2hwtw.R;


/**
 * 用于我的管理页面显示角标
 * Created by Administrator on 2016-07-04.
 */
public class OrderBadgeView extends FrameLayout {

    private TextView tv_badge_title;
    private TextView tv_badge_info;
    private ImageView img_bg;
    private Context context;

    private int maxNumber;

    public OrderBadgeView(Context context) {
        this(context, null);
    }

    public OrderBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.badge_layout, this, true);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);
        int imgId = ta.getResourceId(R.styleable.BadgeView_badge_img, -1);
        String titleString = ta.getString(R.styleable.BadgeView_badge_title);
        ta.recycle();

        tv_badge_title = (TextView) findViewById(R.id.tv_badge_title);
        tv_badge_info = (TextView) findViewById(R.id.tv_badge_info);
        img_bg = (ImageView) findViewById(R.id.img_bg);

        maxNumber = 99;

        setTitle(titleString);
        setImg(imgId);

    }

    public void setTitle(String text) {
        tv_badge_title.setText(text);
    }

    public void setNumInfo(int info) {
        if (info <= 0) {
            tv_badge_info.setVisibility(View.GONE);
        } else if (info <= maxNumber) {
            tv_badge_info.setVisibility(View.VISIBLE);
        } else if (info > maxNumber) {
            tv_badge_info.setVisibility(View.VISIBLE);
            info = maxNumber;
        }
        tv_badge_info.setText(info + "");
    }


    public void initTitleAndImg(String title, int id) {
        setTitle(title);
        setImg(id);
    }

    public void setImg(int id) {
        if (id == -1) {
            return;
        }
        img_bg.setImageResource(id);
//        ImgUtils.loadImg(context, id, img_bg);
    }

}
