package com.cdkj.baselibrary.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.model.QiniuGetTokenModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Call;


/**
 * Created by LeiQ on 2016/12/29.
 */
// TODO 七牛工具类需要优化 压缩上传分离 使用方法优化
public class QiNiuUtil {

    private static final String ANDROID = "ANDROID";
    private static final String IOS = "IOS";
    private String token = "";
    private Context context;

    static String size = "";
    static String imageWidth = "";
    static String imageHeight = "";


    public QiNiuUtil(Context context) {
        this.context = context;
    }

    /**
     * 图片单张上传
     *
     * @param callBack
     * @param url
     */
    public void uploadSingle(final QiNiuCallBack callBack, String url, String token) {

        if (url.indexOf(ANDROID) == -1 || url.indexOf(IOS) == -1) {

            Configuration config = new Configuration.Builder().build();
            UploadManager uploadManager = new UploadManager(config);
            String key = ANDROID + timestamp() + getImageWidthHeight(url) + ".jpg";


            LogUtil.E("图片");
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
    public void getQiniuURL(final QiNiuCallBack callBack, final String data) {

        getQiniuToeknRequest().enqueue(new BaseResponseModelCallBack<QiniuGetTokenModel>(context) {
            @Override
            protected void onSuccess(QiniuGetTokenModel mo, String SucMessage) {
                if (mo == null || TextUtils.isEmpty(mo.getUploadToken())) {
                    return;
                }
                token = mo.getUploadToken();

                try {
                    compressorUpload(callBack, data, token);
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.onFal("图片上传失败");
                    }
                }
            }

            @Override
            protected void onBuinessFailure(String code, String error) {
                callBack.onFal("图片上传失败");
            }

            @Override
            protected void onReqFailure(int errorCode, String errorMessage) {
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


    //多张图片上传
    public void updataeImage(List<String> dataList, String mToekn, QiNiuCallBack callBack) {

        for (int i = 0; i < dataList.size(); i++) {
            String imgPath = dataList.get(i);
            if (TextUtils.isEmpty(imgPath)) {
                continue;
            }

            try {

                compressorUpload(callBack, imgPath, mToekn);
            } catch (Exception e) {
                if (callBack != null) {
                    callBack.onFal("图片上传失败");
                }
            }

        }

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
        imageWidth = options.outWidth + "";
        imageHeight = options.outHeight + "";
        size = "_" + imageWidth + "_" + imageHeight;

        System.out.print("size = _" + imageWidth + "_" + imageHeight);
        return size;
    }


    private static String timestamp() {
        String time = System.currentTimeMillis() + "";

        return "_" + time;
    }


    public void compressorUpload(QiNiuCallBack callBack, String data, String token) {
        try {
            File compressedImageFile = Compressor.getDefault(context).compressToFile(new File(data));
            uploadSingle(callBack, compressedImageFile.getAbsolutePath(), token);
        } catch (Exception e) {
            if(callBack!=null){
                callBack.onFal("图片上传失败");
            }
        }
    }

    public interface QiNiuCallBack {
        void onSuccess(String key, ResponseInfo info, JSONObject res);

        void onFal(String info);
    }


}
