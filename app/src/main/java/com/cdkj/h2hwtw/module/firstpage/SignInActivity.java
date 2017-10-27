package com.cdkj.h2hwtw.module.firstpage;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.ScrollGridLayoutManager;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.SigninDateAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivitySignInBinding;
import com.cdkj.h2hwtw.model.AmountModel;
import com.cdkj.h2hwtw.model.IsSignModel;
import com.cdkj.h2hwtw.model.SignDatetimeModel;
import com.cdkj.h2hwtw.model.SignInTotalAmountModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 签到
 * Created by cdkj on 2017/10/26.
 */

public class SignInActivity extends AbsBaseLoadActivity {

    private ActivitySignInBinding mBinding;

    private SigninDateAdapter signinDateAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("每日签到");
        mBaseBinding.titleView.setRightTitle("签到规则");
        mBinding.tvData.setText(DateUtil.format(new Date(), "yyyy年MM月"));

        mBinding.recyclerDate.setLayoutManager(new ScrollGridLayoutManager(this, 7));

        signinDateAdapter = new SigninDateAdapter(new ArrayList<Date>());
        mBinding.recyclerDate.setAdapter(signinDateAdapter);

        initDateLayout();
        signInRequest();

    }

    /**
     * 设置日历数据
     */
    private void initDateLayout() {

        mSubscription.add(Observable.just(DateUtil.getNowMonthDataList())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<List<Date>, List<Date>>() {
                    @Override
                    public List<Date> apply(List<Date> dates) throws Exception {
                        List<Date> dateList = new ArrayList<>();
                        Calendar now = Calendar.getInstance();
                        int nowYear = now.get(Calendar.YEAR);//获取年份
                        int nowMonth = now.get(Calendar.MONTH);//获取月份
                        for (int i = 0; i < DateUtil.getWeekOfDateIndex(DateUtil.getSupportBeginDayofMonth(nowYear, nowMonth)); i++) {
                            dateList.add(null);
                        }
                        dateList.addAll(dates);
                        return dateList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Date>>() {
                    @Override
                    public void accept(List<Date> dates) throws Exception {
                        signinDateAdapter.replaceData(dates);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    public void topTitleViewRightClick() {
        WebViewActivity.openkey(SignInActivity.this, "签到规则", "signRegulation");
    }

    /**
     * 签到请求
     */
    public void signInRequest() {

        Map map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelpr.getUserId());


        Call<BaseResponseModel<CodeModel>> call = RetrofitUtils.getBaseAPiService().codeRequest("805140", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCode())) {
                    return;
                }

                UITipDialog.showSuccess(SignInActivity.this, "今日签到成功");

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showInfo(SignInActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                isSignInRequest();
                getSignDataRequest();
                getAmountaccountNumber();
                disMissLoading();
            }
        });

    }

    /**
     * 判断今日是否已经签到
     */
    public void isSignInRequest() {

        Map map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelpr.getUserId());


        Call<BaseResponseModel<IsSignModel>> call = RetrofitUtils.createApi(MyApiServer.class).isSign("805148", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<IsSignModel>(this) {
            @Override
            protected void onSuccess(IsSignModel data, String SucMessage) {
                if (data.isTodaySign()) {
                    mBinding.tvSignInInfo.setText("签到成功");
                } else {
                    mBinding.tvSignInInfo.setText("签到失败");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFall(SignInActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取AmountaccountNumber
     */
    public void getAmountaccountNumber() {
        if (!SPUtilHelpr.isLoginNoStart()) {  //没有登录不用请求
            return;
        }
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("currency", "JF");
        map.put("token", SPUtilHelpr.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAmount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AmountModel>(this) {
            @Override
            protected void onSuccess(List<AmountModel> data, String SucMessage) {
                if (data != null && data.size() > 0 && data.get(0) != null) {
                    getSignJf(data.get(0).getAccountNumber());
                }
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


    /**
     * 获取签到积分
     */
    public void getSignJf(String accountNumber) {

        if (TextUtils.isEmpty(accountNumber)) {
            return;
        }

        Map map = RetrofitUtils.getRequestMap();

        map.put("accountNumber", accountNumber);
        map.put("bizType", "02");


        Call<BaseResponseModel<SignInTotalAmountModel>> call = RetrofitUtils.createApi(MyApiServer.class).getSigninAmount("802900", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SignInTotalAmountModel>(this) {
            @Override
            protected void onSuccess(SignInTotalAmountModel data, String SucMessage) {
                if (data.getTotalAmount() > 0) {
                    mBinding.tvSignJf.setText(data.getTotalAmount() / 1000 + "");
                    return;
                }
                mBinding.tvSignJf.setText(data.getTotalAmount() + "");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取签到数据列表 用于UI显示
     */
    public void getSignDataRequest() {
        Map map = RetrofitUtils.getRequestMap();
        map.put("start", "1");
        map.put("limit", "31");
        map.put("orderDir", "desc");
        map.put("orderColumn", "signDatetime");
        map.put("userId", SPUtilHelpr.getUserId());

        Call<BaseResponseModel<ResponseInListModel<SignDatetimeModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getSignListData("805145", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<SignDatetimeModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<SignDatetimeModel> data, String SucMessage) {
                signinDateAdapter.setSignData(data.getList());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });


    }
}
