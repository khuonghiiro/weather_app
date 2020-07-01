package com.example.weather.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.ViewModel.WeatherViewModel
import com.example.weather.util.ChangeUnit
import kotlinx.android.synthetic.main.layout_settings.*


class SettingsActivity : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_settings)

        weatherViewModel =
            ViewModelProvider(this).get(WeatherViewModel::class.java)
        val unit = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).getString(
            "unit",
            "metric"
        )

        when (unit) {
            "metric" -> {
                rd_doC.isChecked = true
            }
            "imperial" -> {
                rd_doF.isChecked = true
            }
        }

        rdg_CF.setOnCheckedChangeListener { _, i ->
            chondonvi(i)
        }
    }


    private fun chondonvi(checkedId: Int) {
        if (checkedId == R.id.rd_doC) {
            getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).edit()
                .putString("unit", "metric").apply()
            ChangeUnit.changeUnitDataSetting("metric")

        } else if (checkedId == R.id.rd_doF) {
            getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).edit()
                .putString("unit", "imperial").apply()
            ChangeUnit.changeUnitDataSetting("imperial")
        }
    }
}