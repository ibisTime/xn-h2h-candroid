package com.cdkj.baselibrary.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.views.LoadingView;


public final class LoadingDialog extends ProgressDialog {

    private int mWidth;
    private int mHeight;
    private LoadingView mLoadingView;


    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogLight);
        this.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setLayout(mWidth, mHeight);  //设置宽高
        getWindow().setGravity(Gravity.CENTER);  //设置居中
        initData();
        initView();
    }

    private void initData() {
        setCancelable(false);  // 设置当返回键按下是否关闭对话框
        setCanceledOnTouchOutside(false);  // 设置当点击对话框以外区域是否关闭对话框
    }

    private void initView() {
        mLoadingView= (LoadingView) findViewById(R.id.loadingview);
    }

    public void showDialog() {
        if (!isShowing()) {
            show();
        }
    }

    public void closeDialog() {
        if(isShowing()){
            dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
        if(mLoadingView!=null){
            mLoadingView.start();
        }
    }

    @Override
    public void dismiss() {
        if(mLoadingView!=null){
            mLoadingView.stop();
        }
        super.dismiss();
    }
}
