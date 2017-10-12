package com.cdkj.h2hwtw.module.product.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ReleasePagePhotoAdapter;
import com.cdkj.h2hwtw.databinding.ActivityReleaseBinding;
import com.cdkj.h2hwtw.model.ReleasePagePhotoModel;
import com.cdkj.h2hwtw.module.common.MyBaseLoadActivity;
import com.cdkj.h2hwtw.pop.PriceKeyboardPop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品发布
 * Created by cdkj on 2017/10/10.
 */
public class ProductReleaseActivity extends MyBaseLoadActivity {

    private ActivityReleaseBinding mBinding;
    private final int requestPhotoCode = 20;
    private ArrayList<String> pathList = new ArrayList<>();

    private ReleasePagePhotoAdapter mAdapter;


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
        setLeftImg();
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

    }

    @Override
    public void topTitleViewRightClick() {
        finish();
    }

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
                new PriceKeyboardPop(ProductReleaseActivity.this).showPopupWindow();
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
                .columnCount(3) // 相册展示列数，默认是2列。
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

                if (pathList.isEmpty()) {
                    mBinding.layoutCheckCamera.linCameraCheck.setVisibility(View.VISIBLE);
                    mBinding.layoutCheckCamera.recyclerPhoto.setVisibility(View.GONE);
                } else {
                    mBinding.layoutCheckCamera.linCameraCheck.setVisibility(View.GONE);
                    mBinding.layoutCheckCamera.recyclerPhoto.setVisibility(View.VISIBLE);

                    List<ReleasePagePhotoModel> mAdapterList = new ArrayList<>();
                    ReleasePagePhotoModel re = new ReleasePagePhotoModel();
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

            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。

            }
        }
    }

    @Override
    public int getLoadTitleBg() {
        return 1;
    }
}
