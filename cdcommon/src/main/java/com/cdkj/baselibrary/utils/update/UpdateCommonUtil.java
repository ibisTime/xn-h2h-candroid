package com.cdkj.baselibrary.utils.update;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.FileProviderHelper;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by xiaofeng on 2016/11/22.
 */

public class UpdateCommonUtil {

    private UpdateCommonUtil() {

    }

    /*
     * 安装apk
     */
    public static void installApp(Context context, String filePath) {

        try {
            if (TextUtils.isEmpty(filePath) || context==null) return;
            File file = new File(filePath);
            if (!file.exists()) return;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            FileProviderHelper.setIntentDataAndType(context,intent,"application/vnd.android.package-archive",file,true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            ToastUtil.show(CdApplication.getContext(),"安装失败");
        }

    }


    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        String md5 = bigInt.toString(16);
        //不满足32位补0
        if (md5.length() < 32) {
            for (int i = 0; i < 32 - md5.length(); i++) {
                md5 = "0" + md5;
            }
        }
        return md5;
    }
}
