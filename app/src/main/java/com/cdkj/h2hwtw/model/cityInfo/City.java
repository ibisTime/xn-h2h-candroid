package com.cdkj.h2hwtw.model.cityInfo;

import java.util.List;

/**
 * Created by 李先俊 on 2017/10/25.
 */

public class City {

    private String name;

    private List<District> district;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistrict() {
        return district;
    }

    public void setDistrict(List<District> district) {
        this.district = district;
    }
}
