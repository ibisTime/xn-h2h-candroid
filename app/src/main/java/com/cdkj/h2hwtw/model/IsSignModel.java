package com.cdkj.h2hwtw.model;

/**包含签到天数和今天是否已经签到
 * Created by 李先俊 on 2017/10/26.
 */

public class IsSignModel
{


    /**
     * days : 1
     * todaySign : true
     */

    private int days;
    private boolean todaySign;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isTodaySign() {
        return todaySign;
    }

    public void setTodaySign(boolean todaySign) {
        this.todaySign = todaySign;
    }
}
