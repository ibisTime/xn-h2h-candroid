package com.cdkj.h2hwtw.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.GridDivider;
import com.cdkj.baselibrary.views.ScrollGridLayoutManager;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductGridAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivityPersonalPageBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.ReleaseNumModel;
import com.cdkj.h2hwtw.model.UserInfoModel;
import com.cdkj.h2hwtw.module.user.info.UserInfoEditActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 个人主页
 * Created by cdkj on 2017/10/26.
 */

public class PersonalPageActivity extends BaseRefreshHelperActivity {


    private String mUserId;

    private boolean isEdit;

    private UserInfoModel mUserInfoModel;

    private ActivityPersonalPageBinding mBinding;

    /**
     * @param context
     * @param userId  用户ID
     */
    public static void open(Context context, String userId) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PersonalPageActivity.class);
        intent.putExtra("userid", userId);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_personal_page, null, false);
        return mBinding.getRoot();
    }


    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mBinding.rv;
    }


    @Override
    protected void onInit(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mUserId = getIntent().getStringExtra("userid");
        }

        mBaseBinding.titleView.setMidTitle("个人主页");

        initEditState();
        mBinding.refreshLayout.setEnableRefresh(false);//禁用下拉
        mBinding.rv.setLayoutManager(new ScrollGridLayoutManager(this, 2));
        getReleaseSumRequest();
    }


    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        ProductGridAdapter mProductAdapter = new ProductGridAdapter(listData);
        return mProductAdapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfoRequest(mUserId);
    }

    /**
     * 设置编辑按钮状态 不是当前用户时状态为关注
     */
    private void initEditState() {


        if (TextUtils.equals(mUserId, SPUtilHelpr.getUserId())) {
            mBinding.layoutUser.tvEditInfo.setText("编辑");
            isEdit = true;
        } else {
            getfocusRequest(false);
            isEdit = false;
        }

        mBinding.layoutUser.tvEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    if (mUserInfoModel == null) return;
                    UserInfoEditActivity.open(PersonalPageActivity.this, mUserInfoModel);
                } else {
                    getfocusRequest(true);
                }
            }
        });

    }

    /**
     * 查询是否存在关注关系
     *
     * @param click 是否主动点击
     */
    private void getfocusRequest(final boolean click) {

        if (TextUtils.isEmpty(mUserId)) {
            return;
        }
        Map map = RetrofitUtils.getRequestMap();
        map.put("toUser", mUserId);
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().booleanRequest("805116", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<Boolean>(this) {
            @Override
            protected void onSuccess(Boolean data, String SucMessage) {
                if (click) {
                    if (data) {
                        cancelFocusRequest();
                    } else {
                        focusRequest();
                    }
                    return;
                }

                if (data) {
                    mBinding.layoutUser.tvEditInfo.setText("取消关注");
                } else {
                    mBinding.layoutUser.tvEditInfo.setText(R.string.focus_on);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(PersonalPageActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 关注请求
     */
    private void focusRequest() {

        if (TextUtils.isEmpty(mUserId)) {
            return;
        }
        Map map = RetrofitUtils.getRequestMap();
        map.put("toUser", mUserId);
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805110", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(PersonalPageActivity.this, "关注成功");
                    mBinding.layoutUser.tvEditInfo.setText("取消关注");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(PersonalPageActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 解除关注请求
     */
    private void cancelFocusRequest() {

        if (TextUtils.isEmpty(mUserId)) {
            return;
        }
        Map map = RetrofitUtils.getRequestMap();
        map.put("toUser", mUserId);
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805111", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(PersonalPageActivity.this, "取消关注成功");
                    mBinding.layoutUser.tvEditInfo.setText(R.string.focus_on);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(PersonalPageActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取发布数量
     */
    private void getReleaseSumRequest() {

        if (TextUtils.isEmpty(mUserId)) {
            return;
        }
        Map map = RetrofitUtils.getRequestMap();
        map.put("userId", mUserId);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getReleaseSum("808018", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<ReleaseNumModel>(this) {
            @Override
            protected void onSuccess(ReleaseNumModel data, String SucMessage) {

                StringBuffer sb = new StringBuffer();
                sb.append("累计发布宝贝");
                sb.append(data.getTotalProduct());
                sb.append("个,");
                sb.append("在架");
                sb.append(data.getTotalOnProduct());
                sb.append("个");

                mBinding.layoutUser.tvReleaseSum.setText(sb.toString());
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
    protected String getErrorInfo() {
        return "还没有发布产品";
    }


    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
/*code:808021
json:{"systemCode":"CD-WTW000016","companyCode":"CD-WTW000016","token":"TSYS_USER_WTWTK201710252155069692838","start":1,"limit":10,"statusList":[3,4,5,6],"userId":"U11111111111111112","orderColumn":"status","orderDir":"asc"}
*/

        if (TextUtils.isEmpty(mUserId)) {
            return;
        }
        Map map = RetrofitUtils.getRequestMap();
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("orderColumn", "status");
        map.put("orderDir", "asc");
        map.put("userId", mUserId);

        List<String> list = new ArrayList<>();

        list.add("3");
        list.add("4");
        list.add("6");
        list.add("6");
        map.put("statusList", list);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808021", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ProductListModel>(this) {
            @Override
            protected void onSuccess(ProductListModel data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.loadError(errorMessage);
            }

            @Override
            protected void onNoNet(String msg) {
                mRefreshHelper.loadError(msg);
            }

            @Override
            protected void onFinish() {
                if (isShowDialog) disMissLoading();

            }
        });

    }


    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(String userId) {


        if (TextUtils.isEmpty(userId)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", userId);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                mUserInfoModel = data;
                mRefreshHelper.onDefaluteMRefresh(false);
                ImgUtils.loadLogo(PersonalPageActivity.this, MyCdConfig.QINIUURL + data.getPhoto(), mBinding.layoutUser.imgUserLogo);
                mBinding.layoutUser.tvUserName.setText(data.getNickname());

                StringBuffer sb = new StringBuffer();
                sb.append(DateUtil.getDatesBetweenData(new Date(data.getCreateDatetime())).size() + "");
                sb.append("天前加入");
                sb.append(getString(R.string.app_name));
                mBinding.layoutUser.tvCrateTime.setText(sb.toString());

                mBinding.layoutUser.tvInfoGo.setText(DateUtil.getLoginDataInfo2(data.getLoginDatetime()));

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


}
