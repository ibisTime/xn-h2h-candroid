package com.cdkj.baselibrary.utils.update;

/**
 * Created by 李先俊 on 2017/8/19.
 */

public interface APKDownloadListener {

    //加载中
    void loadProgress(float progress);

    //成功
    void loadCompeted(float progress);

    //失败
    void loadLoadError(float progress);

}
