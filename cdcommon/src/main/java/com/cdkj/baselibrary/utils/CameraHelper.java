package com.cdkj.baselibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.cdkj.baselibrary.interfaces.CameraPhotoListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 拍照、相册辅助类
 * 使用注意 需要在启动页面中调用onActivityResult处理拍照回调
 * 调用onRequestPermissionsResult处理权限回调
 * 调用clear方法防止内存泄漏
 * Created by cdkj on 2017/11/7.
 */

public class CameraHelper {


    public final static int CAPTURE_PHOTO_CODE = 3;//相机
    public final static int CAPTURE_WALBUM_CODE = 4;//相册
    public final static int CAPTURE_ZOOM_CODE = 5;//裁剪

    private Activity mActivity;
    private Fragment mFragment;
    private Uri imageUrl;
    protected CompositeDisposable mSubscription;
    private PermissionHelper mPreHelper;//权限请求


    private String photoPath;//拍照图片路径
    public final static String staticPath = "imgSelect";
    private boolean isSplit = false;//执行相机或拍照后是否需要裁剪 默认不需要

    private CameraPhotoListener mCameraPhotoListener;

    //需要的权限
    private String[] needLocationPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /**
     * @param mActivity          在Activity界面使用
     * @param isSplit             是否裁剪
     * @param cameraPhotoListener 获取图片监听
     */
    public CameraHelper(@NonNull Activity mActivity, boolean isSplit, @NonNull CameraPhotoListener cameraPhotoListener) {
        this.isSplit = isSplit;
        this.mActivity = mActivity;
        this.mCameraPhotoListener = cameraPhotoListener;
        mSubscription = new CompositeDisposable();
        mPreHelper = new PermissionHelper(mActivity);

    }

    /**
     * @param fragment        在fragment页面使用
     * @param isSplit             是否裁剪
     * @param cameraPhotoListener 获取图片监听
     */
    public CameraHelper(@NonNull Fragment fragment, boolean isSplit, @NonNull CameraPhotoListener cameraPhotoListener) {
        this.isSplit = isSplit;
        this.mFragment = fragment;
        this.mCameraPhotoListener = cameraPhotoListener;
        mSubscription = new CompositeDisposable();
        mPreHelper = new PermissionHelper(mFragment);

    }

    /**
     * 判断权限并启动相册
     *
     * @return
     */
    public void startAlbum() {
        if (AppUtils.getAndroidVersion(Build.VERSION_CODES.M)) {
            requestPermissions(CAPTURE_WALBUM_CODE);
            return;
        }
        startImageFromAlbum();
    }

    /**
     * 判断权限并启动相机
     *
     * @return
     */
    public void startCamera() {

        if (AppUtils.getAndroidVersion(Build.VERSION_CODES.M)) {
            requestPermissions(CAPTURE_PHOTO_CODE);
            return;
        }
        startImageFromCamera();
    }


    /**
     * 请求权限
     *
     * @param type 判断是相册还是相机
     */
    private void requestPermissions(final int type) {

        mPreHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                switch (type) {
                    case CAPTURE_PHOTO_CODE:
                        startImageFromCamera();
                        break;
                    case CAPTURE_WALBUM_CODE:
                        startImageFromAlbum();
                        break;
                }
            }

