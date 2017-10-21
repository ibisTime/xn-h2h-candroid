package com.cdkj.h2hwtw.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.model.AddressModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 产品详情页面图片适配器
 * Created by cdkj on 2017/10/12.
 */

public class ProductImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public ProductImgAdapter(@Nullable List<String> data, Context context) {
        super(R.layout.item_product_detail_img, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        if (item == null) return;
        ImageView img = (ImageView) viewHolder.getView(R.id.img_details);
        ImageView img2 = (ImageView) viewHolder.getView(R.id.img_details2);

        if (isHalfWidth(viewHolder.getLayoutPosition())) {
            img2.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            int width = AppUtils.getScreenWidth(context);
            if (width > 0) {
                ViewGroup.LayoutParams params = img2.getLayoutParams();
                params.height = Math.abs(width / 2 - DisplayHelper.dp2px(context, 30));
                img2.setLayoutParams(params);
            }
            ImgUtils.loadImgNoPlaceholder(context, MyCdConfig.QINIUURL + item, img2);

        } else {
            ImgUtils.loadImgNoPlaceholder(context, MyCdConfig.QINIUURL + item, img);
            img.setVisibility(View.VISIBLE);
            img2.setVisibility(View.GONE);
        }

    }

    /**
     * 多item列判断
     *
     * @param position
     * @return
     */
    public boolean isHalfWidth(int position) {

        if (position == 0 || position == 1 || position == 2 || getData().size() == 4) {
            return false;
        }

        if (getData().size() % 2 == 0) {
            if (position > 3) {
                return true;
            }
        } else {
            if (position > 2) {
                return true;
            }
        }
        return false;
    }


}
