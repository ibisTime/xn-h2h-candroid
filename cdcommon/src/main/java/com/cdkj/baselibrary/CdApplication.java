
package com.cdkj.baselibrary;

import android.app.Application;
import android.content.Context;

import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;

public class CdApplication extends Application {
    /**
     * Global application context.
     */
    private static Context sContext;

    /**
     * Construct of LitePalApplication. Initialize application context.
     */
    public CdApplication() {
        sContext = this;
    }

    public static void initialize(Context context, boolean isDebug, String urlType) {
        LogUtil.isDeBug = isDebug;
        RetrofitUtils.urlType = urlType;
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }

}
