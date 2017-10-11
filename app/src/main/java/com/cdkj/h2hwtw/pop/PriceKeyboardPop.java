package com.cdkj.h2hwtw.pop;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.cdkj.baselibrary.popup.BasePopupWindow;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.PopPriceCalculateBinding;

/**
 * 发布价格计算pupup
 * Created by cdjk on 2017/10/11.
 */

public class PriceKeyboardPop extends BasePopupWindow {

    private PopPriceCalculateBinding popBinding;


    public PriceKeyboardPop(Activity context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        if (popBinding == null) {
            popBinding = DataBindingUtil.inflate(mContext.getLayoutInflater(), R.layout.pop_price_calculate, null, false);
        }
        initKeyListener();

        return popBinding.getRoot();
    }

    private void initKeyListener() {
        setNumKeyListener();

        popBinding.layouotKeyboard.fraDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        popBinding.layouotKeyboard.btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        popBinding.linPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popBinding.editPrice.setEnabled(true);
            }
        });
        popBinding.editPrice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return popBinding.editPrice.isFocusable();
            }
        });

    }

    private void setNumKeyListener() {
        setViewClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {

                                 }
                             }, popBinding.layouotKeyboard.tvKey0, popBinding.layouotKeyboard.tvKey1, popBinding.layouotKeyboard.tvKey2, popBinding.layouotKeyboard.tvKey3,
                popBinding.layouotKeyboard.tvKey4, popBinding.layouotKeyboard.tvKey5, popBinding.layouotKeyboard.tvKey6, popBinding.layouotKeyboard.tvKey7,
                popBinding.layouotKeyboard.tvKey8, popBinding.layouotKeyboard.tvKey9, popBinding.layouotKeyboard.tvKeyNull);
    }


    @Override
    public View initAnimaView() {
        return popBinding.linKeyboardRoot;
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
        return popBinding.layouotKeyboard.fraCancel;
    }
}
