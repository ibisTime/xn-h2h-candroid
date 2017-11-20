package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityCropTestBinding;
import com.cdkj.baselibrary.interfaces.CameraPhotoListener;
import com.cdkj.baselibrary.utils.CameraHelper2;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;

/**
 * 裁剪
 * Created by cdkj on 2017/11/13.
 */

public class CropTestActivity extends AbsBaseLoadActivity {


    private ActivityCropTestBinding mBindnig;

    private CameraHelper2 mCameraHelper;

    /**
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CropTestActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBindnig = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_crop_test, null, false);
        return mBindnig.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mCameraHelper = new CameraHelper2(CropTestActivity.this, new CameraPhotoListener() {
            @Override
            public void onPhotoSuccessful(int requestCode, String path) {
                LogUtil.E("图片"+path);
                ImgUtils.loadImg(CropTestActivity.this,path,mBindnig.img1);
            }

            @Override
            public void onPhotoFailure(int requestCode, String msg) {

            }

            @Override
            public void noPermissions(int requestCode) {

            }
        });

        mBindnig.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraHelper.startCamera();
            }
        });
        mBindnig.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraHelper.startAlbum();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCameraHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mCameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraHelper.clear();
    }
}
