package com.cdkj.h2hwtw.module.user.fans;

import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.FansListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.FansModel;
import com.cdkj.h2hwtw.module.user.PersonalPageActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 粉丝列表 关注列表
 * Created by cdkj on 2017/10/27.
 */

public class FansListFragment extends BaseRefreshHelperFragment {


    private boolean isFans;//是否粉丝

    public static FansListFragment getInstanse(boolean isFans) {
        FansListFragment fragment = new FansListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFans", isFans);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        final FansListAdapter fansListAdapter = new FansListAdapter(listData);
        fansListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FansModel fansModel = fansListAdapter.getItem(position);
                if (fansModel == null) return;
                PersonalPageActivity.open(mActivity, fansModel.getUserId());
            }
        });
        return fansListAdapter;
    }

    @Override
    protected void onInit() {
        initRefreshHelper(1, 10);
        if (getArguments() != null) {
            isFans = getArguments().getBoolean("isFans");
        }
        mRefreshHelper.onDefaluteMRefresh(false);
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {

        Map map = RetrofitUtils.getRequestMap();

        map.put("start", pageindex + "");
        map.put("limit", "10");
        if (isFans) {
            map.put("toUser", SPUtilHelpr.getUserId());
        } else {
            map.put("userId", SPUtilHelpr.getUserId());
        }


        Call<BaseResponseModel<ResponseInListModel<FansModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getFansList("805115", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<FansModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<FansModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.loadError(errorMessage);
            }

            @Override
            protected void onFinish() {

            }
        });

    }

    @Override
    protected int getErrorImg() {
        return R.drawable.no_msg;
    }

    @Override
    protected String getErrorInfo() {
        if (getArguments() != null && getArguments().getBoolean("isFans")) {
            return "还没有粉丝哦";
        }
        return "还没有任何关注哦";
    }

}
