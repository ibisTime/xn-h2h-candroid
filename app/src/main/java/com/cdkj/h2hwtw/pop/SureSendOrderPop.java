package com.cdkj.h2hwtw.pop;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.model.IntroductionDkeyModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.popup.BasePopupWindow;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.api.MyApiServer;
import com.cdkj.h2hwtw.databinding.PopSendOrderBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2017/10/20.
 */

public class SureSendOrderPop extends BasePopupWindow {

    private PopSendOrderBinding mBinding;

    private OptionsPickerView mCompanyPicker;//优惠券选择

    private List<IntroductionDkeyModel> mCompanyModels = new ArrayList<>();

    public SureSendOrderPop(Activity context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        mBinding = DataBindingUtil.inflate(mContext.getLayoutInflater(), R.layout.pop_send_order, null, false);
        initSelectPickerView();
        initListener();
        return mBinding.getRoot();
    }

    private void initListener() {
/*code:801907
json:{"systemCode":"CD-WTW000016","companyCode":"CD-WTW000016","token":"TSYS_USER_WTWTK201710130935341077766","parentKey":"back_kd_company"}*/
        mBinding.linCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompnayRequest();
            }
        });

    }

    private void initSelectPickerView() {

        mCompanyPicker = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                mBinding.tvCompanyName.setText();
            }
        }).setContentTextSize(16).setLineSpacingMultiplier(4).build();
    }

    /**
     * 获取物流公司
     */
    private void getCompnayRequest() {

        Map<String, String> map = new HashMap<>();

        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("token", SPUtilHelpr.getUserToken());
        map.put("parentKey", "back_kd_company");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getdKeyListInfo("801907", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseListCallBack<IntroductionDkeyModel>(mContext) {
            @Override
            protected void onSuccess(List<IntroductionDkeyModel> data, String SucMessage) {
                mCompanyModels.clear();
                mCompanyModels.addAll(data);
                mCompanyPicker.setPicker(mCompanyModels);
                mCompanyPicker.show();
                LogUtil.E("xxxxxxxxxxxxxxxxx");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                LogUtil.E("xxxxfffffffffx" + errorMessage);
            }

            @Override
            protected void onFinish() {

            }
        });

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
