package com.cdkj.h2hwtw.module.user.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.MyFriendListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 邀请好友列表
 * Created by cdkj on 2017/10/24.
 */

public class InvitationFriendListActivity extends BaseRefreshHelperActivity {


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InvitationFriendListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        return new BaseQuickAdapter<MyFriendListModel.ListBean, BaseViewHolder>(R.layout.item_friend_list, listData) {
            @Override
            protected void convert(BaseViewHolder helper, MyFriendListModel.ListBean item) {
                if (item == null) {
                    return;
                }

                helper.setText(R.id.tv_user_name, item.getMobile());
                helper.setText(R.id.tv_info, DateUtil.formatStringData(item.getCreateDatetime(), DateUtil.DATE_YMD));

                ImageView img = helper.getView(R.id.img_user_logo);

                if (item.getUserExt() != null) {
                    ImgUtils.loadLogo(InvitationFriendListActivity.this, item.getUserExt().getPhoto(), img);
                }

            }
        };
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {

        Map map = RetrofitUtils.getRequestMap();

        map.put("userReferee", SPUtilHelpr.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<MyFriendListModel>> call = RetrofitUtils.createApi(MyApiServer.class).getFriendList("805120", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MyFriendListModel>(this) {
            @Override
            protected void onSuccess(MyFriendListModel data, String SucMessage) {
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

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("好友列表");

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "还没有邀请到好友哦";
    }
}
