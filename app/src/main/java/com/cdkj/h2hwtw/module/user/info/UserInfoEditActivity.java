package com.cdkj.h2hwtw.module.user.info;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.activitys.ImageSelectActivity2;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.QiNiuUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityUserInfoEditBinding;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.other.TXImManager;
import com.qiniu.android.http.ResponseInfo;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的信息编辑
 * Created by 李先俊 on 2017/6/16.
 */

public class UserInfoEditActivity extends AbsBaseLoadActivity {

    private ActivityUserInfoEditBinding mBinding;

    private UserInfoModel mData;

    public final int PHOTOFLAG = 110;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, UserInfoModel data) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserInfoEditActivity.class);

        intent.putExtra("data", data);

        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_info_edit, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("个人资料");

        if (getIntent() != null) {
            mData = getIntent().getParcelableExtra("data");
        }
        setShowData();

        setListener();

    }

    private void setListener() {
        //修改头像
        mBinding.linLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelectActivity2.launch(UserInfoEditActivity.this, PHOTOFLAG);
            }
        });
        //修改昵称
        mBinding.linName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserNickNameUpdateActivity.open(UserInfoEditActivity.this, mBinding.tvName.getText().toString(), UserNickNameUpdateActivity.TYPE_NAME);
            }
        });
        //修改性别
        mBinding.linGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonDialog commonDialog = new CommonDialog(UserInfoEditActivity.this).builder()
                        .setTitle("提示").setContentMsg("请选择性别")
                        .setPositiveBtn("女", new CommonDialog.OnPositiveListener() {
                            @Override
                            public void onPositive(View view) {
                                updateGender(MyCdConfig.GENDERWOMAN);

                            }
                        })
                        .setNegativeBtn("男", new CommonDialog.OnNegativeListener() {
                            @Override
                            public void onNegative(View view) {
                                updateGender(MyCdConfig.GENDERMAN);
                            }
                        }, false);

                commonDialog.show();
            }
        });
        //修改生日
        mBinding.linBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendarNow = Calendar.getInstance();
                int year = calendarNow.get(Calendar.YEAR);
                int monthOfYear = calendarNow.get(Calendar.MONTH);
                int dayOfMonth = calendarNow.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserInfoEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DATE, dayOfMonth);

                        if (DateUtil.isNewer(cal.getTime(), calendarNow.getTime())) {
                            UITipDialog.showInfo(UserInfoEditActivity.this, "请选择正确的生日");
                            return;
                        }

                        updateBirthdy(DateUtil.format(cal.getTime(), DateUtil.DATE_YMD));

                    }


                }, year, monthOfYear, dayOfMonth);

                datePickerDialog.show();

            }
        });

        //个人简介
        mBinding.linUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData == null) return;
                UserInfoUpdateActivity.open(UserInfoEditActivity.this, mData.getIntroduce());
            }
        });
    }

    /**
     * 更新生日
     *
     * @param s
     */
    private void updateBirthdy(final String s) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("birthday", s);
        map.put("token", SPUtilHelpr.getUserToken());

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805096", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    mData.setBirthday(s);
                    mBinding.tvBirthday.setText(s);
                } else {
                    UITipDialog.showFall(UserInfoEditActivity.this, "操作失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(UserInfoEditActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 更新生日
     *
     * @param s
     */
    private void updateGender(final String s) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("gender", s);
        map.put("token", SPUtilHelpr.getUserToken());

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805097", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    mData.setGender(s);

                    mBinding.tvGender.setText(TextUtils.equals(s, MyCdConfig.GENDERWOMAN) ? "女" : "男");
                } else {
                    UITipDialog.showFall(UserInfoEditActivity.this, "操作失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(UserInfoEditActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PHOTOFLAG) {
            String path = data.getStringExtra(ImageSelectActivity.staticPath);
            LogUtil.E("拍照获取路径" + path);
            showLoadingDialog();
            new QiNiuUtil(UserInfoEditActivity.this).getQiniuURL(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    updateUserPhoto(key);
                }

                @Override
                public void onFal(String info) {
                    disMissLoading();
                    UITipDialog.showFall(UserInfoEditActivity.this, info);
                }
            }, path);

        }

    }


    /**
     * 更新头像
     *
     * @param key
     */
    private void updateUserPhoto(final String key) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("photo", key);
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getJsonToString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(UserInfoEditActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (!data.isSuccess()) {
                    disMissLoading();
                    UITipDialog.showSuccess(UserInfoEditActivity.this, "头像上传失败");
                    return;
                }

                TXImManager.getInstance().setUserLogo(key, new TXImManager.changeInfoBallBack() {
                    @Override
                    public void onError(int i, String s) {
                        disMissLoading();
                        UITipDialog.showSuccess(UserInfoEditActivity.this, "头像上传成功");
                        ImgUtils.loadLogo(UserInfoEditActivity.this, MyCdConfig.QINIUURL + key, mBinding.imgLogo);

                    }

                    @Override
                    public void onSuccess() {
                        disMissLoading();
                        UITipDialog.showSuccess(UserInfoEditActivity.this, "头像上传成功");
                        ImgUtils.loadLogo(UserInfoEditActivity.this, MyCdConfig.QINIUURL + key, mBinding.imgLogo);
                    }
                });

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                UITipDialog.showFall(UserInfoEditActivity.this, errorMessage);
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

            }
        });
    }


    private void setShowData() {
        if (mData == null) {
            return;
        }

        ImgUtils.loadLogo(this, MyCdConfig.QINIUURL + mData.getPhoto(), mBinding.imgLogo);

        mBinding.tvInfo.setText(mData.getIntroduce());

        mBinding.tvName.setText(mData.getNickname());
        if (mData != null) {
            mBinding.tvBirthday.setText(mData.getBirthday());
            if (MyCdConfig.GENDERMAN.equals(mData.getGender())) {
                mBinding.tvGender.setText("男");
            } else if (MyCdConfig.GENDERWOMAN.equals(mData.getGender())) {
                mBinding.tvGender.setText("女");
            }
            mBinding.edit.setText(mData.getIntroduce());
        }

    }


    @Subscribe
    public void refeshEvent(EventBusModel e) {
        if (e == null) {
            return;
        }
        if (TextUtils.equals(EventTags.USERNAMEEDITREFRESH, e.getTag()))//刷新用户数据
        {
            mData.setNickname(e.getEvInfo());
            mBinding.tvName.setText(e.getEvInfo());
            UITipDialog.showSuccess(UserInfoEditActivity.this, "昵称修改成功");
        }
        if (TextUtils.equals(EventTags.USERNAMEEDITREFRESH2, e.getTag()))//刷新用户数据
        {
            mData.setIntroduce(e.getEvInfo());
            mBinding.tvInfo.setText(e.getEvInfo());
        }
    }
}
