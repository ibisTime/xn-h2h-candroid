package com.cdkj.baselibrary.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityBindBankCardBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.BankModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 添加 、删除、修改银行卡
 * Created by 李先俊 on 2017/6/29.
 */

public class AddBackCardActivity extends AbsBaseLoadActivity {

    private ActivityBindBankCardBinding mBinding;

    private String[] mBankNames;
    private String[] mBankCodes;

    private String mSelectCardId;//选择的银行卡ID


    /**
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AddBackCardActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_bind_bank_card, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {


        mBaseBinding.titleView.setMidTitle("绑定银行卡");

        //添加银行类型
        mBinding.txtBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBankBrand();
            }
        });


        if (!TextUtils.isEmpty(SPUtilHelpr.getUserName())) {
            mBinding.editName.setText(SPUtilHelpr.getUserName());
            mBinding.editName.setEnabled(false);
        } else {
            mBinding.editName.setEnabled(true);
        }


        //添加银行卡
        mBinding.txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mBinding.editName.getText().toString())) {
                    UITipDialog.showFall(AddBackCardActivity.this, "请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(mSelectCardId)) {
                    UITipDialog.showFall(AddBackCardActivity.this, "请选择银行");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCardId.getText().toString())) {
                    UITipDialog.showFall(AddBackCardActivity.this, "请输入卡号");
                    return;
                }

                if (mBinding.edtCardId.getText().toString().length() < 16) {
                    UITipDialog.showFall(AddBackCardActivity.this, "银行卡号最低为16位数字");
                    return;
                }


                bindCard();
            }
        });

    }


    //绑定银行卡
    public void bindCard() {

        Map<String, String> object = new HashMap<>();

        object.put("realName", mBinding.editName.getText().toString().trim());
        object.put("bankcardNumber", mBinding.edtCardId.getText().toString().trim());
        object.put("bankName", mBinding.txtBankName.getText().toString().trim());
        object.put("bankCode", mSelectCardId);
        object.put("currency", "CNY");
        object.put("type", "C");
        object.put("token", SPUtilHelpr.getUserToken());
        object.put("userId", SPUtilHelpr.getUserId());
        object.put("systemCode", MyCdConfig.SYSTEMCODE);

        Call call = RetrofitUtils.getBaseAPiService().bindBankCard("802010", StringUtils.getJsonToString(object));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack(this) {
            @Override
            protected void onSuccess(Object data, String SucMessage) {
                showToast("银行卡添加成功");
                SPUtilHelpr.saveUserIsBindCard(true);
                finish();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AddBackCardActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    /**
     * 获取银行卡渠道
     */
    private void getBankBrand() {
        Map object = new HashMap();
        object.put("token", SPUtilHelpr.getUserToken());
        object.put("payType", "WAP");
        Call call = RetrofitUtils.getBaseAPiService().getBackModel("802116", StringUtils.getJsonToString(object));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<BankModel>(this) {
            @Override
            protected void onSuccess(List<BankModel> r, String SucMessage) {
                mBankNames = new String[r.size()];
                mBankCodes = new String[r.size()];

                int i = 0;

                for (BankModel b : r) {
                    mBankNames[i] = b.getBankName();
                    mBankCodes[i] = b.getBankCode();
                    LogUtil.E("银行卡code" + b.getBankCode());
                    i++;
                }
                if (mBankNames.length != 0 && mBankNames.length == mBankCodes.length) {
                    chooseBankCard();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AddBackCardActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    private void chooseBankCard() {
        new AlertDialog.Builder(this).setTitle("请选择银行").setSingleChoiceItems(
                mBankNames, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        mBinding.txtBankName.setText(mBankNames[which]);
                        mSelectCardId = mBankCodes[which];
                        LogUtil.E("选择银行卡code" + mSelectCardId);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

}
