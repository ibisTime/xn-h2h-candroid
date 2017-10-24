package com.cdkj.baselibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.activitys.ShareActivity;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.cdkj.baselibrary.R.id.img;

/**
 * Created by LeiQ on 2017/1/10.
 */

public class WxUtil {

    private static IWXAPI api;
    //TODO 微信分享APPID
    public static final String APPID = "wx763220fe7a9672c0";

    public static IWXAPI registToWx(Context context) {
        api = WXAPIFactory.createWXAPI(context, APPID, false);
        api.registerApp(APPID);
        return api;
    }

    /**
     * 检测是否有微信与是否支持微信支付
     *
     * @return
     */
    public static boolean check(Context context) {

        api = registToWx(context);

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        boolean isPaySupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "亲，您要先安装微信才能使用微信分享哦！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return isPaySupported;
    }

    /**
     * 分享到朋友圈
     *
     * @param
     */
    public static void shareToPYQ(Context context, String url, String title, String description) {
        System.out.println("shareURL=" + url);
        if (!check(context)) return;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.share_icon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 100, 100, true);
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 分享微信聊天界面
     *
     * @param
     */
    public static void shareToWX(Context context, String url, String title, String description) {
        if (!check(context)) return;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.share_icon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 100, 100, true);
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


}
