package com.example.weather.retrofit;

import com.example.weather.model.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("onecall")
    Call<WeatherData> getWeatherDataByLatLon(@Query("lat") Double lat,
                                             @Query("lon") Double lon,
                                             @Query("appid") String token,
                                             @Query("units") String unit,@Query("lang") String lang);
}
