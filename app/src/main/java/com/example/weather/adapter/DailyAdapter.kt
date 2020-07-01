package com.example.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.model.Daily
import com.example.weather.util.DimenConverter
import kotlinx.android.synthetic.main.item_daily_temp_graph.view.*
import java.util.*

class DailyAdapter(
    private val context: Context,
    private val listDaily: List<Daily>,
    private val maxTemp: Int,
    private val minTemp: Int
) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHighTemp: TextView = itemView.high_temp
        val tvLowTemp: TextView = itemView.low_temp
        val imgWeather: ImageView = itemView.ic_weather
        val tvDayOfWeek: TextView = itemView.day_of_week
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_daily_temp_graph, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listDaily.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daily = listDaily[position]

        val rangeTemp = (maxTemp - minTemp)
        val khoangcach = if (rangeTemp > 10) {
            7 / (rangeTemp / 10)
        } else {
            14
        }
        val currentMaxTemp = daily.temp?.max?.toInt()
        val currentMinTemp = daily.temp?.min?.toInt()
        val unit = context.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        ).getString("unit", "metric")!!
        when (unit) {
            "metric" -> {
                holder.tvHighTemp.text =
                    context.getString(R.string.nhiet_do_C, currentMaxTemp.toString())
                holder.tvLowTemp.text =
                    context.getString(R.string.nhiet_do_C, currentMinTemp.toString())
            }
            "imperial" -> {
                holder.tvHighTemp.text =
                    context.getString(R.string.nhiet_do_F, currentMaxTemp.toString())
                holder.tvLowTemp.text =
                    context.getString(R.string.nhiet_do_F, currentMinTemp.toString())
            }
        }

        if (maxTemp - currentMaxTemp!! > 0) {
            val layoutParams = holder.tvHighTemp.layoutParams as ConstraintLayout.LayoutParams

            layoutParams.topMargin =
                DimenConverter.dpToPx(context, khoangcach * (maxTemp - currentMaxTemp).toFloat())

            holder.tvHighTemp.layoutParams = layoutParams
        }

        if (currentMinTemp!! - minTemp > 0) {
            val layoutParams = holder.tvLowTemp.layoutParams as ConstraintLayout.LayoutParams

            layoutParams.bottomMargin =
                DimenConverter.dpToPx(context, khoangcach * (currentMinTemp - minTemp).toFloat())

            holder.tvLowTemp.layoutParams = layoutParams
        }

        Calendar.getInstance().apply {
            time = Date(daily.dt!! * 1000)

            holder.tvDayOfWeek.text =
                context.resources.getStringArray(R.array.day_of_week)[get(Calendar.DAY_OF_WEEK) - 1]
        }

        Glide
            .with(context)
            .load("https://openweathermap.org/img/wn/${daily.weather!![0].icon}@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.imgWeather)
    }
}
