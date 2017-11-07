package com.cdkj.baselibrary.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.model.QiniuGetTokenModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;


/**
 * Created by LeiQ on 2016/12/29.
 */
// TODO 七牛工具类需要优化 压缩上传分离 使用方法优化
public class QiNiuUtil {

    private static final String ANDROID = "ANDROID";
    private static final String IOS = "IOS";
    private Context context;
    private int upLoadListIndex = 0; //多张图片上传索引

    public QiNiuUtil(Context context) {
        this.context = context;
    }

    /**
     * 图片单张上传
     *
     * @param callBack
     * @param url
     */
    public void uploadSingle(final QiNiuCallBack callBack, final String url, final String token) {

        if (url.indexOf(ANDROID) == -1 || url.indexOf(IOS) == -1) {

            Configuration config = new Configuration.Builder().build();
            final UploadManager uploadManager = new UploadManager(config);
            final String key = ANDROID + timestamp() + getImageWidthHeight(url) + ".jpg";

            Observable.just(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Function<String, byte[]>() {
                        @Override
                        public byte[] apply(@NonNull String s) throws Exception {
                            return BitmapUtils.compressImage(s);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<byte[]>() {
                        @Override
                        public void accept(byte[] bytes) throws Exception {

                            if (bytes == null || bytes.length == 0) {
                                callBack.onFal("图片上传失败");
                                return;
                            }

                            uploadManager.put(url, key, token,
                                    new UpCompletionHandler() {
                                        @Override
                                        public void complete(final String key, final ResponseInfo info, final JSONObject res) {

                                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                                            if (info != null && info.isOK()) {
                                                if (callBack != null) {

                                                    Observable.just("")
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Consumer<String>() {
                                                                @Override
                                                                public void accept(String s) throws Exception {
                                                                    callBack.onSuccess(key, info, res);
                                                                }
                                                            });
                                                }

                                            } else {
                                                if (callBack != null) {
                                                    Observable.just("token失败")
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Consumer<String>() {
                                                                @Override
                                                                public void accept(String s) throws Exception {
                                                                    callBack.onFal(s);
                                                                }
                                                            });
                                                }
                                                Log.i("QiNiu", "Upload Fail");
                                                Log.i("QiNiu", "key=" + key);
                                                Log.i("QiNiu", "res=" + res);
                                                Log.i("QiNiu", "info=" + info);
                                            }
                                        }
                                    }, null);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            callBack.onFal("图片上传失败");
                        }
                    });


        }

    }


    /**
     * 获取七牛token
     *
     * @return
     */
    public Call<BaseResponseModel<QiniuGetTokenModel>> getQiniuToeknRequest() {
        Map<String, String> object = new HashMap<>();
        object.put("companyCode", MyCdConfig.COMPANYCODE);
        object.put("systemCode", MyCdConfig.SYSTEMCODE);
        return RetrofitUtils.getBaseAPiService().getQiniuTOken("805951", StringUtils.getJsonToString(object));
    }

    /**
     * 获取七牛url
     *
     * @param callBack
     */
    public void getQiniuURL(final QiNiuCallBack callBack, final String filePath) {

        getQiniuToeknRequest().enqueue(new BaseResponseModelCallBack<QiniuGetTokenModel>(context) {
            @Override
            protected void onSuccess(QiniuGetTokenModel mo, String SucMessage) {
                if (mo == null || TextUtils.isEmpty(mo.getUploadToken()) || TextUtils.isEmpty(filePath)) {
                    if (callBack != null) {
                        callBack.onFal("图片上传失败");
                    }
                    return;
                }
                String token = mo.getUploadToken();

                try {
                    uploadSingle(callBack, filePath, token);
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.onFal("图片上传失败");
                    }
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                callBack.onFal("图片上传失败");
            }

            @Override
            protected void onNull() {
                callBack.onFal("图片上传失败");
            }

            @Override
            protected void onFinish() {

            }
        });

    }


    public int getUpLoadListIndex() {
        return upLoadListIndex;
    }

    public void setUpLoadListIndex(int upLoadListIndex) {
        this.upLoadListIndex = upLoadListIndex;
    }

    /**
     * 递归实现多图片上传
     *
     * @param dataList
     * @param token
     * @param listListener
     */
    public void upLoadListPic(final List<String> dataList, final String token, final upLoadListListener listListener) {

        if (TextUtils.isEmpty(token)) {
            if (listListener != null) {
                listListener.onFal("图片上传失败");
            }
            return;
        }

        Observable.just(dataList.get(upLoadListIndex))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, byte[]>() {
                    @Override
                    public byte[] apply(@NonNull String s) throws Exception {
                        return BitmapUtils.compressImage(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) throws Exception {
                        if (bytes == null || bytes.length == 0) {
                            if (listListener != null) {
                                listListener.onFal("图片上传失败" + upLoadListIndex);
                            }
                            return;
                        }
                        uploadSingle(new QiNiuCallBack() {
                            @Override
                            public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                                listListener.onChange(upLoadListIndex, key);
                                if (upLoadListIndex < dataList.size() - 1) {
                                    upLoadListIndex++;
                                    upLoadListPic(dataList, token, listListener);
                                } else {
                                    upLoadListIndex = 0;
                                    if (listListener != null) {
                                        listListener.onSuccess();
                                    }

                                }
                            }

                            @Override
                            public void onFal(String info) {
                                if (listListener != null) {
                                    listListener.onFal(info);
                                }
                            }
                        }, bytes, token);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (listListener != null) {
                            listListener.onError("出现未知错误");
                        }
                    }
                });

    }

    /**
     * 图片单张上传
     *
     * @param callBack
     * @param
     */
    public void uploadSingle(final QiNiuCallBack callBack, byte[] data, String token) {

        Configuration config = new Configuration.Builder().build();
        UploadManager uploadManager = new UploadManager(config);
        String key = ANDROID + timestamp() + ".jpg";

        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(final String key, final ResponseInfo info, final JSONObject res) {

                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info != null && info.isOK()) {
                            if (callBack != null) {
                                Observable.just("")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {
                                                callBack.onSuccess(key, info, res);
                                            }
                                        });
                            }

                        } else {
                            if (callBack != null) {
                                Observable.just("token失败")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {
                                                callBack.onFal(s);
                                            }
                                        });
                            }
                            Log.i("QiNiu", "Upload Fail");
                            Log.i("QiNiu", "key=" + key);
                            Log.i("QiNiu", "res=" + res);
                            Log.i("QiNiu", "info=" + info);
                        }
                    }
                }, null);

    }


    public static String getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        String imageWidth = options.outWidth + "";
        String imageHeight = options.outHeight + "";
        String size = "_" + imageWidth + "_" + imageHeight;

        System.out.print("size = _" + imageWidth + "_" + imageHeight);
        return size;
    }


    private static String timestamp() {
        String time = System.currentTimeMillis() + "";

        return "_" + time;
    }


    public interface QiNiuCallBack {
        void onSuccess(String key, ResponseInfo info, JSONObject res);

        void onFal(String info);
    }

    /**
     * 多图片上传
     */
    public interface upLoadListListener {

        void onChange(int index, String url);

        void onSuccess();

        void onFal(String info);

        void onError(String info);
    }


}
