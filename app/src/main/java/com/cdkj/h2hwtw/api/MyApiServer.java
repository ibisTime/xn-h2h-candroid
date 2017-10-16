package com.cdkj.h2hwtw.api;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.h2hwtw.model.AddressModel;
import com.cdkj.h2hwtw.model.AmountModel;
import com.cdkj.h2hwtw.model.BillListMode;
import com.cdkj.h2hwtw.model.IntroductionInfoList;
import com.cdkj.h2hwtw.model.ProductListModel;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.model.TotalAmountModel;
import com.cdkj.h2hwtw.model.UserInfoModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 李先俊 on 2017/10/13.
 */

public interface MyApiServer {

    /**
     * 获取用户信息详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserInfoModel>> getUserInfoDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取地址列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<AddressModel>> getAddress(@Field("code") String code, @Field("json") String json);

    /**
     * 添加收货地址
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CodeModel>> AddAddress(@Field("code") String code, @Field("json") String json);


    /**
     * 设置默认地址
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<Boolean>> setDefultAddress(@Field("code") String code, @Field("json") String json);

    /**
     * 获取 流水
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BillListMode>> getBillList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取账户余额
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<AmountModel>> getAmount(@Field("code") String code, @Field("json") String json);

    /**
     * 获取账户余额
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<TotalAmountModel>> getTotaleAmount(@Field("code") String code, @Field("json") String json);


    /**
     * 根据ckey查询系统参数
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<IntroductionInfoList>> getKeySystemListInfo(@Field("code") String code, @Field("json") String json);

    /**
     * 获取产品分类
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<ProductTypeModel>> getProductType(@Field("code") String code, @Field("json") String json);
    /**
     * 获取产品列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ProductListModel>> getProductList(@Field("code") String code, @Field("json") String json);

}
