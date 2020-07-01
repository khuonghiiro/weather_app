package com.example.weather.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

class Daily {
    @SerializedName("clouds")
    var clouds: Long? = null

    @SerializedName("dew_point")
    var dewPoint: Double? = null

    @SerializedName("dt")
    var dt: Long? = null

    @SerializedName("feels_like")
    var feelsLike: FeelsLike? = null

    @SerializedName("humidity")
    var humidity: Long? = null

    @SerializedName("pressure")
    var pressure: Long? = null

    @SerializedName("rain")
    var rain: Double? = null

    @SerializedName("sunrise")
    var sunrise: Long? = null

    @SerializedName("sunset")
    var sunset: Long? = null

    @SerializedName("temp")
    var temp: Temp? = null

    @SerializedName("uvi")
    var uvi: Double? = null

    @SerializedName("weather")
    var weather: List<Weather>? = null

    @SerializedName("wind_deg")
    var windDeg: Long? = null

    @SerializedName("wind_speed")
    var windSpeed: Double? = null


}