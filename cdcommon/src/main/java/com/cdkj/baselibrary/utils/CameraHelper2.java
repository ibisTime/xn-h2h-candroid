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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.cdkj.baselibrary.activitys.CropActivity;
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
 * 如果使用Fragment对象 为了简单使用的是 android.support.v4.app.Fragment
 * Created by cdkj on 2017/11/7.
 */
//TODO 裁剪接口抽取
public class CameraHelper2 {

    public final static int CAPTURE_PHOTO_CODE = 3;//相机
    public final static int CAPTURE_WALBUM_CODE = 4;//相册
    public final static int CAPTURE_ZOOM_CODE = 5;//裁剪

    private int mRequestCode = -1;//用于记录是相机还是相册裁剪

    private Object mContext;

    private Uri imageUrl;
    protected CompositeDisposable mSubscription;
    private PermissionHelper mPreHelper;//权限请求


    private String photoPath;//拍照图片路径
    public final static String staticPath = "imgSelect";
    public final static String cropPath = "cropPath";

    private boolean isSplit = true;//执行相机或拍照后是否需要裁剪 默认裁剪

    private CameraPhotoListener mCameraPhotoListener;

    private CamerahelperCropInterface mCamerahelperCropInterface;//裁剪接口

    //需要的权限
    private String[] needLocationPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    /**
     * 设置裁剪接口
     *
     * @param mCamerahelperCropInterface
     */
    public void setmCamerahelperCropInterface(CamerahelperCropInterface mCamerahelperCropInterface) {
        this.mCamerahelperCropInterface = mCamerahelperCropInterface;
    }

    /**
     * 默认启动裁剪
     *
     * @param cameraPhotoListener 获取图片监听
     */
    public CameraHelper2(@NonNull Object object, @NonNull CameraPhotoListener cameraPhotoListener) {
        this.mContext = object;
        checkCallingObjectSuitability(object);
        this.isSplit = true;
        this.mCameraPhotoListener = cameraPhotoListener;
        mSubscription = new CompositeDisposable();
        mPreHelper = new PermissionHelper(object);
        mCamerahelperCropInterface = new defaultCropInterface();
    }

