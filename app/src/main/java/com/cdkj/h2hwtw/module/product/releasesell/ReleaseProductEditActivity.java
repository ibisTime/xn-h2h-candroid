package com.cdkj.h2hwtw.module.product.releasesell;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.QiniuGetTokenModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.QiNiuUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ReleasePagePhotoAdapter;
import com.cdkj.h2hwtw.databinding.ActivityReleaseBinding;
import com.cdkj.h2hwtw.model.PriceKeyBoardListenerModel;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.model.ReleasePagePhotoModel;
import com.cdkj.h2hwtw.module.product.ProductReleaseActivity;
import com.cdkj.h2hwtw.module.product.ReleaseTypeSelectActivity;
import com.cdkj.h2hwtw.pop.PriceKeyboardPop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiniu.android.http.ResponseInfo;
import com.yanzhenjie.album.Album;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 产品编辑
 * Created by cdkj on 2017/10/10.
 */

//TODO 发布接口参数
public class ReleaseProductEditActivity extends AbsBaseLoadActivity {

    private ActivityReleaseBinding mBinding;
    private final int requestPhotoCode = 20;
    private ArrayList<String> pathList = new ArrayList<>();
    private List<String> mPahtRquestList = new ArrayList<>();
    private ReleasePagePhotoAdapter mPhotoAdapter;
    private PriceKeyBoardListenerModel mPriceModel;//包含价格 用于发起请求

    private String mType;//类型

    private ProductListModel.ListBean mProductData;

    private boolean isSelectPhoto;//用于判断用户是否选择过图片；

