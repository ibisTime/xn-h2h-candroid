package com.cdkj.baselibrary.nets;


import com.cdkj.baselibrary.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Okhttp OkHttpClient 使用封装
 * Created by Administrator on 2016-09-05.
 */
public class OkHttpUtils {

    private final static int CONNECT_TIMEOUT = 35;//连接超时
    private final static int READ_TIMEOUT = 35;//数据返回超时
    private final static int WRITE_TIMEOUT = 35;//请求超时


    private OkHttpUtils() {
    }

    private static OkHttpClient client;

    /**
     * 获取 OkHttpClient 对象
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getInstance() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//允许失败重试
                    .cookieJar(new CookiesManager())  //cookie 管理
                    .addInterceptor(getInterceptor(LogUtil.isDeBug))    //网络日志
                    .build();
        }

        return client;
    }


    /**
     * 网络请求拦截器
     *
     * @param isShow 控制请求日志的显示
     * @return interceptor
     */
    private static HttpLoggingInterceptor getInterceptor(boolean isShow) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    LogUtil.I("okhttp: " + URLDecoder.decode(message, "utf-8")); //post请求FormUrlEncoded注解 unicode 转码
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    LogUtil.I("okhttp log error: " + message);
                }
            }
        });

        if (isShow) {
            interceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }


        return interceptor;
    }


}
