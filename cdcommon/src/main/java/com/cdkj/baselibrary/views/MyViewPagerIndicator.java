package com.cdkj.baselibrary.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.DisplayHelper;

import java.util.List;

/**
 * ViewPager指示器 颜色 数量暂时硬编码
 */
public class MyViewPagerIndicator extends LinearLayout {

    private Paint mPaint;

    /**
     * 手指滑动时的偏移量
     */
    private int mTranslationX;
    /**
     * tab数量
     */
    private int mTabVisibleCount;

    private Context mContext;

    private List<String> mTitles;

    private int mLinWidth;

    public void setmLinWidth(int mLinWidth) {
        this.mLinWidth = DisplayHelper.dip2px(mContext, mLinWidth);
    }

    public MyViewPagerIndicator(Context context) {
        this(context, null);
    }

    public MyViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTabVisibleCount = 2;

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#2296f0"));
        mPaint.setStyle(Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));

        setOrientation(HORIZONTAL);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mLinWidth > 0) {
            canvas.save();
            mLinWidth = Math.min(mLinWidth, getMeasuredWidth() / 2);
            // 画出一个矩形
            Rect rect = new Rect(mTranslationX, getMeasuredHeight() - 6, mTranslationX + mLinWidth
                    , getMeasuredHeight());
            canvas.translate(getMeasuredWidth() / mTabVisibleCount / 2 - mLinWidth / 2, 0);   //计算偏移量
            canvas.drawRect(rect, mPaint); // 绘制该矩形
            canvas.restore();
        }

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0)
            return;

        for (int i = 0; i < cCount; i++)              //子View 平分宽度
        {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(lp);
        }

        setItemClickEvent();

    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 指示器跟随手指进行滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / mTabVisibleCount;
        mTranslationX = (int) (tabWidth * (offset + position));

        // 容器移动，在tab处于移动至最后一个时
        if (position >= (mTabVisibleCount - 2) && offset > 0
                && getChildCount() > mTabVisibleCount) {

            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
                        + (int) (tabWidth * offset), 0);
            } else {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }

        }

        invalidate();

    }

    public void setTabItemTitles(List<String> titles) {
        Log.i("lxj", "setTabItemTitles");
        if (titles != null && titles.size() > 0) {
            mTabVisibleCount = titles.size();
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                addView(generateTextView(title));
            }
            setItemClickEvent();
        }
    }

    /**
     * 设置可见的Tab数量
     *
     * @param count
     */
    public void setVisibleTabCount(int count) {
        mTabVisibleCount = count;
    }

    /**
     * 根据title创建Tab
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(Color.parseColor("#999999"));
        tv.setLayoutParams(lp);
        return tv;
    }

    private ViewPager mViewPager;

    public interface PageOnchangeListener {
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    public PageOnchangeListener mListener;

    public void setOnPageChangeListener(PageOnchangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置关联的ViewPager
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, final int pos) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset,
                            positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }

                highLightTextView(position);

                // 极端情况的Bug修复
                if (position <= (mTabVisibleCount - 2))
                    scrollTo(0, 0);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    /**
     * 重置TAB文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
            }
        }

    }

    /**
     * 高亮某个Tab的文本
     *
     * @param pos
     */
    private void highLightTextView(int pos) {
        resetTextViewColor();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.parseColor("#2296f0"));
        }
    }

    /**
     * 设置Tab的点击事件
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });

        }
    }

}
