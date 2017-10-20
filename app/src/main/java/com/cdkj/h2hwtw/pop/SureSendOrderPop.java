package com.cdkj.h2hwtw.pop;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cdkj.baselibrary.popup.BasePopupWindow;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.PopSendOrderBinding;

/**
 * Created by 李先俊 on 2017/10/20.
 */

public class SureSendOrderPop extends BasePopupWindow {

    private PopSendOrderBinding mBinding;

    public SureSendOrderPop(Activity context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        mBinding = DataBindingUtil.inflate(mContext.getLayoutInflater(), R.layout.pop_send_order, null, false);
        return mBinding.getRoot();
    }

    @Override
    public View initAnimaView() {
        return mBinding.linSendRoot;
    }

    @Override
    protected Animation initShowAnimation() {
        return AnimationUtils.loadAnimation(mContext, R.anim.pop_bottom_in);
    }

    @Override
    protected Animation initExitAnimation() {
        return AnimationUtils.loadAnimation(mContext, R.anim.pop_bottom_out);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }
}
