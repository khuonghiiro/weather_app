package com.example.weather.util

import android.content.Context
import android.util.TypedValue

object DimenConverter {
    fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }

    fun spToPx(context: Context, sp: Float): Float {
        return sp / context.resources.displayMetrics.scaledDensity
    }
}