            @Override
            public void doAfterDenied(String... permission) {
                mCameraPhotoListener.noPermissions();
            }
        }, needLocationPermissions);
    }


    /**
     * 判断是否存在可用相机
     *
     * @return
     */
    public boolean hasCamera() {
        if (mActivity != null) {
            PackageManager packageManager = mActivity.getPackageManager();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }

        PackageManager packageManager = mFragment.getContext().getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;

    }

    // 调相机拍照
    private void startImageFromCamera() {

        if (!hasCamera())  //判断有没有可用相机
        {
            mCameraPhotoListener.onPhotoFailure("没有可用相机");
            return;
        }

        String SDState = Environment.getExternalStorageState();

        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                    .format(new Date()) + "camera.jpg";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            if (mActivity != null) {
                imageUrl = FileProviderHelper.getUriForFile(mActivity, file);
                photoPath = file.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                mActivity.startActivityForResult(intent, CAPTURE_PHOTO_CODE);
                return;
            }

            imageUrl = FileProviderHelper.getUriForFile(mFragment.getContext(), file);
            photoPath = file.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            mFragment.startActivityForResult(intent, CAPTURE_PHOTO_CODE);

        } else {
            mCameraPhotoListener.onPhotoFailure("内存卡不存在");
        }
    }

    /**
     * 回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAPTURE_WALBUM_CODE:// 相册
                abumNext(data);
                break;
            case CAPTURE_PHOTO_CODE:// 拍照
                cameraNext();
                break;
            case CAPTURE_ZOOM_CODE:  //图片裁剪
                zoomNext(data);
                break;
            default:
                break;
        }
    }

    /**
     * 裁剪
     *
     * @param data
     */
    private void zoomNext(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap photo = extras.getParcelable("data");

        mSubscription.add(Observable.just(photo)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<Bitmap, String>() {
                    @Override
                    public String apply(@NonNull Bitmap bitmap) throws Exception {
                        String path = BitmapUtils.saveBitmapFile(bitmap, "split");  //图片名称
                        return path;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String path) throws Exception {
                        mCameraPhotoListener.onPhotoSuccessful(path);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mCameraPhotoListener.onPhotoFailure("图片获取失败");
                    }
                }));
    }

    /**
     * 相机
     */
    private void cameraNext() {
        if (isSplit) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                startPhotoZoom(new File(imageUrl.getPath()));
            } else {
                startPhotoZoom(new File(photoPath));
            }

        } else {
            mSubscription.add(Observable.just("")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .map(new Function<String, Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull String s) throws Exception {
                            Bitmap bitmap;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                bitmap = BitmapUtils.decodeBitmapFromFile(imageUrl.getPath(), 480, 800);
                            } else {
                                bitmap = BitmapUtils.decodeBitmapFromFile(photoPath, 480, 800);
                            }
                            LogUtil.E("poto1");
                            return bitmap;
                        }
                    })
                    .observeOn(Schedulers.io())
                    .map(new Function<Bitmap, String>() {
                        @Override
                        public String apply(@NonNull Bitmap bitmap) throws Exception {
                            String path = BitmapUtils.saveBitmapFile(bitmap, "camera");
                            LogUtil.E("poto2");
                            return path;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mCameraPhotoListener.onPhotoSuccessful(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mCameraPhotoListener.onPhotoFailure("图片获取失败");
                        }
                    }));
        }
    }

    /**
     * 相册
     *
     * @param data
     */
    private void abumNext(Intent data) {
        Uri imageUri = data.getData();

        if ("Xiaomi".equals(Build.MANUFACTURER) || SystemUtils.isMIUI())   //小米相册兼容代码
        {
            String imgP = setPhotoForMiuiSystem(data);

            if (imageUri == null) {
                mCameraPhotoListener.onPhotoFailure("图片获取失败");
                return;
            }
            if (isSplit) {
                startPhotoZoom(new File(imgP));
                return;
            }

            if (!TextUtils.isEmpty(imgP)) {
                mCameraPhotoListener.onPhotoSuccessful(imgP);
            }
            return;
        }
        if (imageUri == null) {
            mCameraPhotoListener.onPhotoFailure("图片获取失败");
            return;
        }

        if (isSplit) {
            startPhotoZoom(new File(imageUri.getPath()));
        } else {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (mActivity != null) {
                Cursor cursor = mActivity.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                mCameraPhotoListener.onPhotoSuccessful(picturePath);
                return;
            }

            Cursor cursor = mFragment.getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mCameraPhotoListener.onPhotoSuccessful(picturePath);

        }
    }


    /**
     * MIUI系统的相册选择
     *
     * @param data
     */
    private String setPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            if (mActivity != null) {
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = mActivity.getContentResolver().query(localUri, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                imagePath = c.getString(columnIndex);
                c.close();
                return imagePath;
            }
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = mFragment.getContext().getContentResolver().query(localUri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
            return imagePath;

        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        return imagePath;
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(File uri) {

        /*
         * yourself_sdk_path/docs/reference/android/content/Intent.html
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (mActivity != null) {
            intent.setDataAndType(getImageContentUri(uri), "image/*");
        } else {
            intent.setDataAndType(getImageContentUriFragment(uri), "image/*");
        }

        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        if (mActivity != null) {
            mActivity.startActivityForResult(intent, CAPTURE_ZOOM_CODE);
            return;
        }
        mFragment.startActivityForResult(intent, CAPTURE_ZOOM_CODE);
    }


    /**
     * 7.0适配
     * 转换 content:// uri
     *
     * @param imageFile
     * @return
     */
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = mActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return mActivity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 7.0适配
     * 转换 content:// uri
     *
     * @param imageFile
     * @return
     */
    public Uri getImageContentUriFragment(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = mFragment.getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return mFragment.getContext().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    // 调相册图片
    private void startImageFromAlbum() {
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         *
         */
        Intent intent = new Intent(Intent.ACTION_PICK, null);

        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/jpeg");

        mActivity.startActivityForResult(intent, CAPTURE_WALBUM_CODE);
    }


    /**
     * 获取权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        if (mPreHelper == null) return;
        mPreHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 清除
     */
    public void clear() {
        if (mSubscription != null) {
            mSubscription.dispose();
            mSubscription.clear();
        }
        mActivity = null;
        mFragment = null;
    }


}
