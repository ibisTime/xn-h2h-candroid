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
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.ActivityCommentsEditBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 订单评价
 * Created by cdkj on 2017/10/17.
 */

public class OrderCommentsEditActivity extends AbsBaseLoadActivity {

    private String mProductCode;//产品编号
    private String mOrderCode;//订单编号

    private ActivityCommentsEditBinding mBinding;

    public static void open(Context context, String ProductCode, String OrderCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OrderCommentsEditActivity.class);
        intent.putExtra("ProductCode", ProductCode);
        intent.putExtra("OrderCode", OrderCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_comments_edit, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {

        if (TextUtils.isEmpty(mBinding.editInfo.getText().toString())) {
            UITipDialog.showFall(this, "请输入留言内容");
            return;
        }

        sendDataRequest();
    }

    /*commentList  object
      content(必填) 内容 string
      productCode(必填) 产品编号 string
      score（必填） 得分 string 默认传 0
 commenter（必填） 评论人 string
 orderCode（必填）
*/
    private void sendDataRequest() {

        if (TextUtils.isEmpty(mProductCode) || TextUtils.isEmpty(mOrderCode)) {
            return;
        }
        Map map = new HashMap();
        Map<String, String> commentMap = new HashMap<String, String>();
        commentMap.put("content", mBinding.editInfo.getText().toString());
        commentMap.put("productCode", mProductCode);
        commentMap.put("score", "0");

        List<Map> commentList = new ArrayList<>();

        commentList.add(commentMap);

        map.put("commentList", commentList);
        map.put("commenter", SPUtilHelpr.getUserId());
        map.put("orderCode", mOrderCode);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);

        Call call = RetrofitUtils.getBaseAPiService().stringRequest("808059", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<String>(this) {
            @Override
            protected void onSuccess(String data, String SucMessage) {
                if (!TextUtils.isEmpty(data)) {
                    EventBus.getDefault().post(EventTags.RELEASESCOMMENTSORDER);
                    ToastUtil.show(OrderCommentsEditActivity.this, "评价成功");
                    finish();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(OrderCommentsEditActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("评价");
        mBaseBinding.titleView.setRightTitle("发布");
        mBinding.editInfo.setHint("写下你的评价");

        if (getIntent() != null) {
            mProductCode = getIntent().getStringExtra("ProductCode");
            mOrderCode = getIntent().getStringExtra("OrderCode");
        }

    }
}
