package com.example.weather.util

import androidx.lifecycle.MutableLiveData

object ChangeUnit {
    val unitData = MutableLiveData<String>()

    fun changeUnitDataSetting(unit: String?) {
        unitData.value = unit
    }
}