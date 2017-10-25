package com.cdkj.h2hwtw.other.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdkj.h2hwtw.R;
import com.cdkj.h2hwtw.adapters.ProductTypeSelectAdapter;
import com.cdkj.h2hwtw.adapters.ScreeningCitySelectAdapter;
import com.cdkj.h2hwtw.adapters.ScreeningDIstrictSelectAdapter;
import com.cdkj.h2hwtw.adapters.ScreeningPriovinceSelectAdapter;
import com.cdkj.h2hwtw.model.ProductTypeModel;
import com.cdkj.h2hwtw.model.ScreeningAddressModel;
import com.cdkj.h2hwtw.model.ScreeningTypeModel;
import com.cdkj.h2hwtw.model.cityInfo.AddressInfo;
import com.cdkj.h2hwtw.model.cityInfo.City;
import com.cdkj.h2hwtw.model.cityInfo.District;
import com.cdkj.h2hwtw.model.cityInfo.Province;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于产品筛选
 * Created by cdkj on 2017/10/24.
 */
//筛选View优化
public class ProductScreeningView extends FrameLayout {

    private Context mContext;

    private onScreeningListener listener;

    private LinearLayout mAreaView;//区域View

    private LinearLayout mTabAreaView;//区域点击

    private TextView mTabTypeView;//类型点击

    private LinearLayout mTabPriceView;//价格点击

    private LinearLayout mTabScreeningView;//筛选点击

    private LinearLayout mTypeSelectView;//类型选择

    private TextView mTvArea;//区域textView
    private ImageView mImgArea;//区域textView

    private View mDissMisVIew;//黑色背景

    private AddressInfo mAddressInfo;//包含城市数据

    private int mTypeListSelectPostion = 0;//类型坐标点击下标

    //类型
    private ProductTypeSelectAdapter mTypeMenuLeftAdapter;
    private ProductTypeSelectAdapter mTypeMenuRightAdapter;


    private RecyclerView mRecyclerTypeLeft;
    private RecyclerView mRecyclerTypeRight;

    private RecyclerView mRecyclerAddressProvince;//省份
    private RecyclerView mRecyclerAddressCity;//城市
    private RecyclerView mRecyclerAddressDistrict;//地区


    //区域
    private ScreeningPriovinceSelectAdapter mPriovnceSelectAdapter;//省份选择
    private ScreeningCitySelectAdapter mPrCitySelectAdapter;//城市选择
    private ScreeningDIstrictSelectAdapter mScreeningDIstrictSelectAdapter;//地区选择

    //价格
    private ImageView mImgUpPrice;
    private ImageView mImgDownPrice;

    private boolean mPriceUpstate;//价格降序还是升序  false 降序

    private String mSelectProvince;
    private String mSelectCity;

    private String mSelectBigType;//记录用户选择的大类

    private ScreeningAddressModel mSelectAddressMode;
    private ScreeningTypeModel mSelectTypeModel;

    public ProductScreeningView(@NonNull Context context) {
        this(context, null);
    }

