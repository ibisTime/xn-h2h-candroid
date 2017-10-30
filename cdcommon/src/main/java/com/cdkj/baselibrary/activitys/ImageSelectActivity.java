package com.cdkj.baselibrary.activitys;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.FileProviderHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import static com.cdkj.baselibrary.appmanager.MyCdConfig.CACHDIR;

/**
 * 打开相机 相册 图片裁剪 功能
 */

// TODO Activity和拍照方法分离优化 bitmapUtils抽取
public class ImageSelectActivity extends Activity implements View.OnClickListener {

    private TextView tv_take_capture;// 拍照
    private TextView tv_alumb;// 相册选取
    private TextView tv_cancle;// 取消
    private View empty_view;// 取消


    public final static String staticPath = "imgSelect";

    private boolean isSplit = false;//执行相机或拍照后是否需要裁剪 默认不需要

    private String photoPath;//拍照图片路径


    public final static int CAPTURE_PHOTO_CODE = 3;//相机
    public final static int CAPTURE_WALBUM_CODE = 4;//相册
    public final static int CAPTURE_ZOOM_CODE = 5;//裁剪


    public final static int CAPTURE_PERMISSION_CODE = 6;//相机权限申请
    public final static int CAPTURE_PERMISSION_CODD_2 = 7;//相册权限申请

    public static final int SHOWPIC = 1; //显示拍照按钮
    public static final int SHOWALBUM = 2;//显示相册


    private Uri imageUrl;

    protected CompositeDisposable mSubscription;

