package com.cdkj.baselibrary.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityCropBinding;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.LogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.baselibrary.utils.BitmapUtils.picHeight;
import static com.cdkj.baselibrary.utils.BitmapUtils.picWidth;
import static com.cdkj.baselibrary.utils.CameraHelper.cropPath;

/**
 * 裁剪
 * Created by cdkj on 2017/11/13.
 */
public class CropActivity extends AbsBaseLoadActivity {

    private ActivityCropBinding mBinding;

    private int code = 0;
    private Bitmap openBitmap;


    /**
     * @param context
     * @param path    需要裁剪的图片路径
     */
    public static void open(Context context, String path, int requestCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(cropPath, path);
        intent.putExtra("code", path);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_crop, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return BitmapUtils.saveBitmapFile(mBinding.cimg.getCroppedImage(openBitmap), "crop_img");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        disMissLoading();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String path) throws Exception {
                        setResult(Activity.RESULT_OK, new Intent().putExtra(cropPath, path));
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        finish();
                    }
                }));
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setRightTitle("确定");
        mBaseBinding.titleView.setMidTitle("图片裁剪");

        code = getIntent().getIntExtra("code", 0);
        showLoadingDialog();

        mSubscription.add(Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        if (code == CameraHelper.CAPTURE_PHOTO_CODE) {   //如果是拍照的话
                            openBitmap = BitmapUtils.decodeBitmapFromFile(getIntent().getStringExtra(cropPath), picWidth, picHeight);
                        } else {
/*
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(getIntent().getStringExtra(cropPath), options); // Just get image size
                            LogUtil.E("图片wg"+options.outWidth+" "+options.outHeight);*/
                            openBitmap = BitmapUtils.decodeBitmapFromFile(getIntent().getStringExtra(cropPath), picWidth, picHeight);
//                            openBitmap = BitmapUtils.decodeFileAndRotaing(getIntent().getStringExtra(cropPath));
                        }
                        return openBitmap;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        disMissLoading();
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bytes) throws Exception {
                        mBinding.cimg.setImageBitmap(bytes);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        finish();
                    }
                }));
    }
}
