package com.cdkj.h2hwtw.pop;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.popup.BasePopupWindow;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.databinding.PopPriceCalculateBinding;
import com.cdkj.h2hwtw.model.PriceKeyBoardListenerModel;

import java.math.BigDecimal;

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

    private PriceKeyBoardPopListener mSureListener;

    private PriceKeyBoardListenerModel mPriceModel;//包含价格

    private boolean mIsJoinSendActivity = false;

    /**
     * @param context
     * @param priceModel
     * @param isJoinSendActivity  是否参加了包邮活动 参加了不能对邮费进行编辑
     * @param listener
     */
    public PriceKeyboardPop(Activity context, PriceKeyBoardListenerModel priceModel, boolean isJoinSendActivity, PriceKeyBoardPopListener listener) {
        super(context);
        mSureListener = listener;
        this.mPriceModel = priceModel;
        this.mIsJoinSendActivity = isJoinSendActivity;
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


    @Override
    public void showPopupWindow() {

        if (mPriceModel != null) {
            mPriceEditInputString.append(mPriceModel.getPrice());
            mPriceOldEditInputString.append(mPriceModel.getOldPrice());
            mPriceSendEditInputString.append(mPriceModel.getSendPrice());

            LogUtil.E("价格" + mPriceEditInputString.toString());

            popBinding.editPrice.setText(mPriceEditInputString.toString());
            popBinding.editPriceOld.setText(mPriceOldEditInputString.toString());
            popBinding.editSendPrice.setText(mPriceSendEditInputString.toString());

            if (!mIsJoinSendActivity) {
                setIsSendState(mPriceModel.isCanSend());
            } else {
                popBinding.checkboxCanSend.setChecked(true);
                popBinding.editSendPrice.setEnabled(false);
            }

            if (!TextUtils.isEmpty(popBinding.editPrice.getText().toString())) {
                popBinding.editPrice.setSelection(popBinding.editPrice.getText().toString().length());
            }

        }else{
            if (!mIsJoinSendActivity) {
                popBinding.editSendPrice.setEnabled(true);
            } else {
                popBinding.checkboxCanSend.setChecked(true);
                popBinding.editSendPrice.setEnabled(false);
            }

        }

        super.showPopupWindow();
    }

    private void initKeyListener() {
        setNumKeyListener();

        //删除
        popBinding.layouotKeyboard.fraDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteIndextInput(mEditIndex);
            }
        });


        setEditToouch(popBinding.editPrice, 0);
        setEditToouch(popBinding.editPriceOld, 1);
        setEditToouch(popBinding.editSendPrice, 2);

        //包邮点击
        popBinding.linSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsJoinSendActivity) {
                    setSendState();
                }
            }
        });


        //确认
        popBinding.layouotKeyboard.btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriceKeyBoardListenerModel model = new PriceKeyBoardListenerModel();

                model.setCanSend(popBinding.checkboxCanSend.isChecked());
                if (TextUtils.isEmpty(popBinding.editPrice.getText().toString())) {
                    UITipDialog.showInfo(mContext, "请输入价格");
                    model.setPrice("0");
                    mPriceEditInputString.setLength(0);
                    return;
                } else {
                    model.setPrice(popBinding.editPrice.getText().toString());
                }
                if (TextUtils.isEmpty(popBinding.editPriceOld.getText().toString())) {
                    model.setOldPrice("0");

                    UITipDialog.showInfo(mContext, "请输入原价");
                    mPriceOldEditInputString.setLength(0);
                    return;
                } else {
                    model.setOldPrice(popBinding.editPriceOld.getText().toString());
                }

                if (TextUtils.isEmpty(popBinding.editSendPrice.getText().toString())) {
                    model.setSendPrice("0");
                    mPriceSendEditInputString.setLength(0);
                } else {
                    model.setSendPrice(popBinding.editSendPrice.getText().toString());
                }

                if (!popBinding.checkboxCanSend.isChecked() && TextUtils.isEmpty(popBinding.editSendPrice.getText().toString())) {
                    UITipDialog.showInfo(mContext, "请输入邮费");
                    return;
                }

                if (popBinding.checkboxCanSend.isChecked()) {
                    model.setSendPrice("0");
                }


                if (mSureListener != null) {
                    mSureListener.sureInputDone(model);
                }


                dismiss();
            }
        });
    }

    /**
     * 设置是包邮状态
     */
    private void setSendState() {
        popBinding.checkboxCanSend.setChecked(!popBinding.checkboxCanSend.isChecked());
        popBinding.editSendPrice.setEnabled(!popBinding.checkboxCanSend.isChecked());
        if (popBinding.checkboxCanSend.isChecked()) {
            popBinding.editSendPrice.setText("");
            mPriceSendEditInputString.setLength(0);
        }
    }

    /**
     * 设置是包邮状态
     */
    private void setIsSendState(boolean isSend) {
        popBinding.checkboxCanSend.setChecked(isSend);
        popBinding.editSendPrice.setEnabled(!isSend);
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
                popBinding.layouotKeyboard.tvKey8, popBinding.layouotKeyboard.tvKey9, popBinding.layouotKeyboard.tvKeyPoint);
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

                int indexof = mPriceEditInputString.toString().indexOf(".");

                if (TextUtils.equals(view.getTag().toString(), ".") && indexof != -1) {//禁止输入两个小数点
                    return;
                }


                if (indexof != -1 && StringUtils.subStringEnd(mPriceEditInputString.toString(), indexof).length() - 1 >= 2) {//小数点后最多输入两位
                    LogUtil.E(StringUtils.subStringEnd(mPriceEditInputString.toString(), indexof - 1) + "输入");
                    return;
                }

                if (TextUtils.equals(view.getTag().toString(), ".") && TextUtils.isEmpty(mPriceEditInputString.toString())) { //禁止第一个输入是.
                    return;
                }

                mPriceEditInputString.append(view.getTag().toString());
                if (new BigDecimal(mPriceEditInputString.toString()).floatValue() > 999999) {
                    UITipDialog.showInfo(mContext, "价格不能超过999999哦");
                    mPriceEditInputString.deleteCharAt(mPriceEditInputString.toString().length() - 1);

                    return;
                }

                if (new BigDecimal(mPriceEditInputString.toString()).floatValue() == 999999 &&
                        TextUtils.equals(StringUtils.subStringLenghtEnd(mPriceEditInputString.toString()), ".")) {                      //去除999999.的情况
                    mPriceEditInputString.deleteCharAt(mPriceEditInputString.toString().length() - 1);
                }

                popBinding.editPrice.setText(mPriceEditInputString.toString());
                popBinding.editPrice.setSelection(mPriceEditInputString.toString().length());
                break;
            case 1:
                if (TextUtils.equals(view.getTag().toString(), "0") && TextUtils.isEmpty(mPriceOldEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }

                int indexof1 = mPriceOldEditInputString.toString().indexOf(".");
                if (TextUtils.equals(view.getTag().toString(), ".") && indexof1 != -1) { //禁止输入两个小数点
                    return;
                }
                if (indexof1 != -1 && StringUtils.subStringEnd(mPriceOldEditInputString.toString(), indexof1).length() - 1 >= 2) {//小数点后最多输入两位
                    LogUtil.E(StringUtils.subStringEnd(mPriceOldEditInputString.toString(), indexof1 - 1) + "输入");
                    return;
                }

                if (TextUtils.equals(view.getTag().toString(), ".") && TextUtils.isEmpty(mPriceOldEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }
                mPriceOldEditInputString.append(view.getTag().toString());

                if (new BigDecimal(mPriceOldEditInputString.toString()).floatValue() > 999999) {
                    UITipDialog.showInfo(mContext, "原价不能超过999999哦");
                    mPriceOldEditInputString.deleteCharAt(mPriceOldEditInputString.toString().length() - 1);
                }

                if (new BigDecimal(mPriceOldEditInputString.toString()).floatValue() == 999999 &&
                        TextUtils.equals(StringUtils.subStringLenghtEnd(mPriceOldEditInputString.toString()), ".")) {                      //去除999999.的情况
                    mPriceOldEditInputString.deleteCharAt(mPriceOldEditInputString.toString().length() - 1);
                }

                popBinding.editPriceOld.setText(mPriceOldEditInputString.toString());
                popBinding.editPriceOld.setSelection(mPriceOldEditInputString.toString().length());
                break;
            case 2:

                if (popBinding.checkboxCanSend.isChecked()) { //如果选择包邮则禁止输入
                    return;
                }

                if (TextUtils.equals(view.getTag().toString(), "0") && TextUtils.isEmpty(mPriceSendEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }
                int indexof2 = mPriceSendEditInputString.toString().indexOf(".");
                if (TextUtils.equals(view.getTag().toString(), ".") && indexof2 != -1) { //禁止输入两个小数点
                    return;
                }
                if (indexof2 != -1 && StringUtils.subStringEnd(mPriceSendEditInputString.toString(), indexof2).length() - 1 >= 2) {//小数点后最多输入两位
                    LogUtil.E(StringUtils.subStringEnd(mPriceSendEditInputString.toString(), indexof2 - 1) + "输入");
                    return;
                }

                if (TextUtils.equals(view.getTag().toString(), ".") && TextUtils.isEmpty(mPriceSendEditInputString.toString())) { //禁止第一个输入是0
                    return;
                }
                mPriceSendEditInputString.append(view.getTag().toString());
                if (new BigDecimal(mPriceSendEditInputString.toString()).floatValue() > 999) {
                    UITipDialog.showInfo(mContext, "运费价不能超过999哦");
                    mPriceSendEditInputString.deleteCharAt(mPriceSendEditInputString.toString().length() - 1);
                }

                if (new BigDecimal(mPriceSendEditInputString.toString()).floatValue() == 999 &&
                        TextUtils.equals(StringUtils.subStringLenghtEnd(mPriceSendEditInputString.toString()), ".")) {                      //去除999999.的情况
                    mPriceSendEditInputString.deleteCharAt(mPriceSendEditInputString.toString().length() - 1);
                }

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

    public interface PriceKeyBoardPopListener {

        void sureInputDone(PriceKeyBoardListenerModel model);

    }
}