    /**
     * 判断权限并启动相册
     *
     * @return
     */
    public void startAlbum() {
        if (isNeedRequestPremission()) {
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
        if (isNeedRequestPremission()) {
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
        mRequestCode = type;
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
                mCameraPhotoListener.noPermissions(type);
            }
        }, needLocationPermissions);
    }


    /**
     * 判断是否存在可用相机
     *
     * @return
     */
    public boolean hasCamera() {
        if (getContextActivity(mContext) == null) {
            return false;
        }
        PackageManager packageManager = getContextActivity(mContext).getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    // 调相机拍照
    private void startImageFromCamera() {

        mRequestCode = CAPTURE_PHOTO_CODE;

        if (!hasCamera())  //判断有没有可用相机
        {
            mCameraPhotoListener.onPhotoFailure(CAPTURE_PHOTO_CODE, "没有可用相机");
            return;
        }

        String SDState = Environment.getExternalStorageState();

        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                    .format(new Date()) + "camera.jpg";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            if (isActivity(mContext)) {
                imageUrl = FileProviderHelper.getUriForFile(getActivity(mContext), file);
                photoPath = file.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                getActivity(mContext).startActivityForResult(intent, CAPTURE_PHOTO_CODE);
                return;
            }

            imageUrl = FileProviderHelper.getUriForFile(getContextActivity(mContext), file);
            photoPath = file.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            getFragment(mContext).startActivityForResult(intent, CAPTURE_PHOTO_CODE);

        } else {
            mCameraPhotoListener.onPhotoFailure(CAPTURE_PHOTO_CODE, "内存卡不存在");
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
                mRequestCode = CAPTURE_WALBUM_CODE;
                abumNext(data);
                break;
            case CAPTURE_PHOTO_CODE:// 拍照
                mRequestCode = CAPTURE_PHOTO_CODE;
                cameraNext();
                break;
            case CAPTURE_ZOOM_CODE:  //图片裁剪
                if (data == null || TextUtils.isEmpty(data.getStringExtra(cropPath))) {
                    mCameraPhotoListener.onPhotoFailure(CAPTURE_ZOOM_CODE, "图片获取失败");
                    return;
                }
                mCameraPhotoListener.onPhotoSuccessful(mRequestCode, data.getStringExtra(cropPath));
//                zoomNext(data, mRequestCode);
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
    private void zoomNext(Intent data, final int requestCode) {
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
                        mCameraPhotoListener.onPhotoSuccessful(requestCode, path);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mCameraPhotoListener.onPhotoFailure(requestCode, "图片获取失败");
                    }
                }));
    }

    /**
     * 相机
     */
    private void cameraNext() {
        if (isSplit) {
            if (!CameraHelper2.isNeedUriAdapte()) {
                startCrop(imageUrl.getPath());
//                startPhotoZoom(new File(imageUrl.getPath()));
            } else {
                startCrop(photoPath);
//                startPhotoZoom(new File(photoPath));
            }

        } else {
            mSubscription.add(Observable.just("")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .map(new Function<String, Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull String s) throws Exception {
                            Bitmap bitmap;
                            if (!CameraHelper2.isNeedUriAdapte()) {
                                bitmap = BitmapUtils.decodeBitmapFromFile(imageUrl.getPath(), BitmapUtils.picWidth, BitmapUtils.picHeight);
                            } else {
                                bitmap = BitmapUtils.decodeBitmapFromFile(photoPath, BitmapUtils.picWidth, BitmapUtils.picHeight);
                            }
                            return bitmap;
                        }
                    })
                    .observeOn(Schedulers.io())
                    .map(new Function<Bitmap, String>() {
                        @Override
                        public String apply(@NonNull Bitmap bitmap) throws Exception {
                            String path = BitmapUtils.saveBitmapFile(bitmap, "camera");
                            return path;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            mCameraPhotoListener.onPhotoSuccessful(CAPTURE_PHOTO_CODE, s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mCameraPhotoListener.onPhotoFailure(CAPTURE_PHOTO_CODE, "图片获取失败");
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
        if (data == null) return;
        Uri imageUri = data.getData();

        if ("Xiaomi".equals(Build.MANUFACTURER) || SystemUtils.isMIUI())   //小米相册兼容代码
        {
            String imgP = setPhotoForMiuiSystem(data);

            if (imageUri == null) {
                mCameraPhotoListener.onPhotoFailure(CAPTURE_WALBUM_CODE, "图片获取失败");
                return;
            }
            if (isSplit) {
//                startPhotoZoom(new File(imgP));
                startCrop(imgP);
                return;
            }

            if (!TextUtils.isEmpty(imgP)) {
                mCameraPhotoListener.onPhotoSuccessful(CAPTURE_WALBUM_CODE, imgP);
            }
            return;
        }
        if (imageUri == null) {
            mCameraPhotoListener.onPhotoFailure(CAPTURE_WALBUM_CODE, "图片获取失败");
            return;
        }

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        if (isActivity(mContext)) {
            Cursor cursor = getActivity(mContext).getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mCameraPhotoListener.onPhotoSuccessful(CAPTURE_WALBUM_CODE, picturePath);

            if (isSplit) {
                startCrop(picturePath);
//            startPhotoZoom(new File(imageUri.getPath()));
            }
            return;
        }

        Cursor cursor = getFragment(mContext).getContext().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        mCameraPhotoListener.onPhotoSuccessful(CAPTURE_WALBUM_CODE, picturePath);

        if (isSplit) {
            startCrop(picturePath);
//            startPhotoZoom(new File(imageUri.getPath()));
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
            if (isActivity(mContext)) {
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity(mContext).getContentResolver().query(localUri, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                imagePath = c.getString(columnIndex);
                c.close();
                return imagePath;
            }
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getFragment(mContext).getContext().getContentResolver().query(localUri, filePathColumns, null, null, null);
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
     * 启动裁剪页面
     *
     * @param path
     */
    public void startCrop(String path) {
        if (mCamerahelperCropInterface == null) return;
        if (isActivity(mContext)) {
            mCamerahelperCropInterface.startCropFromActivity(getActivity(mContext), mRequestCode, path);
            return;
        }
        mCamerahelperCropInterface.startCropFromFragment(getFragment(mContext), mRequestCode, path);
    }

    /**
     * 系统裁剪图片方法实现 华为裁剪为圆形没做兼容处理
     *
     * @param uri
     */
    public void startPhotoZoom(File uri) {

        /*
         * yourself_sdk_path/docs/reference/android/content/Intent.html
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(uri), "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");

        if (android.os.Build.MODEL.contains("HUAWEI")) {//华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        if (isActivity(mContext)) {
            getActivity(mContext).startActivityForResult(intent, CAPTURE_ZOOM_CODE);
            return;
        }
        getFragment(mContext).startActivityForResult(intent, CAPTURE_ZOOM_CODE);
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
        Cursor cursor = getContextActivity(mContext).getContentResolver().query(
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
                return getContextActivity(mContext).getContentResolver().insert(
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
        mRequestCode = CAPTURE_WALBUM_CODE;
        Intent intent = new Intent(Intent.ACTION_PICK, null);

        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        if (isActivity(mContext)) {
            getActivity(mContext).startActivityForResult(intent, CAPTURE_WALBUM_CODE);
            return;
        }
        getFragment(mContext).startActivityForResult(intent, CAPTURE_WALBUM_CODE);
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
        this.mContext = null;
    }

    /**
     * 裁剪接口
     */
    public interface CamerahelperCropInterface {
        void startCropFromActivity(Activity context, int requestCode, String path);//从Activity页面启动

        void startCropFromFragment(Fragment fragment, int requestCode, String path);//从fragment页面启动
    }


    /**
     * 默认裁剪实现
     */
    class defaultCropInterface implements CamerahelperCropInterface {

        @Override
        public void startCropFromActivity(Activity context, int requestCode, String path) {
            Intent intent = new Intent(context, CropActivity.class);
            intent.putExtra(cropPath, path);
            intent.putExtra("code", requestCode);
            context.startActivityForResult(intent, CAPTURE_ZOOM_CODE);
        }

        @Override
        public void startCropFromFragment(Fragment fragment, int requestCode, String path) {
            Intent intent = new Intent(fragment.getContext(), CropActivity.class);
            intent.putExtra(cropPath, path);
            intent.putExtra("code", requestCode);
            fragment.startActivityForResult(intent, CAPTURE_ZOOM_CODE);
        }
    }


    /**
     * 检查传递Context是否合法
     *
     * @param object
     */
    private void checkCallingObjectSuitability(@Nullable Object object) {
        if (object == null) {
            throw new IllegalArgumentException("camera start object is null");
        }

        boolean isActivity = object instanceof android.app.Activity;

        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;

        if (!isActivity && !isSupportFragment) {
            throw new IllegalArgumentException("object is check fall");
        }
    }

    /**
     * 是否需要进行权限申请
     *
     * @return
     */
    public static boolean isNeedRequestPremission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否需要7.0适配
     *
     * @return false 不需要
     */
    public static boolean isNeedUriAdapte() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


    /**
     * 判断是否activity
     *
     * @param object
     * @return
     */
    public boolean isActivity(Object object) {
        return object instanceof Activity;
    }

    /**
     * 获取上下文
     *
     * @param object
     * @return
     */
    public Activity getContextActivity(Object object) {
        return PermissionHelper.getActivity(object);
    }

    public Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else {
            return null;
        }
    }

    /**
     * 获取framgent
     *
     * @param object
     * @return
     */
    public Fragment getFragment(Object object) {
        if (object instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) object);
        }
        return null;
    }

}
