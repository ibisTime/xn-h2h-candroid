package com.cdkj.h2hwtw.module.firstpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.model.MsgListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 消息中心
 * Created by 李先俊 on 2017/8/9.
 */

public class MsgListActivity extends BaseRefreshHelperActivity<MsgListModel.ListBean> {


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MsgListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseQuickAdapter getAdapter(List<MsgListModel.ListBean> listData) {
        return new BaseQuickAdapter<MsgListModel.ListBean, BaseViewHolder>(R.layout.item_msg, listData) {
            @Override
            protected void convert(BaseViewHolder helper, MsgListModel.ListBean item) {
                if (item == null) return;

                helper.setText(R.id.tv_content, item.getSmsContent());
                helper.setText(R.id.tv_title, item.getSmsTitle());
                helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(), DateUtil.DATE_YMD));

            }
        };
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, boolean canShowDialog) {


        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("pushType", "41");
        map.put("toKind", "C");
        map.put("status", "1");
        map.put("fromSystemCode", MyCdConfig.SYSTEMCODE);
        map.put("toSystemCode", MyCdConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMsgList("804040", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(this) {
            @Override
            protected void onSuccess(MsgListModel data, String SucMessage) {
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
                disMissLoading();
            }
        });
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("消息中心");
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected String getErrorInfo() {
        return "暂无消息";
    }
}
