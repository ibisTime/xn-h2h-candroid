package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.BankCardModel;
import com.cdkj.baselibrary.model.MyBankCardListMode;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 银行卡列表
 * Created by 李先俊 on 2017/6/29.
 */

public class BackCardListActivity extends BaseRefreshHelperActivity<BankCardModel> {

    private boolean mIsselect;//用户打开类型是否是选择银行卡

    /**
     * @param context
     */
    public static void open(Context context, boolean isSelect) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackCardListActivity.class);
        intent.putExtra("isSelect", isSelect);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public void topTitleViewRightClick() {
        AddBackCardActivity.open(BackCardListActivity.this);
    }

    @Override
    public BaseQuickAdapter getAdapter(List<BankCardModel> listData) {
        return new BaseQuickAdapter<BankCardModel, BaseViewHolder>(R.layout.item_my_bank_card, listData) {
            @Override
            protected void convert(BaseViewHolder holder, final BankCardModel bankCardModel) {
                if (bankCardModel == null) return;

                holder.setOnClickListener(R.id.img_back_bg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIsselect) {//如果是选择银行卡
                            EventBus.getDefault().post(bankCardModel);
                            finish();
                        } else {
                            UpdateBackCardActivity.open(mContext, bankCardModel);
                        }
                    }
                });

                int logoId = R.mipmap.logo_defalut;
                int backId = R.mipmap.back_default;
                if (!TextUtils.isEmpty(bankCardModel.getBankCode())) {
                    logoId = mContext.getResources().getIdentifier("logo_" + bankCardModel.getBankCode().toLowerCase(), "mipmap", mContext.getPackageName());
                    backId = mContext.getResources().getIdentifier("back_" + bankCardModel.getBankCode().toLowerCase(), "mipmap", mContext.getPackageName());
                }

                ImgUtils.loadBankLogo(mContext, logoId, (ImageView) holder.getView(R.id.img_bankCart));
                ImgUtils.loadBankBg(mContext, backId, (ImageView) holder.getView(R.id.img_back_bg));

                holder.setText(R.id.txt_name, bankCardModel.getBankName());
                if (!TextUtils.isEmpty(bankCardModel.getBankcardNumber()) && bankCardModel.getBankcardNumber().length() > 5) {
                    holder.setText(R.id.txt_number, bankCardModel.getBankcardNumber().substring(bankCardModel.getBankcardNumber().length() - 4, bankCardModel.getBankcardNumber().length()));
                } else {
                    holder.setText(R.id.txt_number, bankCardModel.getBankcardNumber());
                }

            }
        };
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, boolean canShowDialog) {
        Map<String, String> object = new HashMap<>();

        object.put("systemCode", MyCdConfig.SYSTEMCODE);
        object.put("token", SPUtilHelpr.getUserToken());
        object.put("userId", SPUtilHelpr.getUserId());
        object.put("start", "1");
        object.put("limit", "10");

        Call call = RetrofitUtils.getBaseAPiService().getCardListData("802015", StringUtils.getJsonToString(object));  //签约界面也使用了获取银行卡列表接口 SigningSureActivity

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MyBankCardListMode>(this) {
            @Override
            protected void onSuccess(MyBankCardListMode data, String SucMessage) {
                mRefreshHelper.setData(data.getList());

                if (mRefreshHelper.getmAdapter().getData() != null && mRefreshHelper.getmAdapter().getData().size() > 0) {
                    mBaseBinding.titleView.setRightTitle("");
                } else {
                    mBaseBinding.titleView.setRightTitle("添加");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(BackCardListActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mIsselect = getIntent().getBooleanExtra("isSelect", true);
        }

        mBaseBinding.titleView.setMidTitle("我的银行卡");

        mRefreshHelper.getmRefreshLayout().setEnableLoadmore(false);
        mRefreshHelper.getmRefreshLayout().setEnableRefresh(false);
    }

    @Override
    protected String getErrorInfo() {
        return "暂无银行卡";
    }
}
