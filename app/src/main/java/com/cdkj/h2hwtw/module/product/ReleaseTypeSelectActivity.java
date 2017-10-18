package com.cdkj.h2hwtw.module.product;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductTypeSelectAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityReleaseTypeSelectBinding;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 发布类型选择
 * Created by cdkj on 2017/10/16.
 */

public class ReleaseTypeSelectActivity extends AbsBaseLoadActivity {

    private ProductTypeSelectAdapter mTypeMenuLeftAdapter;
    private ProductTypeSelectAdapter mTypeMenuRightAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ReleaseTypeSelectActivity.class);
        context.startActivity(intent);
    }


    private ActivityReleaseTypeSelectBinding mBinding;

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_release_type_select, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("宝贝分类");
        initLeftAdapter();
        initRightAdapter();
        getMenuTypeRequest("0", true);
    }



    private void initLeftAdapter() {
        mTypeMenuLeftAdapter = new ProductTypeSelectAdapter(new ArrayList<ProductTypeModel>(), true);
        mBinding.recyclerTypeLeft.setAdapter(mTypeMenuLeftAdapter);
        mBinding.recyclerTypeLeft.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mTypeMenuLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTypeMenuLeftAdapter.setFirstTouch(false);
                mTypeMenuLeftAdapter.setSelect(position);
                if (mTypeMenuLeftAdapter.getItem(position) == null) return;
                getMenuTypeRequest(mTypeMenuLeftAdapter.getItem(position).getCode(), false);
            }
        });
    }

    private void initRightAdapter() {
        mTypeMenuRightAdapter = new ProductTypeSelectAdapter(new ArrayList<ProductTypeModel>(), false);
        mBinding.recyclerTypeRight.setAdapter(mTypeMenuRightAdapter);
        mBinding.recyclerTypeRight.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mTypeMenuRightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductTypeModel typeModel = mTypeMenuRightAdapter.getItem(position);
                EventBus.getDefault().post(typeModel);
                finish();
            }
        });
    }

    /**
     * 获取分类请求
     */
    private void getMenuTypeRequest(String parentCode, final boolean isLeft) {

        Map<String, String> map = new HashMap();

        map.put("status", "1");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("parentCode", parentCode);
        map.put("type", "4");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductType("808007", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<ProductTypeModel>(ReleaseTypeSelectActivity.this) {
            @Override
            protected void onSuccess(List<ProductTypeModel> data, String SucMessage) {
                if (isLeft) {
                    mTypeMenuLeftAdapter.replaceData(data);
                } else {
                    mTypeMenuRightAdapter.replaceData(data);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ReleaseTypeSelectActivity.this, errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                UITipDialog.showFall(ReleaseTypeSelectActivity.this, msg);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}
