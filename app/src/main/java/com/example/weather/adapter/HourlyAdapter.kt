package com.example.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.model.Hourly
import kotlinx.android.synthetic.main.item_hourly.view.*
import java.util.*
import kotlin.math.roundToInt

class HourlyAdapter(private val context: Context, private val listHourly: List<Hourly>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temperature: TextView = itemView.temp_hourly
        val icWeather: ImageView = itemView.icon_weather_hourly
        val timestamp: TextView = itemView.timestamp_hourly
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listHourly.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourly = listHourly[position]

        val unit = context.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        ).getString("unit", "metric")!!
        when (unit){
            "metric" -> {
                holder.temperature.text = context.getString(R.string.nhiet_do_C, hourly.temp.roundToInt().toString())
            }
            "imperial" -> {
                holder.temperature.text = context.getString(R.string.nhiet_do_F, hourly.temp.roundToInt().toString())
            }
        }

        Glide
            .with(context)
            .load("https://openweathermap.org/img/wn/${hourly.weather[0].icon}@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.icWeather)

        Calendar.getInstance().apply {
            time = Date(hourly.dt * 1000)
            when {
                get(Calendar.MINUTE) < 10 -> {
                    holder.timestamp.text =
                        "${get(Calendar.HOUR_OF_DAY)}:0${get(Calendar.MINUTE)}"

                }
                else -> {
                    holder.timestamp.text = "${get(Calendar.HOUR_OF_DAY)}:${get(Calendar.MINUTE)}"
                }
            }
        }
    }
}
