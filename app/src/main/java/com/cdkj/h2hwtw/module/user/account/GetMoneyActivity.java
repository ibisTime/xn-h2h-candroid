package com.cdkj.h2hwtw.module.user.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cdkj.baselibrary.activitys.BackCardListActivity;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.BankCardModel;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.MyBankCardListMode;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityGetMoneyBinding;
import com.cdkj.h2hwtw.model.IntroductionInfoList;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 提现
 * Created by  cdkj on 2017/10/14.
 */

public class GetMoneyActivity extends AbsBaseLoadActivity {

    private ActivityGetMoneyBinding mBinding;

    private BankCardModel mSelectBankCard;

    private String amount;

    private double mFeilv = 0;

    private String mAmountaccountNumber;//账户
    private InputDialog inputDialog;

    public static void open(Context context, String amount, String amountnumber) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, GetMoneyActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("amountnumber", amountnumber);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_get_money, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("提现");
        if (getIntent() != null) {
            amount = getIntent().getStringExtra("amount");
            mAmountaccountNumber = getIntent().getStringExtra("amountnumber");
            mBinding.tvAmount.setText(amount + "元");
        }
        mBinding.linSelectBankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackCardListActivity.open(GetMoneyActivity.this, true);
            }
        });

        mBinding.editGetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    mBinding.tvFei.setText("0元");
                    return;
                }
                mBinding.tvFei.setText(MoneyUtils.doubleFormatMoney2(mFeilv * Double.valueOf(charSequence.toString())) + "元");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.btnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mBinding.editGetAmount.getText().toString())) {
                    UITipDialog.showFall(GetMoneyActivity.this, "请输入提现金额");
                    return;
                }

                if (SPUtilHelpr.isTradepwdFlag()) {

                    inputPayDialog();

                } else {

                    showDoubleWarnListen("您还没有设置支付密码，请先设置支付密码", new CommonDialog.OnPositiveListener() {
                        @Override
                        public void onPositive(View view) {
                            PayPwdModifyActivity.open(GetMoneyActivity.this, false, SPUtilHelpr.getUserPhoneNum());
                        }
                    });

                }


            }
        });

        getBankCardData(true);
        getTipInfo();
    }

    public void inputPayDialog() {
        if (inputDialog == null) {
            inputDialog = new InputDialog(this).builder().setTitle("支付密码")
                    .setPositiveBtn("确定", new InputDialog.OnPositiveListener() {
                        @Override
                        public void onPositive(View view, String inputMsg) {
                            if (TextUtils.isEmpty(inputMsg)) {
                                UITipDialog.showFall(GetMoneyActivity.this, "请输入支付密码");
                                return;
                            }
                            inputDialog.getContentView().setText("");
                            inputDialog.dismiss();
                            getMoneyRequest(inputMsg);
                        }
                    })
                    .setNegativeBtn("取消", null)
                    .setContentMsg("");
            inputDialog.getContentView().setHint("请输入支付密码");
//            inputDialog.getContentView().setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//            inputDialog.getContentView().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        }
        inputDialog.show();
    }

    protected void getBankCardData(boolean canShowDialog) {
        Map<String, String> object = new HashMap<>();

        object.put("systemCode", MyCdConfig.SYSTEMCODE);
        object.put("token", SPUtilHelpr.getUserToken());
        object.put("userId", SPUtilHelpr.getUserId());
        object.put("start", "1");
        object.put("limit", "10");

        Call call = RetrofitUtils.getBaseAPiService().getCardListData("802015", StringUtils.getJsonToString(object));  //签约界面也使用了获取银行卡列表接口 SigningSureActivity

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MyBankCardListMode>(this) {
            @Override
            protected void onSuccess(MyBankCardListMode data, String SucMessage) {
                if (data != null && data.getList() != null && data.getList().size() > 0) {
                    setBankInfo(data.getList().get(0));
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(GetMoneyActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void getTipInfo() {
        Map<String, String> object = new HashMap<>();
        object.put("type", "3");
        object.put("start", "1");
        object.put("limit", "100");
        object.put("systemCode", MyCdConfig.SYSTEMCODE);
        object.put("companyCode", MyCdConfig.COMPANYCODE);
        object.put("token", SPUtilHelpr.getUserToken());


        Call call = RetrofitUtils.createApi(MyApiServer.class).getKeySystemListInfo("802025", StringUtils.getJsonToString(object));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoList>(this) {
            @Override
            protected void onSuccess(IntroductionInfoList data, String SucMessage) {
                setTips(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });


    }

    private void setTips(List<IntroductionInfoModel> data) {
        String CUSERQXBS = "";
        String QXDBZDJE = "";
        for (IntroductionInfoModel d : data) {

            if (TextUtils.equals(d.getCkey(), "CUSERMONTIMES")) {//次数

                mBinding.tvTip1.setText("1 每月最大提现次数" + d.getCvalue() + "次。");

            } else if (TextUtils.equals(d.getCkey(), "CUSERQXBS")) {//倍数
                CUSERQXBS = d.getCvalue();
            } else if (TextUtils.equals(d.getCkey(), "QXDBZDJE")) {//单笔
                QXDBZDJE = d.getCvalue();
            } else if (TextUtils.equals(d.getCkey(), "CUSERQXFL")) {//费率
                try {
                    mFeilv = Double.valueOf(d.getCvalue());
                } catch (Exception e) {
                }

            } else if (TextUtils.equals(d.getCkey(), "CUSERDZTS")) {//天数
                mBinding.tvTip3.setText("3 " + d.getCvalue() + "天到账");
            }
        }

        mBinding.tvTip2.setText("2 提现金额必须是" + CUSERQXBS + "的整倍数，单笔最高" + QXDBZDJE + "元。");
    }

    @Subscribe
    public void setBankInfo(BankCardModel data) {
        if (data == null) {
            mSelectBankCard = null;
            mBinding.tvBankSelect.setText("");
            return;
        }
        mSelectBankCard = data;
        mBinding.tvBankSelect.setText(mSelectBankCard.getBankName());
    }


    public void getMoneyRequest(String pwd) {

        if (TextUtils.isEmpty(mAmountaccountNumber) || mSelectBankCard == null) {
            return;
        }
        Map<String, String> object = new HashMap<>();
        object.put("accountNumber", mAmountaccountNumber);
        object.put("amount", MoneyUtils.getRequestPrice(mBinding.editGetAmount.getText().toString()));
        object.put("payCardInfo", mSelectBankCard.getBankCode());
        object.put("payCardNo", mSelectBankCard.getBankcardNumber());
        object.put("applyUser", SPUtilHelpr.getUserId());
        object.put("tradePwd", pwd);
        object.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().codeRequest("802750", StringUtils.getJsonToString(object));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getCode())){
                    GetMoneySuccessfulActivity.open(GetMoneyActivity.this);
                } else{
                    UITipDialog.showFall(GetMoneyActivity.this, "提现失败");
                }

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(GetMoneyActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inputDialog != null) {
            inputDialog.dismiss();
            inputDialog = null;
        }
    }
}
