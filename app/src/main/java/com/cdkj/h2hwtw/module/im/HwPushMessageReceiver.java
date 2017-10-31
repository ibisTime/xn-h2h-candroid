//package com.cdkj.h2hwtw.module.im;
//
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.huawei.hms.support.api.push.PushEventReceiver;
//import com.huawei.hms.support.api.push.PushReceiver;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
///**
// * 华为推送接收
// */
//public class HwPushMessageReceiver extends PushEventReceiver{
//    private final String TAG = "HwPushMessageReceiver";
//    private String mToken = "";
//
//    @Override
//    public void onToken(Context context, String token, Bundle extras){
//        String belongId = extras.getString("belongId");
//        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
//
//        mToken = token;
//
//        Log.e(TAG, content);
//    }
//
//    @Override
//    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
//        try {
//            String content = "收到一条Push消息： " + new String(msg, "UTF-8");
//            Log.e(TAG, content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public void onEvent(Context context, PushReceiver.Event event, Bundle extras) {
//        if (PushReceiver.Event.NOTIFICATION_OPENED.equals(event) || PushReceiver.Event.NOTIFICATION_CLICK_BTN.equals(event)) {
//            int notifyId = extras.getInt(PushReceiver.BOUND_KEY.pushNotifyId, 0);
//            if (0 != notifyId) {
//                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.cancel(notifyId);
//            }
//            String content = "收到通知附加消息： " + extras.getString(PushReceiver.BOUND_KEY.pushMsgKey);
//            Log.e(TAG, content);
//            //获取ext内容
//            try {
//                JSONArray objArray = new JSONArray(extras.getString(PushReceiver.BOUND_KEY.pushMsgKey));
//                for(int i = 0; i < objArray.length(); i++){
//                    JSONObject obj = objArray.getJSONObject(i);
//                    if(obj.has("msgid")) {
//                        Log.e(TAG, "msgid:" + obj.getString("msgid"));
//                    }
//
//                    if(obj.has("ext")){
//                        Log.e(TAG, "ext:" + obj.getString("ext"));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
