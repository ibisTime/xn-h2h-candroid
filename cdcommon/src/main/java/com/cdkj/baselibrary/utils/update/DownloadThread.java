package com.cdkj.baselibrary.utils.update;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载线程
 */
public class DownloadThread extends Thread {

    private static final int UPDATE_SIZE = 20 * 1024;

    private OkHttpClient client;
    private int id;
    private long startIndex;
    private long endIndex;
    private long loadIndex;

    private String url;
    private String path;
    private RandomAccessFile file;

    private boolean isCancel = false;

    private DownloadThreadListener listener;

    public static DownloadThread getInstance(
            int id, String url, String path, long startIndex, long endIndex,
            DownloadThreadListener listener) {
        return new DownloadThread(id, url, path, startIndex, endIndex, listener);
    }

    private DownloadThread(
            int id, String url, String path, long startIndex, long endIndex,
            DownloadThreadListener listener) {
        this.id = id;
        this.url = url;
        this.path = path;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.loadIndex = startIndex;
        this.listener = listener;
        try {
            file = new RandomAccessFile(path, "rwd");
        } catch (FileNotFoundException e) {
        }
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .header("RANGE", "bytes=" + loadIndex + "-" + endIndex)
                    .build();
            file.seek(loadIndex);
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                inputStream = responseBody.byteStream();
                bis = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[10 * 1024];
                int length;
                int buffOffset = 0;
                while (!isCancel && (length = bis.read(buffer)) > 0) {
                    file.write(buffer, 0, length);
                    loadIndex += length;
                    buffOffset += length;
                    if (buffOffset >= UPDATE_SIZE) {
                        buffOffset = 0;
                        onDownloading();
                    }
                }
                onDownloading();
            }
        } catch (FileNotFoundException e) {
            onError(DownloadThreadListener.DOWNLOAD_ERROR_FILE_NOT_FOUND);
        } catch (IOException e) {
            //用于切换网络导致的下载异常，只处理一次
            try {
                sleep(500);
                run();
            } catch (Exception e1) {
            }
        } finally {
            try {
                if (bis != null) bis.close();
                if (inputStream != null) inputStream.close();
                if (file != null) file.close();
            } catch (IOException e) {
            }
        }
    }

    public void cancel() {
        isCancel = true;
    }

    private void onDownloading() {
        Log.i("xyz", "thread" + id + ":loading");
        if (listener != null) {
            listener.onDownloading(id, loadIndex - startIndex, loadIndex >= endIndex);
            if (loadIndex >= endIndex) {
                isCancel = true;
            }
        }
    }

    private void onError(int errorCode) {
        if (listener != null) {
            listener.onError(id, errorCode);
        }
    }

    public interface DownloadThreadListener {
        void onDownloading(int id, long load, boolean isCompleted);

        void onError(int id, int errorCode);

        int DOWNLOAD_ERROR_FILE_NOT_FOUND = -1;
        int DOWNLOAD_ERROR_IO_ERROR = -2;
    }
}
