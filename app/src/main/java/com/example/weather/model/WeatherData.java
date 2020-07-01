
package com.example.weather.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("current")
    private Current mCurrent;
    @SerializedName("daily")
    private List<Daily> mDaily;
    @SerializedName("hourly")
    private List<Hourly> mHourly;
    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lon")
    private Double mLon;
    @SerializedName("minutely")
    private List<Minutely> mMinutely;
    @SerializedName("timezone")
    private String mTimezone;
    @SerializedName("timezone_offset")
    private Long mTimezoneOffset;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public List<Daily> getDaily() {
        return mDaily;
    }

    public void setDaily(List<Daily> daily) {
        mDaily = daily;
    }

    public List<Hourly> getHourly() {
        return mHourly;
    }

    public void setHourly(List<Hourly> hourly) {
        mHourly = hourly;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLon() {
        return mLon;
    }

    public void setLon(Double lon) {
        mLon = lon;
    }

    public List<Minutely> getMinutely() {
        return mMinutely;
    }

    public void setMinutely(List<Minutely> minutely) {
        mMinutely = minutely;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public Long getTimezoneOffset() {
        return mTimezoneOffset;
    }

    public void setTimezoneOffset(Long timezoneOffset) {
        mTimezoneOffset = timezoneOffset;
    }

}
