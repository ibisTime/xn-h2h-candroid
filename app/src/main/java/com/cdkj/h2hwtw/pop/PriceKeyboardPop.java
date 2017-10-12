package com.cdkj.h2hwtw.pop;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.cdkj.baselibrary.popup.BasePopupWindow;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.PopPriceCalculateBinding;

/**
 * 发布价格计算pupup
 * Created by cdjk on 2017/10/11.
 */

public class PriceKeyboardPop extends BasePopupWindow {

    private PopPriceCalculateBinding popBinding;

    private int mEditIndex;//输入下标记录

    private StringBuffer mPriceEditInputString;//输入的价格记录
    private StringBuffer mPriceOldEditInputString;//输入的原价记录
    private StringBuffer mPriceSendEditInputString;//输入的运费记录


    public PriceKeyboardPop(Activity context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        if (popBinding == null) {
            popBinding = DataBindingUtil.inflate(mContext.getLayoutInflater(), R.layout.pop_price_calculate, null, false);
        }
        initKeyListener();
        initVar();

        return popBinding.getRoot();
    }

    private void initVar() {
        mEditIndex = 0;
        mPriceEditInputString = new StringBuffer();
        mPriceOldEditInputString = new StringBuffer();
        mPriceSendEditInputString = new StringBuffer();

    }

    private void initKeyListener() {
        setNumKeyListener();

        popBinding.layouotKeyboard.fraDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteIndextInput(mEditIndex);
            }
        });


        setEditToouch(popBinding.editPrice, 0);
        setEditToouch(popBinding.editPriceOld, 1);
        setEditToouch(popBinding.editSendPrice, 2);


        popBinding.layouotKeyboard.btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                hideKeyboard(view.getWindowToken());
                popBinding.editPrice.setEnabled(true);
            }
        });


    }

    /**
     * 禁用输入法
     *
     * @param edview
     */
    private void setEditToouch(final EditText edview, final int editIndex) {
        edview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mEditIndex = editIndex;
                int inType = edview.getInputType(); // backup the input type
                edview.setInputType(InputType.TYPE_NULL); // disable soft input
                edview.onTouchEvent(event); // call native handler
                edview.setInputType(inType); // restore input type
                edview.setSelection(edview.getText().length());
                return true;
            }

        });
    }

    /**
     * 设置数字键盘监听
     */
    private void setNumKeyListener() {
        setViewClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     editIndextInput(view, mEditIndex);
                                 }
                             }, popBinding.layouotKeyboard.tvKey0, popBinding.layouotKeyboard.tvKey1, popBinding.layouotKeyboard.tvKey2, popBinding.layouotKeyboard.tvKey3,
                popBinding.layouotKeyboard.tvKey4, popBinding.layouotKeyboard.tvKey5, popBinding.layouotKeyboard.tvKey6, popBinding.layouotKeyboard.tvKey7,
                popBinding.layouotKeyboard.tvKey8, popBinding.layouotKeyboard.tvKey9/*, popBinding.layouotKeyboard.tvKeyNull*/);
    }

    /**
     * 根据下标设置输入那个一editText
     *
     * @param view
     * @param editIndex
     */

    private void editIndextInput(View view, int editIndex) {

        switch (editIndex) {
            case 0:
                if (TextUtils.equals(view.getTag().toString(), "0") && TextUtils.isEmpty(mPriceEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }
                if (StringUtils.parseInt(mPriceEditInputString.toString()) > 999999) {
                    ToastUtil.show(mContext, "价格不能超过999999哦");
                    return;
                }
                mPriceEditInputString.append(view.getTag().toString());
                popBinding.editPrice.setText(mPriceEditInputString.toString());
                popBinding.editPrice.setSelection(mPriceEditInputString.toString().length());
                break;
            case 1:
                if (TextUtils.equals(view.getTag().toString(), "0") && TextUtils.isEmpty(mPriceOldEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }
                if (StringUtils.parseInt(mPriceOldEditInputString.toString()) > 999999) {
                    ToastUtil.show(mContext, "原价不能超过999999哦");
                    return;
                }
                mPriceOldEditInputString.append(view.getTag().toString());
                popBinding.editPriceOld.setText(mPriceOldEditInputString.toString());
                popBinding.editPriceOld.setSelection(mPriceOldEditInputString.toString().length());
                break;
            case 2:
                if (TextUtils.equals(view.getTag().toString(), "0") && TextUtils.isEmpty(mPriceSendEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }
                if (StringUtils.parseInt(mPriceSendEditInputString.toString()) > 999) {
                    ToastUtil.show(mContext, "运费价不能超过999哦");
                    return;
                }
                mPriceSendEditInputString.append(view.getTag().toString());
                popBinding.editSendPrice.setText(mPriceSendEditInputString.toString());
                popBinding.editSendPrice.setSelection(mPriceSendEditInputString.toString().length());
                break;

        }
    }

    /**
     * 根据下标执行删除
     *
     * @param
     * @param editIndex
     */
    private void deleteIndextInput(int editIndex) {
        switch (editIndex) {
            case 0:
                if (TextUtils.isEmpty(mPriceEditInputString.toString())) {
                    return;
                }
                mPriceEditInputString.deleteCharAt(mPriceEditInputString.toString().length() - 1);
                popBinding.editPrice.setText(mPriceEditInputString.toString());
                popBinding.editPrice.setSelection(mPriceEditInputString.toString().length());
                break;
            case 1:
                if (TextUtils.isEmpty(mPriceOldEditInputString.toString())) {
                    return;
                }
                mPriceOldEditInputString.deleteCharAt(mPriceOldEditInputString.toString().length() - 1);
                popBinding.editPriceOld.setText(mPriceOldEditInputString.toString());
                popBinding.editPriceOld.setSelection(mPriceOldEditInputString.toString().length());
                break;
            case 2:
                if (TextUtils.isEmpty(mPriceSendEditInputString.toString())) {
                    return;
                }
                mPriceSendEditInputString.deleteCharAt(mPriceSendEditInputString.toString().length() - 1);
                popBinding.editSendPrice.setText(mPriceSendEditInputString.toString());
                popBinding.editSendPrice.setSelection(mPriceSendEditInputString.toString().length());
                break;

        }
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
