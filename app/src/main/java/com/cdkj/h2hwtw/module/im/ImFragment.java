package com.cdkj.h2hwtw.module.im;

import android.text.TextUtils;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.BuildConfig;
import com.cdkj.h2hwtw.MyApplication;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.model.immodel.getTxKeyModel;
import com.cdkj.h2hwtw.other.TXImManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 消息
 * Created by cdkj on 2017/10/9.
 */

public class ImFragment extends BaseLazyFragment {


    private String mImsign;//

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static ImFragment getInstanse() {
        ImFragment fragment = new ImFragment();
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        getTxKeyRequest();
    }

    @Override
    protected void onInvisible() {

    }

    public void getTxKeyRequest() {

        if (!SPUtilHelpr.isLoginNoStart()) {
            return;
        }

        Map map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelpr.getUserId());

        Call<BaseResponseModel<getTxKeyModel>> call = RetrofitUtils.createApi(MyApiServer.class).getTxSing("805953", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<getTxKeyModel>(mActivity) {
            @Override
            protected void onSuccess(getTxKeyModel data, String SucMessage) {
                mImsign = data.getSig();
                TXImManager.getInstance().login(SPUtilHelpr.getUserId(), mImsign, new TXImManager.LoginBallBack() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess() {

                    }
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


}
