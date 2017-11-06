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
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivitySureBackPayBinding;
import com.cdkj.h2hwtw.model.PickerPassModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 确认退款Activity
 * Created by cdkj on 2017/10/21.
 */

public class SureBackPayActivity extends AbsBaseLoadActivity {

    private ActivitySureBackPayBinding mBinding;

    private OptionsPickerView mPassPicker;//选择弹框

    private PickerPassModel mPassModel;//是否通过选择

    private List<PickerPassModel> passModels;

    private String mOrderCode;

    /**
     * @param context
     * @param orderCode 订单编号
     */
    public static void open(Context context, String orderCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SureBackPayActivity.class);
        intent.putExtra("code", orderCode);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sure_back_pay, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTitle("");
        if (getIntent() != null) {
            mOrderCode = getIntent().getStringExtra("code");
        }

        initPassModels();
        initSelectPickerView();
        initListener();
    }

    private void initSelectPickerView() {

        mPassPicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mPassModel = passModels.get(options1);
            }
        }).setContentTextSize(16).setLineSpacingMultiplier(4).build();
    }

    private void initPassModels() {
        passModels = new ArrayList<>();

        PickerPassModel pickerPassModel = new PickerPassModel();
        pickerPassModel.setSure(true);
        passModels.add(pickerPassModel);
        PickerPassModel pickerPassModel1 = new PickerPassModel();
        pickerPassModel.setSure(false);
        passModels.add(pickerPassModel1);
    }

    private void initListener() {
/*code:801907
json:{"systemCode":"CD-WTW000016","companyCode":"CD-WTW000016","token":"TSYS_USER_WTWTK201710130935341077766","parentKey":"back_kd_company"}*/
        mBinding.linCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPassPicker.setPicker(passModels);
                mPassPicker.show();

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
                if (mPassModel == null && TextUtils.isEmpty(mBinding.tvCompanyName.getText())) {
                    UITipDialog.showFall(SureBackPayActivity.this, "请确认是否通过退款");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editCode.getText())) {
                    UITipDialog.showFall(SureBackPayActivity.this, "请输入退款备注");
                    return;
                }

                if (mPassModel == null || TextUtils.isEmpty(mOrderCode)) return;
                backPayRequest(mOrderCode, mPassModel.isSure());
            }
        });

    }

    /**
     * 退款请求
     *
     * @param code
     * @param isSure 是否同意退款
     */
    private void backPayRequest(String code, boolean isSure) {

        /*code（必填） 编号 string
 remark（选填） 备注 string
 result（必填） 审核结果 string 0 不同意，1同意
 updater（必填） 更新人 string
*/
        Map map = new HashMap();

        map.put("code", code);
        map.put("remark", "");
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("result", isSure ? "1" : "0");

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808062", StringUtils.getJsonToString(map));

        showLoadingDialog();

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack(this) {
            @Override
            protected void onSuccess(Object data, String SucMessage) {
                UITipDialog.showSuccess(SureBackPayActivity.this, "处理退款信息成功", new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        EventBus.getDefault().post(EventTags.BACKPAYSUCC);
                        finish();
                    }
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(SureBackPayActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

}
