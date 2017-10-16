package com.cdkj.h2hwtw.module.product.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.base.BaseLocationActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.QiniuGetTokenModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.QiNiuUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ReleasePagePhotoAdapter;
import com.cdkj.h2hwtw.databinding.ActivityReleaseBinding;
import com.cdkj.h2hwtw.model.PriceKeyBoardListenerModel;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.model.ReleasePagePhotoModel;
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
 * 产品发布
 * Created by cdkj on 2017/10/10.
 */
public class ProductReleaseActivity extends BaseLocationActivity {

    private ActivityReleaseBinding mBinding;
    private final int requestPhotoCode = 20;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<String> mPahtRquestList = new ArrayList<>();

    private ReleasePagePhotoAdapter mAdapter;

    private PriceKeyBoardListenerModel mPriceModel;//包含价格 用于发起请求
    private AMapLocation mLocation;//定位信息

    private String mType;//类型


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ProductReleaseActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_release, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getString(R.string.text_release));
        mBaseBinding.titleView.setRightTitle(getString(R.string.text_cancel));
        initListener();
        mBinding.layoutCheckCamera.recyclerPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new ReleasePagePhotoAdapter(null);
        mAdapter.setHeaderAndEmpty(true);
        mBinding.layoutCheckCamera.recyclerPhoto.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.fra_add_photo:
                        startSelsetPhoto();
                        break;
                    case R.id.img_delete:
                        pathList.remove(mAdapter.getItem(position).getIndex());
                        mAdapter.remove(position);
                        break;
                }

            }
        });

        startLocation();//开始定位

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
                mPriceModel = null;
                new PriceKeyboardPop(ProductReleaseActivity.this, new PriceKeyboardPop.PriceKeyBoardPopListener() {
                    @Override
                    public void sureInputDone(PriceKeyBoardListenerModel model) {
                        mPriceModel = model;
                        mBinding.tvType.setText(model.getPrice());
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
                ReleaseTypeSelectActivity.open(ProductReleaseActivity.this);
            }
        });


        /**
         * 发布
         */
        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseRequest();
            }
        });

    }

    /**
     * 图片选择
     */
    private void startSelsetPhoto() {

        Album.album(ProductReleaseActivity.this)
                .requestCode(requestPhotoCode) // 请求码，返回时onActivityResult()的第一个参数。
                .toolBarColor(ContextCompat.getColor(ProductReleaseActivity.this, R.color.title_bg_app)) // Toolbar 颜色，默认蓝色。
                .statusBarColor(ContextCompat.getColor(ProductReleaseActivity.this, R.color.title_bg_app)) // StatusBar 颜色，默认蓝色。
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
                pathList = Album.parseResult(data);
                if (pathList == null) return;
                setPhotoAdapterAndChangeLayout();
                upLoadImg();

            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。

            }
        }
    }

    /**
     * 更新图片适配器并改变布局
     */
    private void setPhotoAdapterAndChangeLayout() {
        if (pathList.isEmpty()) {
            mBinding.layoutCheckCamera.linCameraCheck.setVisibility(View.VISIBLE);
            mBinding.layoutCheckCamera.recyclerPhoto.setVisibility(View.GONE);
        } else {
            mBinding.layoutCheckCamera.linCameraCheck.setVisibility(View.GONE);
            mBinding.layoutCheckCamera.recyclerPhoto.setVisibility(View.VISIBLE);

            List<ReleasePagePhotoModel> mAdapterList = new ArrayList<>();
            final ReleasePagePhotoModel re = new ReleasePagePhotoModel();
            re.setHeart(true);
            re.setIndex(-1);
            mAdapterList.add(re);
            for (int i = 0; i < pathList.size(); i++) {
                ReleasePagePhotoModel re2 = new ReleasePagePhotoModel();
                re2.setImgPath(pathList.get(i));
                re.setIndex(i);
                mAdapterList.add(re2);
            }
            mAdapter.replaceData(mAdapterList);

        }
    }

    /**
     * 上传图片
     */
    private void upLoadImg() {
        final QiNiuUtil qiNiuUtil = new QiNiuUtil(this);

        qiNiuUtil.getQiniuToeknRequest().enqueue(new BaseResponseModelCallBack<QiniuGetTokenModel>(this) {
            @Override
            protected void onSuccess(QiniuGetTokenModel data, String SucMessage) {
                qiNiuUtil.updataeImage(pathList, data.getUploadToken(), new QiNiuUtil.QiNiuCallBack() {
                    @Override
                    public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                        LogUtil.E("url" + key);
                        if (mPahtRquestList != null) {
                            mPahtRquestList.add(key);
                        }
                    }

                    @Override
                    public void onFal(String info) {

                    }
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    @Override
    protected void locationSuccessful(AMapLocation aMapLocation) {
        mLocation = aMapLocation;
    }

    @Override
    protected void locationFailure() {

    }

    @Override
    protected void onNegativeButton() {
        mBinding.location.tvLocation.setText(R.string.error_location);
    }


    /**
     * 发布请求
     */
    public void releaseRequest() {

        if (mPahtRquestList == null || mPahtRquestList.isEmpty()) {
            UITipDialog.showFall(ProductReleaseActivity.this, getString(R.string.please_add_photo));
            return;
        }

        if (TextUtils.isEmpty(mBinding.editName.getText().toString())) {
            UITipDialog.showFall(ProductReleaseActivity.this, getString(R.string.please_input_name));
            return;
        }
        if (TextUtils.isEmpty(mBinding.editDescription.getText().toString())) {
            UITipDialog.showFall(ProductReleaseActivity.this, getString(R.string.please_input_info));
            return;
        }
        if (TextUtils.isEmpty(mType)) {
            UITipDialog.showFall(ProductReleaseActivity.this, getString(R.string.please_select_type));
            return;
        }

        if (mPriceModel == null || TextUtils.isEmpty(mPriceModel.getPrice())) {
            UITipDialog.showFall(ProductReleaseActivity.this, getString(R.string.please_set_price));
            return;
        }


        if (mPriceModel == null) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        if (mLocation == null) {
            map.put("area", "地区");
            map.put("province", "省份");
            map.put("city", "城市");
            map.put("latitude", "经度");
            map.put("longitude", "维度");

        } else {
            map.put("area", mLocation.getDistrict());
            map.put("province", mLocation.getProvince());
            map.put("city", mLocation.getCity());
            map.put("latitude", mLocation.getLatitude() + "");
            map.put("longitude", mLocation.getLongitude() + "");
        }

        map.put("pic", StringUtils.listToString(mPahtRquestList, "||"));
        map.put("price", mPriceModel.getPrice());
        map.put("originalPrice", mPriceModel.getOldPrice());
        map.put("yunfei", mPriceModel.getSendPrice());

        map.put("type", mType);

        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("description", mBinding.editDescription.getText().toString());
        map.put("isNew", mBinding.checkboxIsnew.isChecked() ? "1" : "0");
        map.put("isPublish", mBinding.checkboxIsPublish.isChecked() ? "1" : "0");
        map.put("kind", "1");
        map.put("name", mBinding.editName.getText().toString());
        map.put("updater", SPUtilHelpr.getUserId());
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("storeCode", MyCdConfig.SYSTEMCODE);

        Call call = RetrofitUtils.getBaseAPiService().stringRequest("808007", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<String>(this) {
            @Override
            protected void onSuccess(String data, String SucMessage) {
                EventBus.getDefault().post(EventTags.RELEASESUSS);//发布成功
                finish();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(ProductReleaseActivity.this, errorMessage);
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
