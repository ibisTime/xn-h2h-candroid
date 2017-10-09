package com.cdkj.baselibrary.utils.update;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cdkj.baselibrary.R;

import java.util.Locale;

/**
 * 下载进度dialog
 */

public class DownloadDialog extends AlertDialog {

    private ProgressBar mProgressBar;
    private TextView mTipTxt;
    private TextView mProTxt;
    private boolean mIsForceUpdate=false;

    public DownloadDialog(@NonNull Context context, String title,boolean isForceUpdate) {
        super(context);
        mIsForceUpdate=isForceUpdate;
        setTitle(title);
        init();
    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.layout_downloading, null, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mTipTxt = (TextView) view.findViewById(R.id.tv_loading_tip);
        mProTxt = (TextView) view.findViewById(R.id.tv_loading_pro);
        this.setView(view);
    }

    public void setProgress(float progress) {
        if (progress >= 100) {
            mProgressBar.setProgress(100);
            mTipTxt.setText("下载完成");
            mProTxt.setText("100 / 100");
        } else if(progress==0) {
            mTipTxt.setText("下载失败");
            this.setCancelable(!mIsForceUpdate);
            this.setTitle("下载失败,请到应用市场进行更新");
        }else {
            mProgressBar.setProgress((int) progress);
            mTipTxt.setText(String.format(Locale.getDefault(), "%.2f%%", progress));
            mProTxt.setText((int) progress + " / 100");
        }
    }
}
