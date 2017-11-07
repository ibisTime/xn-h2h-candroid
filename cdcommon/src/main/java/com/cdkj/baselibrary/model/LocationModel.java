package com.cdkj.baselibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**用于定位
 * Created by cdkj on 2017/11/7.
 */

public class LocationModel implements Parcelable {

    private String city;

    private String province;

    private String district;

    private String latitude;

    private String longitude;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.province);
        dest.writeString(this.district);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    public LocationModel() {
    }

    protected LocationModel(Parcel in) {
        this.city = in.readString();
        this.province = in.readString();
        this.district = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Parcelable.Creator<LocationModel> CREATOR = new Parcelable.Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel source) {
            return new LocationModel(source);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };
}
