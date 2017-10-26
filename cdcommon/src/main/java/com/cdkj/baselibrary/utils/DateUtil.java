package com.cdkj.baselibrary.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author lsb
 * @date 2012-5-29 下午11:31:09
 */
public class DateUtil {
    public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FMT_YMD = "MM月dd日";

    public static final String DATE_MMddHHmm = "MM-dd HH:mm";
    public static final String DATE_YYMMddHHmm = "yyyy-MM-dd HH:mm";

    public static final String DATE_YMD_H = "yyyy-MM-dd HH点";

    public static final String DATE_YMD = "yyyy-MM-dd";

    public static final String DATE_ymd = "yyyyMMddHHmm";

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }

    /**
     * date1比date2更新（更迟，更晚），返回true，否则返回false
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNewer(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        return d1 > d2;
    }

    /**
     * date1等于date2 或更新（更迟，更晚），返回true，否则返回false
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNewer2(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        return d1 >= d2;
    }

    /**
     * 大于等于今天返回true
     *
     * @param
     * @param
     * @return
     */
    public static boolean isNewer2(int day1, int month, Calendar now) {
        if (day1 == 0 || now == null) {
            return false;
        }
        int d1 = now.get(Calendar.DAY_OF_MONTH);
        int d2 = now.get(Calendar.MONTH) + 1;
        return (day1 >= d1 && month >= d2) || month > d2;
    }

    public static Date parse(String input, String fmt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.parse(input);
        } catch (ParseException e) {

        }

        SimpleDateFormat sdf1 = new SimpleDateFormat(fmt);
        try {

            return sdf1.parse("1970-01-01 00:00");

        } catch (ParseException e) {
        }

        return new Date();
    }

    public static Date parseNoException(String input) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FMT);
            return sdf.parse(input);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse(String input) {
        return parse(input, DATE_YMD);
    }


    public static String format(Date date, String fmt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FMT);
    }


    public static long getAlarmTime() {
        Date date = new Date();
        return date.getTime() + 1000 * 30;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取日期对于星期
     * @param dt
     * @return
     */
    public static int getWeekOfDateIndex(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    public static String getWeekOfDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(fmt.parse(date));
        } catch (ParseException e) {
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }


    /**
     * 根据提供的年月日获取该月份的第一天
     *
     * @param year
     * @param monthOfYear
     * @return
     * @Description: (这里用一句话描述这个方法的作用)
     * @Author: gyz
     * @Since: 2017-1-9下午2:26:57
     */
    public static Date getSupportBeginDayofMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDate = cal.getTime();
        return firstDate;
    }

    /**
     * 根据提供的年月获取该月份的最后一天
     *
     * @param year
     * @param monthOfYear
     * @return
     */
    public static Date getSupportEndDayofMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDate = cal.getTime();

        return lastDate;
    }

    //获取两个data 之间的 data集合
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList();
        lDate.add(beginDate);//把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后士大夫
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);//把结束时间加入集合
        return lDate;
    }

    /**
     * 获取本月的所有日期集合
     *
     * @return
     */
    public static List<Date> getNowMonthDataList() {

        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);//获取年份
        int nowMonth = now.get(Calendar.MONTH);//获取月份

        Date beginDate = DateUtil.getSupportBeginDayofMonth(nowYear, nowMonth);//获取本月第一天
        Date endDate = DateUtil.getSupportEndDayofMonth(nowYear, nowMonth); //获取本月最后一天

        return DateUtil.getDatesBetweenTwoDate(beginDate, endDate);

    }


    //获取指定日期到今天的天大小
    public static int getDatesBetweenDays(Date beginDate) {

        Date endDate = new Date();
        int days = 1;
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后士大夫
            if (endDate.after(cal.getTime())) {
                days++;
            } else {
                break;
            }
        }
        return days;
    }

    //得到后两个月的日期
    public static Date getMonthToDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        Date formNow3Month = calendar.getTime();
        return formNow3Month;
    }

    /**
     * 获取 当前日期几天后 的日期用于显示
     *
     * @param day
     * @return
     */
    public static String getShowDayToData(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date formNow3Month = calendar.getTime();
        return format(formNow3Month, DATE_YMD);
    }

    public static String getCurrentDate() {
        return format(new Date(), "yyyy-MM-dd");
    }


    public static String formatStringData(String s, String format) {
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(format)) {
            return "";
        }
        return DateUtil.format(new Date(s), format);
    }

    public static Date parseStringData(String s, String format) {
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(format)) {
            return null;
        }

        return DateUtil.parse(s, format);
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 获取用户登录时间描述
     *
     * @param time
     * @return
     */
    public static String getLoginDataInfo(String time) {
        Calendar before = Calendar.getInstance();
        before.setTime(new Date(time));
        Calendar now = Calendar.getInstance();

        int nowYear = now.get(Calendar.YEAR);//获取年份
        int beforeYear = before.get(Calendar.YEAR);//获取年份

        int nowMonth = now.get(Calendar.MONTH);//获取月份
        int beforeMonth = before.get(Calendar.MONTH);//获取月份

        if (nowYear > beforeYear) {
            int diffy = nowYear - beforeYear;
            int diffm = diffy * 12 + nowMonth - beforeMonth;
            return diffm < 12 ? diffm + "月前来过" : diffy + "年前来过";
        }
        if (nowMonth > beforeMonth) {
            return nowMonth - beforeMonth + "月前来过";
        }

        int beforeDay = before.get(Calendar.DATE);//获取日
        int nowDay = now.get(Calendar.DATE);//获取日
        if (nowDay > beforeDay) {
            return nowDay - beforeDay + "天前来过";
        }

        int beforeHour = before.get(Calendar.HOUR);//小时
        int nowHour = now.get(Calendar.HOUR);//小时
        if (nowHour > beforeHour) {
            return nowHour - beforeHour + "小时前来过";
        }
        int beforeMinute = before.get(Calendar.MINUTE);//小时
        int nowMinute = now.get(Calendar.MINUTE);//小时

        int diffMinute = nowMinute - beforeMinute;

        return diffMinute <= 5 ? "刚刚来过" : diffMinute + "分钟前来过";
    }

    /**
     * 获取用户登录在线时长描述
     *
     * @param time
     * @return
     */
    public static String getLoginDataInfo2(String time) {
        Calendar before = Calendar.getInstance();
        before.setTime(new Date(time));
        Calendar now = Calendar.getInstance();

        int nowYear = now.get(Calendar.YEAR);//获取年份
        int beforeYear = before.get(Calendar.YEAR);//获取年份

        int nowMonth = now.get(Calendar.MONTH);//获取月份
        int beforeMonth = before.get(Calendar.MONTH);//获取月份

        if (nowYear > beforeYear) {
            int diffy = nowYear - beforeYear;
            int diffm = diffy * 12 + nowMonth - beforeMonth;
            return diffm < 12 ? diffm + "月" : diffy + "年";
        }
        if (nowMonth > beforeMonth) {
            return nowMonth - beforeMonth + "月";
        }

        int beforeDay = before.get(Calendar.DATE);//获取日
        int nowDay = now.get(Calendar.DATE);//获取日
        if (nowDay > beforeDay) {
            return nowDay - beforeDay + "天";
        }

        int beforeHour = before.get(Calendar.HOUR);//小时
        int nowHour = now.get(Calendar.HOUR);//小时
        if (nowHour > beforeHour) {
            return nowHour - beforeHour + "小时";
        }
        int beforeMinute = before.get(Calendar.MINUTE);//小时
        int nowMinute = now.get(Calendar.MINUTE);//小时

        int diffMinute = nowMinute - beforeMinute;

        return diffMinute <= 5 ? "刚刚" : diffMinute + "分钟";
    }

}
