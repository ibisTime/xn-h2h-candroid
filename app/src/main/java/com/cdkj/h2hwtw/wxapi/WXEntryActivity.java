package com.cdkj.h2hwtw.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.utils.WxUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWx();
        inits();


    }

    private void initWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WxUtil.APPID, false);
        api.handleIntent(getIntent(), this);
    }

    private void inits() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法！
    @Override
    public void onResp(BaseResp resp) {

        System.out.print("resp.getType()="+resp.getType());

        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {// 分享
            String result = "";
            System.out.println("resp.errCode="+resp.errCode);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:

                    result = "分享成功";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "分享取消";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "分享拒绝";
                    break;
                default:
                    result = "分享失败";
                    break;
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }


}