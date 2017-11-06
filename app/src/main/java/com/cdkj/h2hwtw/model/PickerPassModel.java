package com.cdkj.h2hwtw.model;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by 李先俊 on 2017/11/6.
 */

public class PickerPassModel implements IPickerViewData {

    private boolean isSure;//true 代表确认


    @Override
    public String getPickerViewText() {
        return isSure ? "通过" : "不通过";
    }

    public boolean isSure() {
        return isSure;
    }

    public void setSure(boolean sure) {
        isSure = sure;
    }
}
