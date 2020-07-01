
package com.example.weather.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Hourly {

    @SerializedName("clouds")
    private Long mClouds;
    @SerializedName("dew_point")
    private Double mDewPoint;
    @SerializedName("dt")
    private Long mDt;
    @SerializedName("feels_like")
    private Double mFeelsLike;
    @SerializedName("humidity")
    private Long mHumidity;
    @SerializedName("pressure")
    private Long mPressure;
    @SerializedName("temp")
    private Double mTemp;
    @SerializedName("weather")
    private List<Weather> mWeather;
    @SerializedName("wind_deg")
    private Long mWindDeg;
    @SerializedName("wind_speed")
    private Double mWindSpeed;

    public Long getClouds() {
        return mClouds;
    }

    public void setClouds(Long clouds) {
        mClouds = clouds;
    }

    public Double getDewPoint() {
        return mDewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        mDewPoint = dewPoint;
    }

    public Long getDt() {
        return mDt;
    }

    public void setDt(Long dt) {
        mDt = dt;
    }

    public Double getFeelsLike() {
        return mFeelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        mFeelsLike = feelsLike;
    }

    public Long getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Long humidity) {
        mHumidity = humidity;
    }

    public Long getPressure() {
        return mPressure;
    }

    public void setPressure(Long pressure) {
        mPressure = pressure;
    }

    public Double getTemp() {
        return mTemp;
    }

    public void setTemp(Double temp) {
        mTemp = temp;
    }

    public List<Weather> getWeather() {
        return mWeather;
    }

    public void setWeather(List<Weather> weather) {
        mWeather = weather;
    }

    public Long getWindDeg() {
        return mWindDeg;
    }

    public void setWindDeg(Long windDeg) {
        mWindDeg = windDeg;
    }

    public Double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        mWindSpeed = windSpeed;
    }

}
