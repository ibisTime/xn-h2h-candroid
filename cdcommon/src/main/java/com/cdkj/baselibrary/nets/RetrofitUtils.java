package com.cdkj.baselibrary.nets;


import com.cdkj.baselibrary.api.BaseApiServer;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * 服务器api
 * Created by Administrator on 2016/9/1.
 */
public class RetrofitUtils {

    private static Retrofit retrofitInstance = null;

    public static String urlType;

    private RetrofitUtils() {
    }

    /**
     * 获取Retrofit实例
     *
     * @return Retrofit
     */
    private static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(getBaseURL())
                    .client(OkHttpUtils.getInstance())
                    .addConverterFactory(FastJsonConVerter.create())
                    .build();
        }
        return retrofitInstance;
    }

    /**
     * 创建Retrofit请求Api
     *
     * @param clazz Retrofit Api接口
     * @return api实例
     */
    public static <T> T createApi(Class<T> clazz) {
        return getInstance().create(clazz);
    }

    public static BaseApiServer getBaseAPiService() {
        return createApi(BaseApiServer.class);
    }

    /**
     * 获取URL  根据版本切换不同版本
     *
     * @return
     */
    public static String getBaseURL() {

        return "http://121.43.101.148:3901/forward-service/";//研发环境
//        return "http://118.178.124.16:3901/forward-service/";//测试
    }

    /**
     * 添加公共请求参数
     * @return
     */
    public static Map getRequestMap() {
        Map map = new HashMap();
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        return map;
    }

}
