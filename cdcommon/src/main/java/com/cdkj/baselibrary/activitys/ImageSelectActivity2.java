package com.cdkj.baselibrary.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.interfaces.CameraPhotoListener;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.ToastUtil;

/**
 * 打开相机 相册 图片裁剪 功能
 */
public class ImageSelectActivity2 extends Activity implements View.OnClickListener, CameraPhotoListener {

    private TextView tv_take_capture;// 拍照
    private TextView tv_alumb;// 相册选取
    private TextView tv_cancle;// 取消
    private View empty_view;// 取消

    private boolean isSplit = false;//执行相机或拍照后是否需要裁剪 默认不需要

    public static final int SHOWPIC = 1; //显示拍照按钮
    public static final int SHOWALBUM = 2;//显示相册

    private CameraHelper cameraHelper;

    public static void launch(Activity activity, int photoid, int showType, boolean isSplit) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageSelectActivity2.class);
        intent.putExtra("showType", showType);
        intent.putExtra("isSplit", isSplit);
        activity.startActivityForResult(intent, photoid);
    }

    public static void launch(Activity activity, int photoid) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageSelectActivity2.class);
        activity.startActivityForResult(intent, photoid);
    }

    public static void launchFragment(Fragment fragment, int photoid) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), ImageSelectActivity2.class);
        fragment.startActivityForResult(intent, photoid);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        initLayout();
        initVar();
    }

    private void initVar() {
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
        cameraHelper = new CameraHelper(this, isSplit, this);
    }

    protected void initLayout() {
        tv_take_capture = (TextView) findViewById(R.id.tv_take_capture);
        tv_alumb = (TextView) findViewById(R.id.tv_alumb);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        empty_view = findViewById(R.id.empty_view);

        tv_take_capture.setOnClickListener(this);
        tv_alumb.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        empty_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_take_capture) {
            cameraHelper.startCamera();
        } else if (i == R.id.tv_alumb) {
            cameraHelper.startAlbum();
        } else if (i == R.id.empty_view || i == R.id.tv_cancle) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //拍照回调
        cameraHelper.onActivityResult(requestCode, resultCode, data);
    }


    //权限申请回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraHelper != null) {
            cameraHelper.clear();
        }
    }

    @Override
    public void onPhotoSuccessful(String path) {
        setResult(Activity.RESULT_OK, new Intent().putExtra(CameraHelper.staticPath, path));
        finish();
    }

    @Override
    public void onPhotoFailure(String msg) {
        ToastUtil.show(this, msg);
        finish();
    }

    @Override
    public void noPermissions() {
        ToastUtil.show(this, "没有获取到权限，无法使用相机和相册功能");
    }
}
