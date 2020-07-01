
package com.example.weather.model;

import com.google.gson.annotations.SerializedName;

public class Minutely {

    @SerializedName("dt")
    private Long mDt;
    @SerializedName("precipitation")
    private Long mPrecipitation;

    public Long getDt() {
        return mDt;
    }

    public void setDt(Long dt) {
        mDt = dt;
    }

    public Long getPrecipitation() {
        return mPrecipitation;
    }

    public void setPrecipitation(Long precipitation) {
        mPrecipitation = precipitation;
    }

}
