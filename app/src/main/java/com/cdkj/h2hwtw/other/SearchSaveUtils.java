package com.cdkj.h2hwtw.other;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.SPUtils;
import com.cdkj.h2hwtw.MyApplication;

/**
 * 搜索内容保存
 * Created by cdkj on 2017/10/24.
 */

public class SearchSaveUtils {

    /**
     * 保存用户搜索的内容
     *
     * @param info
     */
    public static void saveSearchInfo(String info) {
        SPUtils.put(MyApplication.getInstance(), "SearchInfo", info);
    }

    /**
     * 获取用户搜索的内容
     */
    public static String getSaveSearchInfo() {
        return SPUtils.getString(MyApplication.getInstance(), "SearchInfo", "暂无");
    }

    /**
     * 清除用户搜索的内容
     */
    public static void clearSearchInfo() {
        SPUtils.remove(MyApplication.getInstance(), "SearchInfo");
    }


}
