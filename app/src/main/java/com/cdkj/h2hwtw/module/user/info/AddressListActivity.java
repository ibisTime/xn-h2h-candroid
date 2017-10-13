package com.cdkj.h2hwtw.module.user.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.MyDividerItemDecoration;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.AddressListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.AddressModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 地址列表
 * Created by cdkj on 2017/10/13.
 */

public class AddressListActivity extends BaseRefreshActivity<AddressModel> {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AddressListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void topTitleViewRightClick() {
        if (mAdapter != null && mAdapter.getData().size() == 0) {     //还没有添加地址时设置为默认地址
            AddAddressActivity.open(this, true, null);
        } else {
            AddAddressActivity.open(this, false, null);
        }
    }


    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        mBaseBinding.titleView.setMidTitle(getString(R.string.address_manager));
        mBaseBinding.titleView.setRightTitle("添加");

        if (mAdapter != null) {
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    switch (view.getId()) {
                        case R.id.layout_delete://删除
                            deleteAddress(position);
                            break;
                        case R.id.layout_edit://编辑
                            AddressModel addressModel = (AddressModel) mAdapter.getItem(position);
                            AddAddressActivity.open(AddressListActivity.this, false, addressModel);
                            break;

                        case R.id.real_address://选择
                            AddressModel addr = (AddressModel) mAdapter.getItem(position);
                            if (addr == null) return;
                            setDefaultAddress(addr.getCode());
                            break;
                    }

                }
            });
        }
        //禁用刷新加载
        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setEnableLoadmore(false);

        mBinding.rv.addItemDecoration(new MyDividerItemDecoration(this, MyDividerItemDecoration.VERTICAL_LIST));
        getListData(pageIndex, limit, true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, final boolean canShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("token", SPUtilHelpr.getUserToken());


        Call call = RetrofitUtils.createApi(MyApiServer.class).getAddress("805165", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AddressModel>(this) {


            @Override
            protected void onSuccess(List<AddressModel> data, String SucMessage) {
                setData(data);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AddressListActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (canShowDialog) disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<AddressModel> mDataList) {
        return new AddressListAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.no_address_tips);
    }

    @Override
    public int getEmptyImg() {
        return 0;
    }

    /**
     * 删除
     */
    private void deleteAddress(final int po) {
        showDoubleWarnListen("确认删除该收货地址?", new CommonDialog.OnPositiveListener() {
            @Override
            public void onPositive(View view) {
                deleteRequest(po);
            }
        });
    }


    /**
     * 删除
     */
    private void setDefaultAddress(final String po) {
        showDoubleWarnListen("确认设置该收货地址为默认地址?", new CommonDialog.OnPositiveListener() {
            @Override
            public void onPositive(View view) {
                setDefaultAddressRequest(po);
            }
        });
    }


    /**
     * 设置默认地址请求
     */
    public void setDefaultAddressRequest(String code) {

        if (TextUtils.isEmpty(code)) {
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("code", code);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        Call call = RetrofitUtils.createApi(MyApiServer.class).setDefultAddress("805163", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<Boolean>(this) {

            @Override
            protected void onSuccess(Boolean data, String SucMessage) {
                if (data) {
                    getListData(1, 10, false);
                } else {
                    UITipDialog.showFall(AddressListActivity.this, "操作失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AddressListActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 删除请求
     */
    private void deleteRequest(final int position) {

        AddressModel addressModel = (AddressModel) mAdapter.getItem(position);

        if (addressModel == null || TextUtils.isEmpty(addressModel.getCode())) {
            return;
        }

        Map<String, String> object = new HashMap<>();

        object.put("code", addressModel.getCode());
        object.put("token", SPUtilHelpr.getUserToken());
        object.put("systemCode", MyCdConfig.SYSTEMCODE);
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805161", StringUtils.getJsonToString(object));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data != null && data.isSuccess()) {
                    if (mAdapter != null) mAdapter.remove(position);
                } else {
                    getListData(1, 10, false);
                    UITipDialog.showFall(AddressListActivity.this, "删除失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(AddressListActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}
