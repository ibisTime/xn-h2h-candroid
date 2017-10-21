package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionDkeyModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.PopSendOrderBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 确认送货Activity
 * Created by cdkj on 2017/10/21.
 */

public class SureSendOrderActivity extends AbsBaseLoadActivity {

    private PopSendOrderBinding mBinding;

    private OptionsPickerView mCompanyPicker;//优惠券选择

    private List<IntroductionDkeyModel> mCompanyModels;
    private IntroductionDkeyModel mSelectCompany;

    private String mOrderCode;

    /**
     * @param context
     * @param orderCode 订单编号
     */
    public static void open(Context context, String orderCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SureSendOrderActivity.class);
        intent.putExtra("code", orderCode);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.pop_send_order, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mCompanyModels = new ArrayList<>();
        setTitle("");
        if (getIntent() != null) {
            mOrderCode = getIntent().getStringExtra("code");
        }

        initSelectPickerView();
        initListener();
    }

    private void initSelectPickerView() {

        mCompanyPicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mSelectCompany = mCompanyModels.get(options1);
                if (mSelectCompany != null) {
                    mBinding.tvCompanyName.setText(mSelectCompany.getPickerViewText());
                }
            }
        }).setContentTextSize(16).setLineSpacingMultiplier(4).build();
    }

    private void initListener() {
/*code:801907
json:{"systemCode":"CD-WTW000016","companyCode":"CD-WTW000016","token":"TSYS_USER_WTWTK201710130935341077766","parentKey":"back_kd_company"}*/
        mBinding.linCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompanyModels.isEmpty()) {
                    getCompnayRequest();
                    return;
                }
                mCompanyPicker.setPicker(mCompanyModels);
                mCompanyPicker.show();

            }
        });

        mBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBinding.btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectCompany == null || TextUtils.isEmpty(mBinding.tvCompanyName.getText())) {
                    UITipDialog.showFall(SureSendOrderActivity.this, "请选择物流公司");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editCode.getText())) {
                    UITipDialog.showFall(SureSendOrderActivity.this, "请输入物流单号");
                    return;
                }

                sendOrderRequest();
            }
        });

    }

    /**
     * 确认发货
     */
    private void sendOrderRequest() {

        if (TextUtils.isEmpty(mOrderCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("code", mOrderCode);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("deliverer", SPUtilHelpr.getUserId());
        map.put("deliveryDatetime", DateUtil.getCurrentDate());
        map.put("logisticsCode", mBinding.editCode.getText().toString());
        map.put("logisticsCompany", mSelectCompany.getCompanyCode());
        Call call = RetrofitUtils.getBaseAPiService().successRequest("808054", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                EventBus.getDefault().post(EventTags.SENDORDERSUSS);//发货成功
                UITipDialog.showSuccess(SureSendOrderActivity.this, "发货成功", new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(SureSendOrderActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取物流公司
     */
    private void getCompnayRequest() {


        Map<String, String> map = new HashMap<>();

        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("parentKey", "back_kd_company");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getdKeyListInfo("801907", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<IntroductionDkeyModel>(this) {
            @Override
            protected void onSuccess(List<IntroductionDkeyModel> data, String SucMessage) {
                mCompanyModels.clear();
                mCompanyModels.addAll(data);
                mCompanyPicker.setPicker(mCompanyModels);
                mCompanyPicker.show();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(SureSendOrderActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


}
