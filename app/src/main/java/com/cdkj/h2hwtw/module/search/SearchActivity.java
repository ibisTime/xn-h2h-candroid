package com.cdkj.h2hwtw.module.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.BaseRefreshHelperActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductListAdapter;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.ActivitySearchBinding;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.other.SearchSaveUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 搜索
 * Created by cdkj on 2017/10/24.
 */

public class SearchActivity extends BaseRefreshHelperActivity {

    private ActivitySearchBinding mBinding;

    private List<String> searStrings = new ArrayList<>();


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public BaseQuickAdapter getAdapter(List listData) {
        ProductListAdapter mProductAdapter = new ProductListAdapter(listData);
        return mProductAdapter;
    }

    @Override
    public void getListDataRequest(int pageindex, int limit, final boolean isShowDialog) {
        Map<String, String> map = new HashMap();
        map.put("limit", limit + "");
        map.put("pageindex", pageindex + "");
        map.put("start", pageindex + "");
        map.put("status", "3");
        map.put("isJoin", "0");
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("name", mBinding.editSerchView.getText().toString());
        Call call = RetrofitUtils.createApi(MyApiServer.class).getProductList("808025", StringUtils.getJsonToString(map));

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

    private void startSearch() {

        if (TextUtils.isEmpty(mBinding.editSerchView.getText().toString())) {
            UITipDialog.showInfo(this, "请输入搜索内容");
            return;
        }

        mRefreshHelper.onDefaluteMRefresh(true);

        if (mBinding.linSearchHistory.getVisibility() == View.VISIBLE) {
            mBinding.linSearchHistory.setVisibility(View.GONE);
            mBinding.refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setEnableLoadmoreWhenContentNotFull(true);//不满一行启动上啦加载
        mBinding.refreshLayout.setEnableAutoLoadmore(false);//禁用惯性
        return mBinding.refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mBinding.rv;
    }

    @Override
    public View addMainView() {
        mBaseBinding.contentView.setShowLoadingView(true);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_search, null, false);

        return mBinding.getRoot();
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        initEditKeyPoard();
        mBinding.tvSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initSearchInfo();
    }

    private void initSearchInfo() {
        //解析搜索信息
        mSubscription.add(Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull String s) throws Exception {
                        return JSON.parseArray(SearchSaveUtils.getSaveSearchInfo(), String.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mBaseBinding.contentView.showContent(true);
                    }
                })
                .filter(new Predicate<List<String>>() {
                    @Override
                    public boolean test(@NonNull List<String> strings) throws Exception {
                        return strings != null;
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        searStrings = strings;
                        initFlexBoxLayou();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    /**
     * 动态添加ziVIew
     */
    private void initFlexBoxLayou() {
        if (searStrings == null || searStrings.isEmpty()) {
            return;
        }
        for (String searString : searStrings) {
            if (TextUtils.isEmpty(searString)) return;
            mBinding.flexboxLayoutSearch.addView(createNewFlexItemTextView(searString));
        }
    }


    /**
     * 动态创建TextView
     *
     * @param
     * @return
     */
    private TextView createNewFlexItemTextView(String str) {
        final TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(str);
        textView.setTextSize(14);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_black_app));
        textView.setBackgroundResource(R.drawable.search_bg_gray);
        textView.setTag(str);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.editSerchView.setText(textView.getTag() + "");
                startSearch();
            }
        });
        int padding = DisplayHelper.dip2px(this, 5);
        int paddingLeftAndRight = DisplayHelper.dip2px(this, 15);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = DisplayHelper.dip2px(this, 5);
        int marginTop = DisplayHelper.dip2px(this, 16);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }


    /**
     * 设置输入键盘
     */
    private void initEditKeyPoard() {

        mBinding.editSerchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final String str = v.getText().toString();

                if (TextUtils.isEmpty(str)) {
                    UITipDialog.showInfo(SearchActivity.this, "请输入搜索内容");
                    return false;
                }

                startSearch();

                addSearchInfo(str);

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//隐藏键盘
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 最多保存10个搜索记录
     *
     * @param str
     */
    private void addSearchInfo(String str) {
        if (searStrings == null || searStrings.contains(str)) {
            return;
        }
        if (searStrings.size() >= 10) {
            searStrings.remove(searStrings.size() - 1);
        }
        searStrings.add(0, str);
        SearchSaveUtils.saveSearchInfo(StringUtils.getJsonToString(searStrings));
    }

    @Override
    protected String getErrorInfo() {
        return "暂无搜索内容";
    }
}
