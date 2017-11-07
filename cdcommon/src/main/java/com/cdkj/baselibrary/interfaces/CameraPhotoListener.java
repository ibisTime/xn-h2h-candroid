package com.cdkj.baselibrary.interfaces;

/**
 * 启用相机相册监听
 * Created by cdkj on 2017/11/7.
 */

public interface CameraPhotoListener {

    void onPhotoSuccessful(String path);

    void onPhotoFailure(String msg);

    void noPermissions();//没有权限

}
