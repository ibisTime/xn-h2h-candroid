package com.cdkj.h2hwtw.module.user.info;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityInfoEditBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import retrofit2.Call;

/**
 * 我的信息内容输入修改（个人简介）
 * Created by 李先俊 on 2017/6/16.
 */

public class UserInfoUpdateActivity extends AbsBaseLoadActivity {

    private ActivityInfoEditBinding mBinding;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context,String info) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserInfoUpdateActivity.class);

        intent.putExtra("info",info);

        context.startActivity(intent);
    }

    @Override
    public void topTitleViewRightClick() {

        if (TextUtils.isEmpty(mBinding.editInfo.getText().toString())) {
            showToast("请输入个人简介");
            return;
        }

        updateUserInfo();
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_info_edit, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mBinding.editInfo.setText(getIntent().getStringExtra("info"));
            mBinding.editInfo.setSelection(mBinding.editInfo.getText().toString().length());
        }
        mBaseBinding.titleView.setMidTitle("个人简介");
        mBaseBinding.titleView.setRightTitle("确认");
    }

    /**
     * 修改用户昵称
     */
    public void updateUserInfo() {
        Map<String, String> map = RetrofitUtils.getRequestMap();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("introduce", mBinding.editInfo.getText().toString());
        map.put("token", SPUtilHelpr.getUserToken());

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805098", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    EventBusModel e = new EventBusModel();
                    e.setTag(EventTags.USERNAMEEDITREFRESH2);
                    e.setEvInfo(mBinding.editInfo.getText().toString());
                    EventBus.getDefault().post(e);
                    UITipDialog.showSuccess(UserInfoUpdateActivity.this, "个人简介设置成功", new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            finish();
                        }
                    });

                } else {
                    UITipDialog.showFall(UserInfoUpdateActivity.this, "操作失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(UserInfoUpdateActivity.this, errorCode);
            }

            @Override
            protected void onNoNet(String msg) {
                super.onNoNet(msg);
                disMissLoading();
            }

            @Override
            protected void onNull() {
                disMissLoading();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}