    public ProductScreeningView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductScreeningView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mSelectAddressMode = new ScreeningAddressModel();
        mSelectTypeModel = new ScreeningTypeModel();
        initLayout();
        initTabListener();
        initAreaListener();
        initTypeAdapter();
    }

    /**
     * 初始化区域
     */
    private void initAreaListener() {

        //省份选择
        mPriovnceSelectAdapter = new ScreeningPriovinceSelectAdapter(new ArrayList<Province>());
        mRecyclerAddressProvince.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerAddressProvince.setAdapter(mPriovnceSelectAdapter);

        mPriovnceSelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mScreeningDIstrictSelectAdapter.replaceData(new ArrayList<District>());
                mScreeningDIstrictSelectAdapter.setSelect(-5);
                mRecyclerAddressDistrict.setVisibility(INVISIBLE);

                mPriovnceSelectAdapter.setSelect(position);
                mPrCitySelectAdapter.setSelect(-1);

                Province province = mPriovnceSelectAdapter.getItem(position);
                if (province == null) return;

                if (TextUtils.equals(mContext.getString(R.string.all_country), province.getName())) {
                    closeAll();
                    if (listener != null) {
                        listener.onAddressSelect(null);
                    }
                    mTvArea.setText(R.string.all_country);
                    return;
                }

                mSelectProvince = province.getName();
                if (province.getCity() == null) {
                    return;
                }
                mPrCitySelectAdapter.replaceData(province.getCity());

            }
        });

        //城市选择
        mPrCitySelectAdapter = new ScreeningCitySelectAdapter(new ArrayList<City>());
        mRecyclerAddressCity.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerAddressCity.setAdapter(mPrCitySelectAdapter);
        mPrCitySelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mRecyclerAddressDistrict.setVisibility(VISIBLE);
                mPrCitySelectAdapter.setSelect(position);
                mScreeningDIstrictSelectAdapter.setSelect(-1);
                City city = mPrCitySelectAdapter.getItem(position);
                if (city == null) {
                    return;
                }
                mSelectCity = city.getName();
                if (city.getDistrict() == null) {
                    return;
                }
                mScreeningDIstrictSelectAdapter.replaceData(city.getDistrict());

            }
        });


        //地区选择
        mScreeningDIstrictSelectAdapter = new ScreeningDIstrictSelectAdapter(new ArrayList<District>());
        mRecyclerAddressDistrict.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerAddressDistrict.setAdapter(mScreeningDIstrictSelectAdapter);
        mScreeningDIstrictSelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mScreeningDIstrictSelectAdapter.setSelect(position);
                District dst = mScreeningDIstrictSelectAdapter.getItem(position);
                closeAll();
                if (dst == null) return;
                mTvArea.setText(dst.getName());

                mSelectAddressMode.setProvince(mSelectProvince);
                mSelectAddressMode.setCity(mSelectCity);
                mSelectAddressMode.setArea(dst.getName());

                if (listener != null) {
                    listener.onAddressSelect(mSelectAddressMode);
                }
            }
        });


    }

    public void setListener(onScreeningListener listener) {
        this.listener = listener;
    }

    private void initTypeAdapter() {

        //类型左列表
        mTypeMenuLeftAdapter = new ProductTypeSelectAdapter(new ArrayList<ProductTypeModel>(), true);
        mRecyclerTypeLeft.setAdapter(mTypeMenuLeftAdapter);
        mRecyclerTypeLeft.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mTypeMenuLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTypeMenuLeftAdapter.setFirstTouch(false);
                mTypeMenuLeftAdapter.setSelect(position);
                if (mTypeMenuLeftAdapter.getItem(position) == null) return;
                mTypeListSelectPostion = position;
                mSelectBigType = mTypeMenuLeftAdapter.getItem(position).getCode();
                if (listener != null) {
                    if (!TextUtils.equals(mTypeMenuLeftAdapter.getItem(position).getCode(), "0")) { //没有点击全部时
                        listener.onGetTypeData(mTypeMenuLeftAdapter.getItem(position).getCode(), false);
                    } else {
                        mTabTypeView.setText(mTypeMenuLeftAdapter.getItem(position).getName());
                        closeAll();
                        listener.onTypeSelect(null);
                    }
                }
            }
        });

        //类型右列表
        mTypeMenuRightAdapter = new ProductTypeSelectAdapter(new ArrayList<ProductTypeModel>(), false);
        mRecyclerTypeRight.setAdapter(mTypeMenuRightAdapter);
        mRecyclerTypeRight.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mTypeMenuRightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                closeAll();
                if (mTypeMenuRightAdapter.getItem(position) == null) return;

                if (TextUtils.equals(mTypeMenuRightAdapter.getItem(position).getName(), mContext.getString(R.string.all))) {
                    if (mTypeMenuLeftAdapter.getItem(mTypeListSelectPostion) != null) {
                        mTabTypeView.setText(mTypeMenuLeftAdapter.getItem(mTypeListSelectPostion).getName());

                        mSelectTypeModel.setCategory(mTypeMenuLeftAdapter.getItem(mTypeListSelectPostion).getCode());

                        listener.onTypeSelect(mSelectTypeModel);
                    }

                } else {
                    mTabTypeView.setText(mTypeMenuRightAdapter.getItem(position).getName());
                    mSelectTypeModel.setCategory(mSelectBigType);
                    mSelectTypeModel.setType(mTypeMenuRightAdapter.getItem(position).getCode());
                    if (listener != null) {
                        listener.onTypeSelect(mSelectTypeModel);
                    }
                }
            }
        });


    }

    /**
     * top tab点击效果控制
     */
    private void initTabListener() {

        //区域点击
        mTabAreaView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabTypeState(false);
                if (mTypeSelectView.getVisibility() == VISIBLE) {
                    mTypeSelectView.clearAnimation();
                    mTypeSelectView.setVisibility(GONE);
                }
                if (mAreaView.getVisibility() == GONE) {
                    mSelectProvince = "";
                    mSelectCity = "";
                    mSelectAddressMode = new ScreeningAddressModel();//每次点击重置数据
                    mAreaView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                    mDissMisVIew.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                    mAreaView.setVisibility(VISIBLE);
                    mDissMisVIew.setVisibility(VISIBLE);
                    setTabAreaState(true);
                } else {
                    mAreaView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
                    mDissMisVIew.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
                    mAreaView.setVisibility(GONE);
                    mDissMisVIew.setVisibility(GONE);
                    setTabAreaState(false);
                }

                if (listener != null) {
                    listener.onTabClick(0, mAreaView.getVisibility() == VISIBLE);
                }
            }
        });
        //类别点击
        mTabTypeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabAreaState(false);
                if (mAreaView.getVisibility() == VISIBLE) {
                    mAreaView.clearAnimation();
                    mAreaView.setVisibility(GONE);
                }
                if (mTypeSelectView.getVisibility() == GONE) {
                    mSelectBigType = "";
                    mSelectTypeModel = new ScreeningTypeModel();
                    mTypeSelectView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                    mDissMisVIew.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                    mTypeSelectView.setVisibility(VISIBLE);
                    mDissMisVIew.setVisibility(VISIBLE);
                    setTabTypeState(true);
                } else {
                    mTypeSelectView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
                    mDissMisVIew.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
                    mTypeSelectView.setVisibility(GONE);
                    mDissMisVIew.setVisibility(GONE);
                    setTabTypeState(false);
                }
                if (listener != null) {
                    listener.onTabClick(1, mTypeSelectView.getVisibility() == VISIBLE);
                    if (mTypeMenuLeftAdapter.getData().size() == 0 && mTypeSelectView.getVisibility() == VISIBLE) {//没有数据和界面展开是才获取数据
                        listener.onGetTypeData("0", true);
                    }
                }
            }
        });
        //价格点击
        mTabPriceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAll();

                if (listener != null) {
                    listener.onPriceSelect(mPriceUpstate);
                }
                setPrictState();
                if (listener != null) {
                    listener.onTabClick(2, true);
                }
            }
        });
        //筛选点击
        mTabScreeningView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAll();
                if (listener != null) {
                    listener.onTabClick(3, true);
                }
            }
        });

    }

    /**
     * 设置价格排序状态
     */
    private void setPrictState() {
        if (mPriceUpstate) {
            mImgUpPrice.setImageResource(R.drawable.up_blue);
            mImgDownPrice.setImageResource(R.drawable.down_gray);
        } else {
            mImgDownPrice.setImageResource(R.drawable.down_blue);
            mImgUpPrice.setImageResource(R.drawable.up_gray);
        }
        mPriceUpstate = !mPriceUpstate;
    }

    /**
     * 设置区域点击效果
     *
     * @param isOpen
     */
    private void setTabAreaState(boolean isOpen) {
        if (isOpen) {
            mImgArea.setImageResource(R.drawable.down_blue);
            mTvArea.setTextColor(ContextCompat.getColor(mContext, R.color.screening_text_selet));
        } else {
            mImgArea.setImageResource(R.drawable.down_gray);
            mTvArea.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
        }
    }

    /**
     * 设置类型点击效果
     *
     * @param isOpen
     */
    private void setTabTypeState(boolean isOpen) {
        if (isOpen) {
            mTabTypeView.setTextColor(ContextCompat.getColor(mContext, R.color.screening_text_selet));
        } else {
            mTabTypeView.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_app));
        }
    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_screening_view, this, true);

        mTabAreaView = findViewById(R.id.lin_tab_1);
        mTabTypeView = findViewById(R.id.lin_tab_2);
        mTabPriceView = findViewById(R.id.lin_tab_3);
        mTabScreeningView = findViewById(R.id.lin_tab_4);

        mTypeSelectView = findViewById(R.id.lin_type_select);
        mAreaView = findViewById(R.id.lin_area);


        mRecyclerTypeLeft = findViewById(R.id.recycler_type_left);
        mRecyclerTypeRight = findViewById(R.id.recycler_type_right);


        mRecyclerAddressProvince = findViewById(R.id.recycler_address_province);
        mRecyclerAddressCity = findViewById(R.id.recycler_address_city);
        mRecyclerAddressDistrict = findViewById(R.id.recycler_address_district);

        mTvArea = findViewById(R.id.tv_tab_1);
        mImgArea = findViewById(R.id.img_tab_1);

        mImgDownPrice = findViewById(R.id.img_down_proce);
        mImgUpPrice = findViewById(R.id.img_up_price);

        mDissMisVIew = findViewById(R.id.screning_bg_view);

        mDissMisVIew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAll();
            }
        });

        LinearLayout lin_area_recycler = findViewById(R.id.lin_area_recycler);

        lin_area_recycler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    /**
     * 关闭所有View
     */
    public void closeAll() {
        setTabAreaState(false);
        setTabTypeState(false);
        mAreaView.clearAnimation();
        mTypeSelectView.clearAnimation();
        mDissMisVIew.clearAnimation();

        mAreaView.setVisibility(GONE);
        mTypeSelectView.setVisibility(GONE);
        mDissMisVIew.setVisibility(GONE);
    }

    public boolean isOpen() {
        return mDissMisVIew.getVisibility() == VISIBLE;
    }


    public void clear() {
        mContext = null;
    }

    public void setTypeData(List<ProductTypeModel> data, boolean isLeft, String parentCode) {
        if (isLeft) {
            ProductTypeModel allModel = new ProductTypeModel();
            allModel.setName(mContext.getString(R.string.all));
            allModel.setCode("0");
            data.add(0, allModel);
            mTypeMenuLeftAdapter.replaceData(data);
        } else {
            ProductTypeModel allModel = new ProductTypeModel();
            allModel.setName(mContext.getString(R.string.all));
            allModel.setCode(parentCode);
            data.add(0, allModel);
            mTypeMenuRightAdapter.replaceData(data);
        }

    }

    public AddressInfo getmAddressInfo() {
        return mAddressInfo;
    }

    /**
     * 设置地址数据
     *
     * @param mAddressInfo
     */
    public void setmAddressInfo(AddressInfo mAddressInfo) {
        this.mAddressInfo = mAddressInfo;
        if (mAddressInfo == null) return;
        mPriovnceSelectAdapter.setSelect(-1);
        List<Province> province = new ArrayList<>();
        Province province1 = new Province();
        province1.setName(mContext.getString(R.string.all_country));
        province.add(province1);
        if (mAddressInfo != null) {
            province.addAll(mAddressInfo.getProvince());
        }
        mPriovnceSelectAdapter.replaceData(province);
    }

    public void setTypeSelectName(String name) {
        if (TextUtils.isEmpty(name)) return;
        mTabTypeView.setText(name);
    }

    /**
     * 筛选监听
     */
    public interface onScreeningListener {
        // tab按钮点击
        void onTabClick(int position, boolean isOpen); //position 点击下标从左 0 开始 ， 点击的tab相应的界面是否打开

        void onGetTypeData(String partentCode, boolean isLeft); //点击类型时获取数据

        void onAddressSelect(ScreeningAddressModel address);//地址筛选完成

        void onTypeSelect(ScreeningTypeModel typeModel);//类型筛选完成

        void onPriceSelect(boolean isUpPrice);//价格筛选完成

    }


}
