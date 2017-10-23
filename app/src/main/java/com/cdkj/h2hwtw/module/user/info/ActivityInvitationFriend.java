package com.cdkj.h2hwtw.module.user.info;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityInvitationFriendBinding;
import com.cdkj.h2hwtw.model.PutMoneySendModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 邀请好友
 * Created by 李先俊 on 2017/10/23.
 */

public class ActivityInvitationFriend extends AbsBaseLoadActivity {

    private ActivityInvitationFriendBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActivityInvitationFriend.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invitation_friend, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("邀请好友");
        mBaseBinding.titleView.setRightTitle("好友列表");
        getInfo(1, 1, true);
    }


    public void getInfo(int pageindex, int limit, final boolean isShowDialog) {
        Map<String, String> map = new HashMap();

        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("limit", pageindex + "");
        map.put("start", limit + "");
        map.put("status", "1");
        map.put("type", "3");
        map.put("orderColumn", "start_datetime");
        map.put("orderDir", "desc");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getActivitySendList("801050", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<PutMoneySendModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<PutMoneySendModel> data, String SucMessage) {
                if (data.getList() == null || data.getList().size() < 1) {
                    return;
                }
                if (data.getList().get(0) == null) return;
                mBinding.tvInfo.setText(data.getList().get(0).getDescription());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onNoNet(String msg) {

            }

            @Override
            protected void onFinish() {
                if (isShowDialog) disMissLoading();
            }
        });

    }
}
