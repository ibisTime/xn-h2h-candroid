package com.cdkj.h2hwtw.module.order;

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
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityProductBuyBinding;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.module.pay.OrderPayActivity;
import com.cdkj.h2hwtw.module.user.info.AddAddressActivity;
import com.cdkj.h2hwtw.module.user.info.AddressListActivity;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 产品购买页面
 * Created by cdkj on 2017/10/18.
 */

public class ProductBuyActivity extends AbsBaseLoadActivity {

    private ActivityProductBuyBinding mBinding;

    private ProductListModel.ListBean mProductData;

    private AddressModel mAddressModel;//地址数据

    public static void open(Context context, ProductListModel.ListBean mProductData) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductBuyActivity.class);
        intent.putExtra("productData", mProductData);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_product_buy, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("订单提交");

        if (getIntent() != null) {
            mProductData = getIntent().getParcelableExtra("productData");
        }

        setShowdata(mProductData);
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressRequest(true);
    }

    private void initListener() {
        //修改地址
        mBinding.layoutAddress.linChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mAddressModel == null) {
//                    return;
//                }
//                AddAddressActivity.open(ProductBuyActivity.this, mAddressModel);

                AddressListActivity.open(ProductBuyActivity.this, true);
            }
        });
        //添加地址
        mBinding.layoutAddress.linAddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AddAddressActivity.open(ProductBuyActivity.this, null);
                AddressListActivity.open(ProductBuyActivity.this, true);
            }
        });

        mBinding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyRequest();
            }
        });
    }


    /**
     * 获取地址请求
     *
     * @param canShowDialog
     */
    public void getAddressRequest(final boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("isDefault", "1");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAddress("805165", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AddressModel>(this) {


            @Override
            protected void onSuccess(List<AddressModel> data, String SucMessage) {
                if (data == null || data.isEmpty()) {
                    setShowAddressData(null);
                    return;
                }
                setShowAddressData(data.get(0));
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                if (canShowDialog) disMissLoading();
            }
        });
    }

    /**
     * 购买请求
     */
    private void buyRequest() {

        if (mAddressModel == null) {
            UITipDialog.showInfo(ProductBuyActivity.this, "请选择收货地址");
            return;
        }
        if (mProductData == null) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("applyUser", SPUtilHelpr.getUserId());
        map.put("applyNote", mBinding.edtEnjoin.getText().toString());
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("productCode", mProductData.getCode());
        map.put("quantity", "1");
        map.put("reMobile", mAddressModel.getMobile());
        map.put("receiver", mAddressModel.getAddressee());
        map.put("reAddress", mAddressModel.getProvince() + " " + mAddressModel.getCity() + " " + mAddressModel.getDistrict() + "" + mAddressModel.getDetailAddress());

        Call call = RetrofitUtils.getBaseAPiService().codeRequest("808060", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getCode())) {
                    OrderPayActivity.open(ProductBuyActivity.this, mBinding.tvPrictAll.getText().toString(), data.getCode(), true);
                    return;
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ProductBuyActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 设置地址显示数据
     *
     * @param addressModel
     */
    private void setShowAddressData(AddressModel addressModel) {
        mAddressModel = addressModel;
        if (addressModel == null) {
            mBinding.layoutAddress.layoutNoAddress.setVisibility(View.VISIBLE);
            mBinding.layoutAddress.linChangeAddress.setVisibility(View.GONE);
            return;
        }
        mBinding.layoutAddress.layoutNoAddress.setVisibility(View.GONE);
        mBinding.layoutAddress.linChangeAddress.setVisibility(View.VISIBLE);

        mBinding.layoutAddress.tvGetName.setText(addressModel.getAddressee());
        mBinding.layoutAddress.tvGetPhone.setText(addressModel.getMobile());
        mBinding.layoutAddress.tvAddress.setText("收货地址：" +
                addressModel.getProvince() + " " + addressModel.getCity() + " " + addressModel.getDistrict() + "" + addressModel.getDetailAddress());

    }

    public void setShowdata(ProductListModel.ListBean data) {
        if (data == null) return;

        mBinding.tvProductName.setText(data.getName());
        ImgUtils.loadImg(this, MyCdConfig.QINIUURL + StringUtils.getAsPicListIndexOne(data.getPic()), mBinding.imgGood);


        mBinding.tvProductPriceSingle.setText(MoneyUtils.showPrice(data.getPrice()));
        mBinding.txtPriceOld.setText(MoneyUtils.getShowPriceSign(data.getOriginalPrice()));
        mBinding.tvProductPrice.setText(MoneyUtils.getShowPriceSign(data.getPrice()));
        BigDecimal allMoney = BigDecimalUtils.add(data.getPrice(), data.getYunfei());//价格加运费 * 折扣
        mBinding.tvPrictAll.setText(MoneyUtils.getShowPriceSign(BigDecimalUtils.multiply(allMoney, data.getDiscount())));
        mBinding.tvYunfei.setText(MoneyUtils.getShowPriceSign(data.getYunfei()));
        mBinding.tvCourierYunfei.setText(MoneyUtils.getShowPriceSign(data.getYunfei()));

    }

    /**
     * 用于购买成功时结束当前页面
     *
     * @param tag
     */
    @Subscribe
    public void EventRefresh(String tag) {
        if (TextUtils.equals(EventTags.BUYLINE, tag)) {
            finish();
            return;
        }
    }

}
