package com.example.weather.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.R
import kotlinx.coroutines.*

class SflashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sflash_screen)

        GlobalScope.launch {
            delay(700)
            withContext(Dispatchers.Main){
                startActivity(Intent(this@SflashScreen, MainActivity2::class.java)).apply { finish() }
            }
        }
    }

}