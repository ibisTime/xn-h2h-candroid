package com.cdkj.h2hwtw.other;

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

}
