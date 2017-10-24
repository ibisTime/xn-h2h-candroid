package com.cdkj.h2hwtw;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.h2hwtw.databinding.ActivityPhotoViewpagerBinding;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2017/10/24.
 */

public class PhotoViewPagerActivity extends AbsBaseLoadActivity {

    private ActivityPhotoViewpagerBinding mbinding;

    private ArrayList<String> urlList;

    private int showPosition = 0;

    public static void open(Context context, ArrayList<String> urlList, int showPosition) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PhotoViewPagerActivity.class);
        intent.putStringArrayListExtra("urlList", urlList);
        intent.putExtra("showPosition", showPosition);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_photo_viewpager, null, false);
        return mbinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            urlList = getIntent().getStringArrayListExtra("urlList");
            showPosition = getIntent().getIntExtra("showPosition", 0);
        }

        if (urlList == null) {
            return;
        }
        mBaseBinding.titleView.setMidTitle(showPosition + 1 + "/" + urlList.size());
        mbinding.photoViewPager.setAdapter(new MyImageAdapter(urlList));
        mbinding.photoViewPager.setOffscreenPageLimit(urlList.size());
        if (showPosition >= 0 && showPosition < urlList.size()) {
            mbinding.photoViewPager.setCurrentItem(showPosition);
        }
        mbinding.photoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBaseBinding.titleView.setMidTitle(position + 1 + "/" + urlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class MyImageAdapter extends PagerAdapter {

        private List<String> imageUrls;

        public MyImageAdapter(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = imageUrls.get(position);
            PhotoView photoView = new PhotoView(PhotoViewPagerActivity.this);
            ImgUtils.loadImg(PhotoViewPagerActivity.this, MyCdConfig.QINIUURL + url, photoView);
            container.addView(photoView);

            return photoView;
        }

        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
