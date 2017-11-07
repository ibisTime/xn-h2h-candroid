package com.cdkj.baselibrary.interfaces;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.model.LocationModel;
import com.cdkj.baselibrary.utils.LogUtil;

/**
 * 高德定位
 * Created by ckdj on 2017/11/7.
 */

public class GaoDeLocation implements LocationInterface {

    //定位功能
    protected AMapLocationClient locationClient = null;

    @Override
    public void init(final LocationCallBackListener listener) {
        //初始化client
        locationClient = new AMapLocationClient(CdApplication.getContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != aMapLocation && aMapLocation.getErrorCode() == 0) {  //定位成功
                    LogUtil.E("高德定位成功");
                    listener.locationSuccessful(transitionModel(aMapLocation));
                } else {
                    if (aMapLocation != null) {
                        LogUtil.E("高德定位失败" + aMapLocation.getErrorInfo());//定位失败
                        listener.locationFailure(aMapLocation.getErrorInfo());
                        return;
                    }
                    listener.locationFailure("定位失败");
                }
                //停止定位
                stopLocation();
            }
        });

    }

    @Override
    public void startLocation() {
        locationClient.startLocation();
    }

    @Override
    public void stopLocation() {
        locationClient.stopLocation();
    }

    @Override
    public void destroyLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    /**
     * 默认定位参数
     *
     * @return
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setLocationCacheEnable(false);  // 设置是否开启缓存
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    /**
     * 把高德定位model转换为 自用model
     *
     * @return
     */
    private LocationModel transitionModel(AMapLocation aMapLocation) {
        LocationModel locationModel = new LocationModel();
        locationModel.setCity(aMapLocation.getCity());
        locationModel.setProvince(aMapLocation.getProvince());
        locationModel.setDistrict(aMapLocation.getDistrict());
        locationModel.setLatitude(aMapLocation.getLatitude() + "");
        locationModel.setLongitude(aMapLocation.getLongitude() + "");
        return locationModel;
    }
}
