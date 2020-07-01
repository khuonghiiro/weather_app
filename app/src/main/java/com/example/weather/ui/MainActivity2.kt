package com.example.weather.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.ViewModel.WeatherViewModel
import com.example.weather.adapter.DailyAdapter
import com.example.weather.adapter.HourlyAdapter
import com.example.weather.adapter.SearchAdapter
import com.example.weather.model.Daily
import com.example.weather.model.Hourly
import com.example.weather.model.WeatherData
import com.example.weather.util.ChangeUnit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.LanguageCode
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.search.*
import kotlinx.android.synthetic.main.layout_main.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class MainActivity2 : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    private var listHourly: MutableList<Hourly> = ArrayList()
    private lateinit var hourlyAdapter: HourlyAdapter
    private val listDaily: MutableList<Daily> = ArrayList()
    private lateinit var dailyAdapter: DailyAdapter
    private val listSearch: MutableList<Place> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter

    private lateinit var unit: String
    lateinit var edSearch: EditText
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private lateinit var imgSearch: ImageView
    private lateinit var searchEngine: SearchEngine

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        weatherViewModel =
            ViewModelProvider(this).get(WeatherViewModel::class.java)

        searchRecyclerView(this)

        unit = getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        ).getString("unit", "metric")!!

        //onclick cai dat
        img_setting.setOnClickListener {
//            setContentView(R.layout.layout_settings)
            val intentMain = Intent(this@MainActivity2, SettingsActivity::class.java)
            startActivity(intentMain)
        }
        //end onclick cai dat
        imgSearch = findViewById(R.id.imgSearch)
        edSearch = findViewById(R.id.edSearch)
        edSearch.visibility = View.INVISIBLE//ẩn nut tim kiếm
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity2,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            location
        }
        generateHourlyRecyclerView(this)
        try {
            searchEngine = SearchEngine()
        } catch (e: InstantiationErrorException) {
            throw RuntimeException("Initialization of SearchEngine failed: " + e.error.name)
        }

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrBlank()) {
                    val centerGeoCoordinates = GeoCoordinates(lat, lng)
                    val maxItems = 5
                    val searchOptions = SearchOptions(LanguageCode.EN_US, maxItems)

                    searchEngine.suggest(
                        TextQuery(
                            edSearch.text.toString().trim(),  // User typed "p".
                            centerGeoCoordinates
                        ),
                        searchOptions,
                        object : SuggestCallback {
                            override fun onSuggestCompleted(
                                searchError: SearchError?,
                                list: MutableList<Suggestion>?
                            ) {
                                if (searchError != null) {
                                    Log.d("LOG_TAG", "Autosuggest Error: " + searchError.name)
                                    return
                                }

                                if (list != null) {
                                    Log.d("LOG_TAG", "Autosuggest results: " + list.size)

                                }

                                if (list != null) {
                                    listSearch.clear()
                                    recycler_search.visibility = View.VISIBLE
                                    for (autosuggestResult in list) {
                                        autosuggestResult.place?.let {
                                            listSearch.add(
                                                it
                                            )
                                        }
                                        searchAdapter.notifyDataSetChanged()
                                    }

                                }
                                Log.d("search:", listSearch.toString() + "")
                            }
                        }
                    )
                } else {
                    listSearch.clear()
                    searchAdapter.notifyDataSetChanged()
                    recycler_search.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })



        imgSearch.setOnClickListener {
            if (edSearch.visibility == View.VISIBLE) {
                edSearch.visibility = View.INVISIBLE
                if (!edSearch.text.isNullOrBlank())
                    getLocationFromAddress(edSearch.text.toString().trim())
            } else {
                edSearch.visibility = View.VISIBLE
            }
        }

        ChangeUnit.unitData.observe(this, Observer {
            unit = it
            weatherViewModel.getWeatherDataByLatLon(
                lat,
                lng,
                it
            )
        })
    }

    // "Địa điểm:" + " " +    textView6.setText(addresses.get(0).getAdminArea());
    private val location: Unit
        get() {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity2, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 44
                )
            }
            fusedLocationProviderClient!!.lastLocation
                .addOnCompleteListener { task ->
                    val location = task.result
                    val geocoder = Geocoder(this@MainActivity2, Locale.getDefault())
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                        weatherViewModel.getWeatherDataByLatLon(
                            lat,
                            lng,
                            unit
                        )
                        weatherViewModel.weatherDataMutableLiveData.observe(
                            this@MainActivity2,
                            Observer { weatherData: WeatherData ->
                                try {
                                    val addresses =
                                        geocoder.getFromLocation(
                                            lat,
                                            lng,
                                            1
                                        )
                                    tv_diadiem.text =
                                        addresses[0].getAddressLine(0) //"Địa điểm:" + " " +
                                    tv_quocgia.text =getString(R.string.quoc_gia,addresses[0].countryName)

                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                when (unit) {
                                    "metric" -> {
                                        tv_temp.text = getString(
                                            R.string.nhiet_do_C,
                                            weatherData.current.temp.roundToInt().toString()
                                        )
                                        tv_camnhan.text =getString(
                                            R.string.cam_thay_doC,
                                            weatherData.current.feelsLike.roundToInt().toString()
                                        )
                                    }
                                    "imperial" -> {
                                        tv_temp.text = getString(
                                            R.string.nhiet_do_F,
                                            weatherData.current.temp.roundToInt().toString()
                                        )
                                        tv_camnhan.text =getString(
                                            R.string.cam_thay_doF,
                                            weatherData.current.feelsLike.roundToInt().toString()
                                        )
                                    }
                                }
                                tv_muigio.text = getString(R.string.mui_gio,weatherData.timezone )

                                tv_doam.text = getString(R.string.do_am,weatherData.current.humidity.toString() )

                                id_mieuta.text = weatherData.current.weather[0].description

                                val now = Calendar.getInstance()
                                now.time = Date(weatherData.current.dt * 1000)
                                val hour = now[Calendar.HOUR_OF_DAY]
                                val minute = now[Calendar.MINUTE]
                                tv_capnhat.text = getString(R.string.time_cap_nhat,hour.toString(),minute.toString())
                                Glide.with(this@MainActivity2).load(
                                    "https://openweathermap.org/img/wn/" + weatherData.current
                                        .weather[0].icon + "@2x.png"
                                )
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .into(img_weather)

                                listHourly.clear()
                                listHourly.addAll(weatherData.hourly)
                                hourlyAdapter.notifyDataSetChanged()

                                listDaily.clear()
                                listDaily.addAll(weatherData.daily)
                                listDaily.removeAt(0)
                                generateDailyRecyclerView(this, listDaily)
                                dailyAdapter.notifyDataSetChanged()
                            }
                        )
                    }
                }
        }


    private fun generateHourlyRecyclerView(context: Context) {
        hourlyAdapter = HourlyAdapter(context, listHourly)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_hourly.layoutManager = layoutManager
        recycler_view_hourly.itemAnimator = DefaultItemAnimator()
        recycler_view_hourly.adapter = hourlyAdapter
    }

    private fun searchRecyclerView(context: Context) {
        searchAdapter = SearchAdapter(context, listSearch)
        searchAdapter.setOnItemClickListener(object : SearchAdapter.ItemClickListener {
            override fun onClick(position: Int) {
                val place = listSearch[position]
                lat = place.coordinates.latitude
                lng = place.coordinates.longitude
                weatherViewModel.getWeatherDataByLatLon(
                    place.coordinates.latitude,
                    place.coordinates.longitude,
                    unit
                )
                listSearch.clear()
                searchAdapter.notifyDataSetChanged()
                recycler_search.visibility = View.GONE
            }
        })
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_search.layoutManager = layoutManager
        recycler_search.itemAnimator = DefaultItemAnimator()
        recycler_search.adapter = searchAdapter
    }

    private fun generateDailyRecyclerView(context: Context, listDaily: List<Daily>) {
        val listMaxTemp: MutableList<Double> = ArrayList()
        val listMinTemp: MutableList<Double> = ArrayList()

        for (day in listDaily) {
            listMaxTemp.add(day.temp!!.max)
            listMinTemp.add(day.temp!!.min)
        }

        val maxTemp = Collections.max(listMaxTemp).toInt()
        val minTemp = Collections.min(listMinTemp).toInt()

        dailyAdapter = DailyAdapter(context, listDaily, maxTemp, minTemp)
        val layoutManager = GridLayoutManager(context, listDaily.size)
        recycler_view_daily.layoutManager = layoutManager
        recycler_view_daily.itemAnimator = DefaultItemAnimator()
        recycler_view_daily.adapter = dailyAdapter
    }


    private fun getLocationFromAddress(strAddress: String) {
        val geocoder = Geocoder(this@MainActivity2, Locale.getDefault())

        val address = geocoder.getFromLocationName(strAddress, 5)
        if (address.size > 0) {
            val location: Address? = address[0]
            location?.let {
                Log.d("kiem tra 3", listOf(location).toString() + "")
                Log.d("kiem tra 4", location.featureName.toString() + "")
                weatherViewModel.getWeatherDataByLatLon(
                    location.latitude,
                    location.longitude,
                    unit
                )
                val addresses2 = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                Log.d("kiem tra", listOf(addresses2[0]).toString() + "")
                Log.d("kiem tra 2", addresses2[0].adminArea + "")
                
                tv_diadiem.text = addresses2[0].getAddressLine(0) //"Địa điểm:" + " " +
                tv_quocgia.text =getString(R.string.quoc_gia,addresses2[0].countryName)

                weatherViewModel.weatherDataMutableLiveData.observe(
                    this@MainActivity2,
                    Observer { weatherData: WeatherData ->
                        when (unit) {
                            "metric" -> {
                                tv_temp.text = getString(
                                    R.string.nhiet_do_C,
                                    weatherData.current.temp.roundToInt().toString()
                                )
                                tv_camnhan.text =getString(
                                        R.string.cam_thay_doC,
                                        weatherData.current.feelsLike.roundToInt().toString()
                                    )
                            }
                            "imperial" -> {
                                tv_temp.text = getString(
                                    R.string.nhiet_do_F,
                                    weatherData.current.temp.roundToInt().toString()
                                )
                                tv_camnhan.text = getString(
                                        R.string.cam_thay_doF,
                                        weatherData.current.feelsLike.roundToInt().toString()
                                    )
                            }
                        }

                        tv_muigio.text = getString(R.string.mui_gio,weatherData.timezone )

                        tv_doam.text = getString(R.string.do_am,weatherData.current.humidity.toString() )

                        id_mieuta.text = weatherData.current.weather[0].description

                        val now = Calendar.getInstance()
                        now.time = Date(weatherData.current.dt * 1000)
                        val hour = now[Calendar.HOUR_OF_DAY]
                        val minute = now[Calendar.MINUTE]
                        tv_capnhat.text = getString(R.string.time_cap_nhat,hour.toString(),minute.toString())

                        Glide.with(this@MainActivity2).load(
                            "https://openweathermap.org/img/wn/" + weatherData.current
                                .weather[0].icon + "@2x.png"
                        )
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(img_weather)

                        listHourly.clear()
                        listHourly.addAll(weatherData.hourly)
                        hourlyAdapter.notifyDataSetChanged()

                        listDaily.clear()
                        listDaily.addAll(weatherData.daily)
                        listDaily.removeAt(0)
                        generateDailyRecyclerView(this, listDaily)
                        dailyAdapter.notifyDataSetChanged()
                    })
                Toast.makeText(this@MainActivity2, "Tìm thành công", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                this@MainActivity2,
                "Không tìm thấy thông tin thành phố",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}