    public static void launch(Activity activity, int photoid, int showType, boolean isSplit) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra("showType", showType);
        intent.putExtra("isSplit", isSplit);
        activity.startActivityForResult(intent, photoid);
    }

    public static void launch(Activity activity, int photoid) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        activity.startActivityForResult(intent, photoid);
    }

    public static void launchFragment(Fragment fragment, int photoid) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), ImageSelectActivity.class);
        fragment.startActivityForResult(intent, photoid);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        init();
    }

    protected void init() {
        mSubscription = new CompositeDisposable();

        tv_take_capture = (TextView) findViewById(R.id.tv_take_capture);
        tv_alumb = (TextView) findViewById(R.id.tv_alumb);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        empty_view = findViewById(R.id.empty_view);


        tv_take_capture.setOnClickListener(this);
        tv_alumb.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        empty_view.setOnClickListener(this);

        if (getIntent() != null) {

            isSplit = getIntent().getBooleanExtra("isSplit", isSplit); //获取是否裁剪

            switch (getIntent().getIntExtra("showType", 0)) {      //根据参数显示相册按钮还是显示拍照按钮 默认两个都显示
                case SHOWPIC:
                    tv_take_capture.setVisibility(View.VISIBLE);
                    tv_alumb.setVisibility(View.GONE);
                    break;
                case SHOWALBUM:
                    tv_take_capture.setVisibility(View.GONE);
                    tv_alumb.setVisibility(View.VISIBLE);
                    break;
                default:
                    tv_take_capture.setVisibility(View.VISIBLE);
                    tv_alumb.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            tv_take_capture.setVisibility(View.VISIBLE);
            tv_alumb.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        try {
            int i = v.getId();
            if (i == R.id.tv_take_capture) {
                permissionCheck(CAPTURE_PERMISSION_CODE); //6.0系统申请相机权限

            } else if (i == R.id.tv_alumb) {
                permissionCheck(CAPTURE_PERMISSION_CODD_2);

            } else if (i == R.id.empty_view || i == R.id.tv_cancle) {
                finish();

            }
        } catch (Exception e) {
            Toast.makeText(ImageSelectActivity.this, "出现未知错误", Toast.LENGTH_SHORT);
            finish();
        }
    }

    /**
     * 判断是否存在可用相机并启动相机
     *
     * @return
     */
    private void startCamera() {
        if (hasCamera())  //判读有没有可用相机
        {
            getImageFromCamera();
        } else {
            if (isFinishing()) {
                return;
            }
            new CommonDialog(this).builder()
                    .setTitle("提示").setContentMsg("没有可用相机")
                    .setNegativeBtn("确定", new CommonDialog.OnNegativeListener() {
                        @Override
                        public void onNegative(View view) {
                            finish();
                        }
                    }, false).show();
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(File uri) {

        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(uri), "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CAPTURE_ZOOM_CODE);

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
        Cursor cursor = getContentResolver().query(
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
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    // 调相册图片
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/jpeg");

        startActivityForResult(intent, CAPTURE_WALBUM_CODE);
    }


    // 调相机拍照
    private void getImageFromCamera() {
        String SDState = Environment.getExternalStorageState();
//        isSplit = true;
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                    .format(new Date()) + "camera.jpg";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            imageUrl = FileProviderHelper.getUriForFile(this, file);
            photoPath = file.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, CAPTURE_PHOTO_CODE);
        } else {
            ToastUtil.show(this, "内存卡不存在");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            switch (requestCode) {
                case CAPTURE_WALBUM_CODE:// 相册
                    Uri imageUri = data.getData();

                    if ("Xiaomi".equals(Build.MANUFACTURER) || SystemUtils.isMIUI())   //小米相册兼容代码
                    {
                        String imgP = setPhotoForMiuiSystem(data);

                        if (imageUri == null) {
                            Toast.makeText(ImageSelectActivity.this, "图片获取失败", Toast.LENGTH_SHORT);
                            finish();
                            return;
                        }
                        if (isSplit) {
                            startPhotoZoom(new File(imgP));
                            return;
                        }

                        if (!TextUtils.isEmpty(imgP)) {
                            setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, imgP));
                            finish();
                        }
                        return;
                    }
                    if (imageUri == null) {
                        Toast.makeText(ImageSelectActivity.this, "图片获取失败", Toast.LENGTH_SHORT);
                        finish();
                        return;
                    }

                    if (isSplit) {
                        startPhotoZoom(new File(imageUri.getPath()));
                    } else {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, picturePath));
                        finish();
                    }
                    break;
                case CAPTURE_PHOTO_CODE:// 拍照

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
                                            bitmap = decodeBitmapFromFile(imageUrl.getPath(), 480, 800);
                                        } else {
                                            bitmap = decodeBitmapFromFile(photoPath, 480, 800);
                                        }
                                        LogUtil.E("poto1");
                                        return bitmap;
                                    }
                                })
                                .observeOn(Schedulers.io())
                                .map(new Function<Bitmap, String>() {
                                    @Override
                                    public String apply(@NonNull Bitmap bitmap) throws Exception {
                                        String path = saveBitmapFile(bitmap, "camera");
                                        LogUtil.E("poto2");
                                        return path;
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        LogUtil.E("poto3");
                                        setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, s));
                                        finish();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(ImageSelectActivity.this, "图片获取失败", Toast.LENGTH_SHORT);
                                        finish();
                                    }
                                }));
                    }
                    break;
                case CAPTURE_ZOOM_CODE:  //图片裁剪
                    Bundle extras = data.getExtras();
                    Bitmap photo = extras.getParcelable("data");

                    mSubscription.add(Observable.just(photo)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(Schedulers.io())
                            .map(new Function<Bitmap, String>() {
                                @Override
                                public String apply(@NonNull Bitmap bitmap) throws Exception {
                                    String path = saveBitmapFile(bitmap, "split");  //图片名称
                                    return path;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String path) throws Exception {
                                    setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, path));
                                    finish();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(ImageSelectActivity.this, "图片获取失败", Toast.LENGTH_SHORT);
                                    finish();
                                }
                            }));

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(ImageSelectActivity.this, "图片获取失败", Toast.LENGTH_SHORT);
            finish();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            LogUtil.E("图片旋转角度异常" + e);
        }
        LogUtil.E("图片旋转角度" + degree);
        return degree;
    }

    /*
   * 旋转图片
   * @param angle
   * @param bitmap
   * @return Bitmap
   */
    public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        try {
            //旋转图片 动作
            Matrix matrix = new Matrix();
            ;
            matrix.postRotate(angle);
            // 创建新的图片
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizedBitmap;
        } catch (Exception e) {

        }
        return bitmap;
    }

    /**
     * 判断是否存在可用相机
     *
     * @return
     */
    public boolean hasCamera() {
        PackageManager packageManager = ImageSelectActivity.this.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 压缩Bitmap的大小
     *
     * @param imagePath     图片文件路径
     * @param requestWidth  压缩到想要的宽度
     * @param requestHeight 压缩到想要的高度
     * @return Bitmap
     */
    public Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (requestWidth <= 0 || requestHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                return bitmap;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    options.outWidth = width;
                    options.outHeight = height;

                } catch (IOException e) {
                }
            }

            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
