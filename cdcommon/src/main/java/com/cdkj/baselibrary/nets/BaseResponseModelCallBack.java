package com.cdkj.baselibrary.nets;

import android.content.Context;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * 网络请求回调
 * Created by Administrator on 2016/9/3.
 */
public abstract class BaseResponseModelCallBack<T> implements Callback<BaseResponseModel<T>> {


    /*0=成功；1=权限错误；2=参数错误；3=业务错误；9=未知错误*/

    public static final String REQUESTOK = "0";   //请求后台成功

    public static final String REQUESTFECODE3 = "3";
    public static final String REQUESTFECODE2 = "2";

    public static final String REQUESTFECODE4 = "4";//重新登录

    public static final String REQUESTFECODE9 = "9";

    public static final String NET_ERROR = "-1";


    /**
     * 网络异常状态错误码
     */
    public static final String NETERRORCODE0 = "10";  //请求成功，但是服务器返回除1000外错误码
    public static final String NETERRORCODE1 = "11";  //网络异常
    public static final String NETERRORCODE2 = "12";  //响应超时
    public static final String NETERRORCODE3 = "13";  //连接超时
    public static final String NETERRORCODE4 = "14";  //其它错误

    private Context context;

    public BaseResponseModelCallBack(Context context) {
        SoftReference<Context> mS = new SoftReference<>(context);
        this.context = mS.get();
    }

    @Override
    public void onResponse(Call<BaseResponseModel<T>> call, Response<BaseResponseModel<T>> response) {

        onFinish();

        if (response == null || response.body() == null) {
            onNull();
            return;
        }

        if (response.isSuccessful()) {

            try {
                BaseResponseModel t = response.body();
                checkState(t);      //根据返回错误的状态码实现相应的操作
            } catch (Exception e) {
                if (LogUtil.isDeBug) {
                    onReqFailure(NETERRORCODE4, "未知错误" + e.toString());
                } else {
                    onReqFailure(NETERRORCODE4, "程序出现未知错误");
                }
            }

        } else {
            onReqFailure(NETERRORCODE4, "网络请求失败");
        }

    }

    @Override
    public void onFailure(Call<BaseResponseModel<T>> call, Throwable t) {

        if (call.isCanceled()) {                //如果是主动请求取消的就不执行
            return;
        }
        onFinish();
        if (!NetUtils.isNetworkConnected(CdApplication.getContext())) {
            onNoNet("暂无网络");
            return;
        }

        String errorString = "";

        String errorCode = NETERRORCODE4;

        if (t instanceof UnknownHostException) { // 网络错误
            errorString = "网络加载异常";
            errorCode = NETERRORCODE1;
        } else if (t instanceof SocketTimeoutException) {//响应超时
            errorString = "服务器响应超时";
            errorCode = NETERRORCODE2;
        } else if (t instanceof ConnectException) {//请求超时
            errorString = "网络请求超时";
            errorCode = NETERRORCODE3;
        } else if (t instanceof HttpException) {
            errorString = "网络异常";
            errorCode = NETERRORCODE1;
        } else {
            errorString = "未知错误";
            errorCode = NETERRORCODE4;
        }

        if (LogUtil.isDeBug) {
            errorString += t.toString();
        }

        onReqFailure(errorCode, errorString);

    }

    /**
     * 检查错误码
     *
     * @param baseModelNew 根据返回错误的状态码实现相应的操作
     */
    protected void checkState(BaseResponseModel baseModelNew) {

        String state = baseModelNew.getErrorCode();

        if (REQUESTOK.equals(state)) { //请求成功

            T t = (T) baseModelNew.getData();

            if (t == null) {
                onFinish();
                onNull();

                return;
            }

            onSuccess(t, baseModelNew.getErrorInfo());

        } else if (REQUESTFECODE4.equals(state)) {
            onLoginFailure(context, baseModelNew);
        } else {
            onReqFailure(state, baseModelNew.getErrorInfo());
        }
    }


    /**
     * 请求成功
     *
     * @param data
     */
    protected abstract void onSuccess(T data, String SucMessage);

    /**
     * 请求失败
     *
     * @param errorCode
     * @param errorMessage
     */
    protected abstract void onReqFailure(String errorCode, String errorMessage);

    /**
     * 重新登录
     *
     * @param
     */
    protected void onLoginFailure(Context context, BaseResponseModel baseModelNew) {
        OnOkFailure.StartDoFailure(context, baseModelNew.getErrorInfo());
    }


    /**
     * 请求数据为空
     */
    protected void onNull() {
        ToastUtil.show(context, "请求失败,数据返回错误");
    }

    /**
     * 请求结束 无论请求成功或者失败都会被调用
     */
    protected abstract void onFinish();

    /**
     * 无网络
     */
    protected void onNoNet(String msg) {
        ToastUtil.show(context, msg);
    }

}
