package com.cdkj.h2hwtw.module.product;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityCommentsEditBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 编号查询
 * Created by cdkj on 2017/10/17.
 */

public class ProductCommentsEditActivity extends AbsBaseLoadActivity {

    private String mProductCode;//产品编号

    private ActivityCommentsEditBinding mBinding;

    public static void open(Context context, String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductCommentsEditActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_comments_edit, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {

        if (TextUtils.isEmpty(mBinding.editInfo.getText().toString())) {
            UITipDialog.showFall(this, "请输入留言内容");
            return;
        }

        sendDataRequest();
    }

    private void sendDataRequest() {

        if (TextUtils.isEmpty(mProductCode)) {
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("commenter", SPUtilHelpr.getUserId());
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("content", mBinding.editInfo.getText().toString());
        map.put("entityCode", mProductCode);
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().codeRequest("801020", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getCode())) {
                    EventBus.getDefault().post(EventTags.RELEASESCOMMENTS);
                    finish();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ProductCommentsEditActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("留言");
        mBaseBinding.titleView.setRightTitle("发布");

        if (getIntent() != null) {
            mProductCode = getIntent().getStringExtra("code");
        }

    }
}