//            options.inSampleSize = Math.min(options.outWidth / requestWidth, options.outHeight / requestHeight);

            options.inJustDecodeBounds = false;

            int degree = getBitmapDegree(imagePath);//获取旋转角度

            if (degree == 0) {
                return BitmapFactory.decodeFile(imagePath, options);
            } else {
                return rotaingImageView(degree, BitmapFactory.decodeFile(imagePath, options));
            }

        } else {
            LogUtil.E("拍照生成图片false");
            return null;
        }


    }

//    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        int height = options.outHeight;
//        int width = options.outWidth;
//        int inSampleSize = 1;
//        if (height > reqHeight || width > reqWidth) {
//            int halfHeight = height / 2;
//
//            for (int halfWidth = width / 2; halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth; inSampleSize *= 2) {
//            }
//
//            long totalPixels = (long) (width * height / inSampleSize);
//
//            for (long totalReqPixelsCap = (long) (reqWidth * reqHeight * 2); totalPixels > totalReqPixelsCap; totalPixels /= 2L) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

	/*
*
     * 相机权限申请
     * @param poto
*/

    @TargetApi(Build.VERSION_CODES.M)
    private void permissionCheck(int requestcode) {
        if (AppUtils.getAndroidVersion(Build.VERSION_CODES.M))  //如果运行环境是6.0
        {
            //判断是否有相机权限
            if (ContextCompat.checkSelfPermission(ImageSelectActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(ImageSelectActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(ImageSelectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //没有权限
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);  //申请相机权限

                return;
            }
        }
        if (requestcode == CAPTURE_PERMISSION_CODE) {
            //判断是否有可用相机
            startCamera();
        } else {
            getImageFromAlbum();
        }
    }


    //权限申请回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean isgetPermissions = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isgetPermissions = false;
                break;
            }
        }

        if (!isgetPermissions) {
            if (isFinishing()) {
                return;
            }
            new CommonDialog(this).builder()
                    .setTitle("系统提示").setContentMsg("未取得您的:\n1.相机。\n2.存储读写。\n使用权限，此功能无法使用。请前往应用权限授予以上权限。")

                    .setPositiveBtn("去打开", new CommonDialog.OnPositiveListener() {
                        @Override
                        public void onPositive(View view) {
                            // 根据包名跳转到系统自带的应用程序信息界面
                            AppUtils.startDetailsSetting(ImageSelectActivity.this);
                        }
                    })
                    .setNegativeBtn("取消", new CommonDialog.OnNegativeListener() {
                        @Override
                        public void onNegative(View view) {
                            ImageSelectActivity.this.finish();
                        }
                    }, false).show();

        } else {
            if (requestCode == CAPTURE_PERMISSION_CODE) {
                startCamera();  //启动相机
            } else {
                getImageFromAlbum(); //启动相册
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 保存bitmap 图片
     *
     * @param bitmap
     * @param imageName
     * @return
     */
    public String saveBitmapFile(Bitmap bitmap, String imageName) {

        File file1 = new File(getDirectory());
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                .format(new Date());
        String imagename = filename + imageName + ".jpg";
        File file = new File(file1, imagename);
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
        }

        return file.getPath();
    }


    /**
     * 获得缓存目录
     **/
    public String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        LogUtil.E("拍照图片路径" + dir);
        return dir;
    }

    /**
     * 取SD卡路径
     **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
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
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        return imagePath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.dispose();
            mSubscription.clear();
        }
    }
}
