package com.cdkj.baselibrary.utils.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.cdkj.baselibrary.utils.LogUtil;

import java.io.File;
import java.io.RandomAccessFile;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 更新包下载工具
 */
public class ApkLoaderUtil implements DownloadThread.DownloadThreadListener {

    private static final String FILE_NAME = "ylq.apk";
    private static final int THREAD_COUNT = 3; //下载线程数量

    private OkHttpClient mClient;
    private long mTotalCount;
    private long[] mLoadCounts = new long[THREAD_COUNT];
    private SparseArray<DownloadThread> mLoadThreads = new SparseArray<>();

    private APKDownloadListener apkDownloadListener;

    public static String getFilePath(Context context) {
        File file = context.getExternalFilesDir(null);
        if (file != null && file.exists()) {
            return file.getAbsolutePath() + File.separator + FILE_NAME;
        } else {
            file = context.getFilesDir();
            return file.getAbsolutePath() + File.separator + FILE_NAME;
        }
    }

    private static String getApkMd5(Context context) {
        String apk_path = getFilePath(context);
        File file = new File(apk_path);
        if (!file.exists()) {
            return "";
        }
        return UpdateCommonUtil.getFileMD5(file);
    }

    public static boolean checkApk(Context context, String md5) {
        return (!TextUtils.isEmpty(md5)) && md5.equals(getApkMd5(context));
    }

    public static void installApk(Context context) {
        UpdateCommonUtil.installApp(context, getFilePath(context));
    }

    public void loadApk(final String path, final String url, APKDownloadListener apkDownloadListener) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        this.apkDownloadListener = apkDownloadListener;

        if (mClient == null) {
            mClient = new OkHttpClient();
        }
        File file = new File(path);
        boolean isDelete = false;
        if (file.exists()) {
            isDelete = file.delete();
        }
        if (isDelete) {
        }
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).build();
                Response response;
                try {

                    response = mClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        setLoadError();
                        return;
                    }

                    ResponseBody responseBody = response.body();
                    mTotalCount = responseBody.contentLength();

                    if(mTotalCount<=0){
                        setLoadError();
                        return;
                    }

                    RandomAccessFile raf = new RandomAccessFile(path, "rwd");
                    raf.setLength(mTotalCount);
                    raf.close();
                    long blockSize = mTotalCount / THREAD_COUNT;
                    for (int i = 0; i < THREAD_COUNT; i++) {
                        long startIndex = i * blockSize;
                        long endIndex = (i + 1) * blockSize - 1;
                        if (i == THREAD_COUNT - 1) {
                            endIndex = mTotalCount;
                        }
                        DownloadThread thread = DownloadThread.getInstance(
                                i, url, path, startIndex, endIndex, ApkLoaderUtil.this);
                        mLoadCounts[i] = 0;
                        thread.setClient(mClient);
                        thread.start();
                        mLoadThreads.put(i, thread);
                    }
                } catch (Exception e) {
                    LogUtil.E("APKLoadError");
                    try {
                        sleep(500);
                        run();
                    } catch (InterruptedException e1) {
                        LogUtil.E("APKLoadError_2");
                    }

                }
            }
        }.start();
    }

    private void setProgress() {
        if (apkDownloadListener == null) return;

        long loadCount = 0;
        for (float p : mLoadCounts) {
            loadCount += p;
        }
        Observable.just(loadCount * 100f / mTotalCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float aFloat) throws Exception {
                        apkDownloadListener.loadProgress(aFloat);
                    }
                });
    }

    private void setCompeted() {
        if (apkDownloadListener == null) return;
        Observable.just(100f)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float integer) throws Exception {
                        apkDownloadListener.loadCompeted(integer);
                    }
                });
    }

    private void setLoadError() {
        if (apkDownloadListener == null) return;
        Observable.just(100f)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float integer) throws Exception {
                        apkDownloadListener.loadLoadError(0);
                    }
                });
    }

    @Override
    public void onDownloading(int id, long load, boolean isCompleted) {
        mLoadCounts[id] = load;
        if (isCompleted) {
            mLoadThreads.remove(id);
            if (mLoadThreads.size() == 0) {
                setCompeted();
                return;
            }
        }
        setProgress();
    }

    //错误处理
    @Override
    public void onError(int id, int errorCode) {
        setLoadError();
    }
}
