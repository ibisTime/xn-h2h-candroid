package com.cdkj.h2hwtw.other;

import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.ProductListModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by cdkj on 2017/11/2.
 */

public class ProductHelper {

      /*3=已上架 4=已卖出，5=已下架 ,6=强制下架*/

    /**
     * 总价格 =单价 * 折扣 +加运费
     *
     * @return
     */
    public static BigDecimal getAllMoney(ProductListModel.ListBean prodata) {

        if (prodata == null) return null;

        BigDecimal discountMoney = BigDecimalUtils.multiply(prodata.getPrice(), prodata.getDiscount());

        BigDecimal allMoney = BigDecimalUtils.add(discountMoney, prodata.getYunfei());

        return allMoney;

    }

    /**
     * 计算要显示的折扣
     *
     * @return
     */
    public static String getShowDiscount(BigDecimal discount) {
        if (discount == null) return "0";
        double zhek = (discount.doubleValue() * 10);
        NumberFormat nf = new DecimalFormat("#.##");
        return nf.format(zhek);
    }

    /**
     * 是否参加了包邮活动
     *
     * @param activityType
     * @return
     */
    public static boolean isJoinSendActivity(String activityType) {
        return TextUtils.equals("2", activityType);
    }

    /**
     * 是否参加了折扣活动
     *
     * @param activityType
     * @return
     */
    public static boolean isJoinZhekouactivity(String activityType) {
        return TextUtils.equals("1", activityType);
    }

    /**
     * 当前产品是否参加了活动 1是 0否
     *
     * @param isJoin
     * @return
     */
    public static boolean isJoinActivity(String isJoin) {
        return TextUtils.equals("1", isJoin);
    }
    /**
     * 当前产品是否全新
     *
     * @param
     * @return
     */
    public static boolean isNewProduct(String isNew) {
        return TextUtils.equals("1", isNew);
    }





}
