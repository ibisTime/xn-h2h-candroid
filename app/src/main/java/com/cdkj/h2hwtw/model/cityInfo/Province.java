package com.cdkj.h2hwtw.model.cityInfo;

import java.util.List;

/**
 * Created by 李先俊 on 2017/10/25.
 */

public class Province {

    private String name;

    private List<City> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}