    public static void open(Context context, ProductListModel.ListBean productData) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ReleaseProductEditActivity.class);
        intent.putExtra("productData", productData);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_release, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("编辑");

        initListener();

        initCameraLayout();

        //改变默认的单行模式
        mBinding.editDescription.setSingleLine(false);
        //水平滚动设置为False
        mBinding.editDescription.setHorizontallyScrolling(false);

        if (getIntent() != null) {
            mProductData = getIntent().getParcelableExtra("productData");
        }

        setPhotoShowData(mProductData);

        setShowData(mProductData);

    }

    /**
     * 设置图片数据
     *
     * @param mProductData
     */
    private void setPhotoShowData(ProductListModel.ListBean mProductData) {
        if (mProductData == null) return;

        isSelectPhoto = false;

        mPahtRquestList.addAll(StringUtils.splitAsPicList(mProductData.getPic()));

        setPhotoAdapterAndChangeLayout(mPahtRquestList, true);

    }

    /**
     * 改变适配器数据
     *
     * @param pahtList
     * @param isRequeURL 是否来自网络地址
     */
    private void changePhotoAdapterData(List<String> pahtList, boolean isRequeURL) {
        if (pahtList == null || pahtList.isEmpty()) return;
        List<ReleasePagePhotoModel> mAdapterList = new ArrayList<>();
        final ReleasePagePhotoModel re = new ReleasePagePhotoModel();
        re.setHeart(true);
        re.setIndex(-1);
        mAdapterList.add(re);
        for (int i = 0; i < pahtList.size(); i++) {
            ReleasePagePhotoModel re2 = new ReleasePagePhotoModel();
            if (isRequeURL) {
                re2.setImgPath(MyCdConfig.QINIUURL + pahtList.get(i));
            } else {
                re2.setImgPath(pahtList.get(i));
            }
            re.setIndex(i);
            mAdapterList.add(re2);
        }
        mPhotoAdapter.replaceData(mAdapterList);
    }

    /**
     * 设置显示数据
     *
     * @param productData
     */
    private void setShowData(ProductListModel.ListBean productData) {
        if (productData == null) return;


        mBinding.editName.setText(productData.getName());          //产品名称
        if (!TextUtils.isEmpty(mBinding.editName.getText().toString())) {
            mBinding.editName.setSelection(mBinding.editName.getText().toString().length());
        }
        mBinding.editDescription.setText(productData.getDescription());

        mBinding.checkboxIsnew.setChecked(TextUtils.equals(productData.getIsNew(), "1"));//是否全新 1全新

        mBinding.checkboxIsPublish.setChecked(TextUtils.equals(productData.getIsPublish(), "1"));//是否发布到圈子 1发布

        mBinding.location.tvLocation.setText(productData.getProvince() + " " + productData.getCity() + " " + productData.getArea());//设置的定位信息


        mType = productData.getType();     //用户选择的类型
        mBinding.tvType.setText(productData.getTypeName());

        mPriceModel = new PriceKeyBoardListenerModel();         //用户设置的价格
        mPriceModel.setPrice(MoneyUtils.getPriceIntValue(productData.getPrice()) + "");
        mPriceModel.setOldPrice(MoneyUtils.getPriceIntValue(productData.getOriginalPrice()) + "");
        mPriceModel.setSendPrice(MoneyUtils.getPriceIntValue(productData.getYunfei()) + "");
        mPriceModel.setCanSend(BigDecimalUtils.intValue(productData.getYunfei()) == 0);
        mBinding.tvPrice.setText(MoneyUtils.getPriceIntValue(productData.getPrice()) + "");


    }

    /**
     * 设置相机布局
     */
    private void initCameraLayout() {
        mBinding.layoutCheckCamera.recyclerPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mPhotoAdapter = new ReleasePagePhotoAdapter(null);
        mPhotoAdapter.setHeaderAndEmpty(true);
        mBinding.layoutCheckCamera.recyclerPhoto.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.fra_add_photo:
                        startSelsetPhoto();
                        break;
                    case R.id.img_delete:
                        if (isSelectPhoto) { //选择了相册
                            if (pathList != null && mPhotoAdapter.getItem(position) != null && pathList.size() > mPhotoAdapter.getItem(position).getIndex()) {
                                pathList.remove(mPhotoAdapter.getItem(position).getIndex());
                            }

                        } else {         //没有选择相册
                            if (mPahtRquestList != null && mPhotoAdapter.getItem(position) != null && mPahtRquestList.size() > mPhotoAdapter.getItem(position).getIndex()) {
                                mPahtRquestList.remove(mPhotoAdapter.getItem(position).getIndex());
                            }
                        }
                        mPhotoAdapter.remove(position);
                        break;
                }

            }
        });
    }

    @Override
    public void topTitleViewRightClick() {
        finish();
    }

    /**
     * 设置事件
     */
    private void initListener() {

        //相册选择
        mBinding.layoutCheckCamera.linCameraCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelsetPhoto();
            }
        });

        //价格计算
        mBinding.layoutPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PriceKeyboardPop(ReleaseProductEditActivity.this, mPriceModel, new PriceKeyboardPop.PriceKeyBoardPopListener() {
                    @Override
                    public void sureInputDone(PriceKeyBoardListenerModel model) {
                        mPriceModel = model;
                        mBinding.tvPrice.setText(model.getPrice());
                    }
                }).showPopupWindow();
            }
        });

        //是否全新
        mBinding.linIsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.checkboxIsnew.setChecked(!mBinding.checkboxIsnew.isChecked());
            }
        });

        mBinding.lineType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleaseTypeSelectActivity.open(ReleaseProductEditActivity.this);
            }
        });


        /**
         * 发布
         */
        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isSelectPhoto && (mPahtRquestList == null || mPahtRquestList.isEmpty())) {//如果用户没有选择图片
                    UITipDialog.showFall(ReleaseProductEditActivity.this, getString(R.string.please_add_photo));
                    return;
                }
                if (isSelectPhoto && (pathList == null || pathList.isEmpty())) {          //如果用户选择了图片
                    UITipDialog.showFall(ReleaseProductEditActivity.this, getString(R.string.please_add_photo));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.editName.getText().toString())) {
                    UITipDialog.showFall(ReleaseProductEditActivity.this, getString(R.string.please_input_name));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.editDescription.getText().toString())) {
                    UITipDialog.showFall(ReleaseProductEditActivity.this, getString(R.string.please_input_info));
                    return;
                }
                if (TextUtils.isEmpty(mType)) {
                    UITipDialog.showFall(ReleaseProductEditActivity.this, getString(R.string.please_select_type));
                    return;
                }

                if (mPriceModel == null || TextUtils.isEmpty(mPriceModel.getPrice())) {
                    UITipDialog.showFall(ReleaseProductEditActivity.this, getString(R.string.please_set_price));
                    return;
                }

                if (!SPUtilHelpr.isLogin(ReleaseProductEditActivity.this, false)) {
                    return;
                }
                if (isSelectPhoto) { //如果选择了相册
                    upLoadImg();
                } else {
                    updateRequest();
                }

            }
        });

    }

    /**
     * 图片选择
     */
    private void startSelsetPhoto() {

        Album.album(ReleaseProductEditActivity.this)
                .requestCode(requestPhotoCode) // 请求码，返回时onActivityResult()的第一个参数。
                .toolBarColor(ContextCompat.getColor(ReleaseProductEditActivity.this, R.color.title_bg_app)) // Toolbar 颜色，默认蓝色。
                .statusBarColor(ContextCompat.getColor(ReleaseProductEditActivity.this, R.color.title_bg_app)) // StatusBar 颜色，默认蓝色。
//                    .navigationBarColor(navigationBarColor) // NavigationBar 颜色，默认黑色，建议使用默认。
                .title(getString(R.string.photo_selset)) // 配置title。
                .selectCount(9) // 最多选择几张图片。
                .columnCount(2) // 相册展示列数，默认是2列。
                .camera(true) // 是否有拍照功能。
                .checkedList(pathList) // 已经选择过得图片，相册会自动选中选过的图片，并计数。
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestPhotoCode == requestCode) {
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
                isSelectPhoto = true;
                mPahtRquestList.clear();
                pathList = Album.parseResult(data);
                if (pathList == null) return;
                setPhotoAdapterAndChangeLayout(pathList, false);

            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。

            }
        }
    }

    /**
     * 更新图片适配器并改变布局
     */
    private void setPhotoAdapterAndChangeLayout(List<String> pathList, boolean isRequeURL) {
        if (pathList.isEmpty()) {
            mBinding.layoutCheckCamera.linCameraCheck.setVisibility(View.VISIBLE);
            mBinding.layoutCheckCamera.recyclerPhoto.setVisibility(View.GONE);
        } else {
            mBinding.layoutCheckCamera.linCameraCheck.setVisibility(View.GONE);
            mBinding.layoutCheckCamera.recyclerPhoto.setVisibility(View.VISIBLE);

            changePhotoAdapterData(pathList, isRequeURL);

        }
    }

    /**
     * 上传图片
     */
    private void upLoadImg() {
        final QiNiuUtil qiNiuUtil = new QiNiuUtil(this);

        showLoadingDialog();
        mPahtRquestList.clear();
        qiNiuUtil.getQiniuToeknRequest().enqueue(new BaseResponseModelCallBack<QiniuGetTokenModel>(this) {
            @Override
            protected void onSuccess(QiniuGetTokenModel data, String SucMessage) {

                qiNiuUtil.setUpLoadListIndex(0);
                qiNiuUtil.upLoadListPic(pathList, data.getUploadToken(), new QiNiuUtil.upLoadListListener() {
                    @Override
                    public void onChange(int index, String url) {
                        mPahtRquestList.add(url);
                    }

                    @Override
                    public void onSuccess() {
                        if (!mPahtRquestList.isEmpty()) {
                            updateRequest();
                        }

                    }

                    @Override
                    public void onFal(String info) {
                        if (mPahtRquestList.isEmpty() && pathList.size() == 1) { //如果只上传一张图片 且失败时
                            disMissLoading();
                            UITipDialog.showFall(ReleaseProductEditActivity.this, "图片上传失败");
                        }
                        LogUtil.E(info);
                    }

                    @Override
                    public void onError(String info) {
                        disMissLoading();
                        UITipDialog.showFall(ReleaseProductEditActivity.this, "图片上传失败");
                    }
                });

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                disMissLoading();
                UITipDialog.showFall(ReleaseProductEditActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 编辑更新请求
     */
    public void updateRequest() {

        Map<String, String> map = new HashMap<>();

        map.put("area", mProductData.getArea());
        map.put("province", mProductData.getProvince());
        map.put("city", mProductData.getCity());
        map.put("latitude", mProductData.getLatitude());
        map.put("longitude", mProductData.getLongitude());

        map.put("pic", StringUtils.listToString(mPahtRquestList, "||"));
        map.put("price", MoneyUtils.getRequestPrice(mPriceModel.getPrice()));
        map.put("originalPrice", MoneyUtils.getRequestPrice(mPriceModel.getOldPrice()));
        map.put("yunfei", MoneyUtils.getRequestPrice(mPriceModel.getSendPrice()));

        map.put("type", mType);
        map.put("code", mProductData.getCode());

        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("description", mBinding.editDescription.getText().toString());
        map.put("isNew", mBinding.checkboxIsnew.isChecked() ? "1" : "0");
        map.put("isPublish", mBinding.checkboxIsPublish.isChecked() ? "1" : "0");
        map.put("kind", "1");
        map.put("name", mBinding.editName.getText().toString());
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("storeCode", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("808012", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    EventBus.getDefault().post(EventTags.RELEASEEDITSUSS);//编辑成功
                    finish();
                } else {
                    disMissLoading();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ReleaseProductEditActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 获取选择的类型
     *
     * @param typeModel
     */
    @Subscribe
    public void eventSelect(ProductTypeModel typeModel) {
        if (typeModel == null) return;
        mType = typeModel.getCode();
        mBinding.tvType.setText(typeModel.getName());
    }

}
