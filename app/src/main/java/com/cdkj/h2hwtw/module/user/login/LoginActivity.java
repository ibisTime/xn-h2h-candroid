package com.cdkj.h2hwtw.module.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.activitys.FindPwdActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.RouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.MainActivity;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityLoginBinding;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.model.immodel.getTxKeyModel;
import com.cdkj.h2hwtw.module.im.TxImLogingActivity;
import com.cdkj.h2hwtw.other.TXImManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.LOGINREFRESH;

/**
 * 登录
 * Created by cdkj on 2017/8/8.
 */
@Route(path = RouteHelper.APPLOGIN)
public class LoginActivity extends AbsBaseLoadActivity implements LoginInterface {

    private LoginPresenter mPresenter;
    private ActivityLoginBinding mBinding;
    private boolean canOpenMain;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, boolean canOpenMain) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("canOpenMain", canOpenMain);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mPresenter = new LoginPresenter(this);

        if (getIntent() != null) {
            canOpenMain = getIntent().getBooleanExtra("canOpenMain", false);
        }

        //登录
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login(mBinding.editUsername.getText().toString(), mBinding.editUserpass.getText().toString(), LoginActivity.this);
            }
        });
        mBinding.tvStartRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.open(LoginActivity.this);
            }
        });

        mBinding.tvFindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPwdActivity.open(LoginActivity.this, "");
            }
        });

    }

    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {

        SPUtilHelpr.saveUserId(user.getUserId());
        SPUtilHelpr.saveUserToken(user.getToken());
        SPUtilHelpr.saveUserPhoneNum(mBinding.editUsername.getText().toString());
//TODO 登录方法 TxImLogingActivity 合并
//        TxImLogingActivity.open(this,null, canOpenMain, true);
        getUserInfoRequest(false);//登录--> 获取用户信息 -->获取腾讯签名-->登录腾讯--->登录成功
    }

    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoading();
        UITipDialog.showFall(LoginActivity.this, msg);
    }

    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }

    @Override
    protected boolean canFinish() {
        if (canOpenMain) {
            MainActivity.open(this);
            finish();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (canOpenMain) {
            MainActivity.open(this);
            finish();
        } else {
            super.onBackPressed();
        }

    }


    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                SPUtilHelpr.saveisTradepwdFlag(data.isTradepwdFlag());
                SPUtilHelpr.saveUserPhoneNum(data.getMobile());
                SPUtilHelpr.saveUserName(data.getNickname());
                SPUtilHelpr.saveUserPhoto(data.getPhoto());
                getTxKeyRequest();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                UITipDialog.showFall(LoginActivity.this, errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                super.onNoNet(msg);
                disMissLoading();
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


    /**
     * 获取腾讯签名
     */
    public void getTxKeyRequest() {


        Map map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelpr.getUserId());

        Call<BaseResponseModel<getTxKeyModel>> call = RetrofitUtils.createApi(MyApiServer.class).getTxSing("805953", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<getTxKeyModel>(this) {
            @Override
            protected void onSuccess(getTxKeyModel data, String SucMessage) {

                TXImManager.getInstance().init(data.getTxAppCode());

                TXImManager.getInstance().login(SPUtilHelpr.getUserId(), data.getSig(), new TXImManager.LoginBallBack() {
                    @Override
                    public void onError(int i, String s) {
                        disMissLoading();
                        UITipDialog.showFall(LoginActivity.this, "登录失败");
                    }

                    @Override
                    public void onSuccess() {
                        if (canOpenMain) {
                            EventBus.getDefault().post(EventTags.MAINFINISH);
                            EventBus.getDefault().post(EventTags.AllFINISH);
                            MainActivity.open(LoginActivity.this);
                        } else {
                            EventBus.getDefault().post(LOGINREFRESH);
                        }

                        disMissLoading();
                        finish();
                    }
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                UITipDialog.showFall(LoginActivity.this, "登录失败");
            }

            @Override
            protected void onNoNet(String msg) {
                super.onNoNet(msg);
                disMissLoading();
            }

            @Override
            protected void onFinish() {

            }
        });
    }


}
