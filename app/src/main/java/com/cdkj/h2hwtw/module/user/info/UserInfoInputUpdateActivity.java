package com.cdkj.h2hwtw.module.user.info;

import android.content.Context;
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
import com.cdkj.h2hwtw.databinding.ActivityUserinfoUpdateInputBinding;
import com.cdkj.h2hwtw.other.TXImManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的信息内容输入修改
 * Created by 李先俊 on 2017/6/16.
 */

public class UserInfoInputUpdateActivity extends AbsBaseLoadActivity {

    private ActivityUserinfoUpdateInputBinding mBinding;

    private int mType;

    public static final int TYPE_NAME = 1;//昵称


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String info, int opentype) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserInfoInputUpdateActivity.class);

        intent.putExtra("info", info);
        intent.putExtra("opentype", opentype);

        context.startActivity(intent);
    }

    @Override
    public void topTitleViewRightClick() {

        if (TextUtils.isEmpty(mBinding.edit.getText().toString())) {
            showToast("请输入昵称");
            return;
        }

        updateUserInfo();
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_userinfo_update_input, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {


        if (getIntent() != null) {
            mBinding.edit.setText(getIntent().getStringExtra("info"));
            mBinding.edit.setSelection(mBinding.edit.getText().toString().length());
            mType = getIntent().getIntExtra("opentype", TYPE_NAME);
        }
        mBaseBinding.titleView.setMidTitle("修改昵称");
        mBaseBinding.titleView.setRightTitle("确认");
    }

    /**
     * 修改用户昵称
     */
    public void updateUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("nickname", mBinding.edit.getText().toString());
        map.put("token", SPUtilHelpr.getUserToken());

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805082", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    TXImManager.getInstance().setUserNickName(mBinding.edit.getText().toString(), new TXImManager.changeInfoBallBack() {
                        @Override
                        public void onError(int i, String s) {
                            disMissLoading();
                            EventBusModel e = new EventBusModel();
                            e.setTag(EventTags.USERNAMEEDITREFRESH);
                            e.setEvInfo(mBinding.edit.getText().toString());
                            EventBus.getDefault().post(e);
                            finish();
                        }

                        @Override
                        public void onSuccess() {
                            disMissLoading();
                            EventBusModel e = new EventBusModel();
                            e.setTag(EventTags.USERNAMEEDITREFRESH);
                            e.setEvInfo(mBinding.edit.getText().toString());
                            EventBus.getDefault().post(e);
                            finish();
                        }
                    });


                } else {
                    UITipDialog.showFall(UserInfoInputUpdateActivity.this, "操作失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(UserInfoInputUpdateActivity.this, errorCode);
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
