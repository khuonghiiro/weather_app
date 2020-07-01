package com.example.weather.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.retrofit.ApiClient;
import com.example.weather.retrofit.ApiInterface;
import com.example.weather.model.WeatherData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<WeatherData> weatherDataMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<WeatherData> getWeatherDataMutableLiveData() {
        return weatherDataMutableLiveData;
    }

    public void getWeatherDataByLatLon(Double lat, Double lon, String unit) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getWeatherDataByLatLon(lat, lon, "6605db93a9d196e65eba3f4e2ea0a092", unit, "vi").enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                weatherDataMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
    }
}
