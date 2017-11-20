package com.cdkj.baselibrary.interfaces;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;

/**
 * 定位辅助类 判断是否有定位权限
 * <p>
 * 使用注意 必须要调用onRequestPermissionsResult用于获取权限回调
 * <p>
 * 必须要在页面关闭时调用destroyLocation方法用于关闭一些东西 比如在Activity的onDestroy中调用
 * <p>
 * Created by cdkj on 2017/11/7.
 */

public class LocationHelper {

    private LocationInterface mLocationInterface;//定位实现接口

    private PermissionHelper mPreHelper;//权限请求

    private Object mContext;

    private LocationCallBackListener mLocationCallBackListener;//定位回调

    //需要进行定位功能的权限
    private String[] needLocationPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public LocationHelper(@NonNull Activity obj, @NonNull LocationInterface locationInterface, @NonNull LocationCallBackListener locationCallBackListener) {
        this.mContext = obj;
        this.mLocationInterface = locationInterface;
        this.mLocationInterface.init(locationCallBackListener);
        this.mLocationCallBackListener = locationCallBackListener;
        mPreHelper = new PermissionHelper(mContext);
    }

    public LocationHelper(@NonNull Fragment obj, @NonNull LocationInterface locationInterface, @NonNull LocationCallBackListener locationCallBackListener) {
        this.mContext = obj;
        this.mLocationInterface = locationInterface;
        this.mLocationInterface.init(locationCallBackListener);
        this.mLocationCallBackListener = locationCallBackListener;
        mPreHelper = new PermissionHelper(mContext);
    }

    /**
     * 启动定位
     */
    public void startLocation() {
        if (AppUtils.getAndroidVersion(Build.VERSION_CODES.M)) {
            requestPermissions();
            return;
        }

        mLocationInterface.startLocation();
    }

    /**
     * 请求定位权限
     */
    private void requestPermissions() {
        mPreHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                mLocationInterface.startLocation();
            }

            @Override
            public void doAfterDenied(String... permission) {
                mLocationCallBackListener.noPermissions();
            }
        }, needLocationPermissions);
    }

    /**
     * 关闭定位
     */
    public void stopLocation() {
        mLocationInterface.stopLocation();
    }

    /**
     * 关闭一些内容
     */
    public void destroyLocation() {
        mLocationInterface.destroyLocation();
    }

    /**
     * 获取权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPreHelper == null) return;
        mPreHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
