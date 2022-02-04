package com.dvt.weatherapp.activities.home.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dvt.weatherapp.R
import com.dvt.weatherapp.activities.home.MainActivity
import com.dvt.weatherapp.adapters.WeatherItemAdapter
import com.dvt.weatherapp.databinding.FragmentHomeBinding
import com.dvt.weatherapp.extentions.showErrorMessage
import com.dvt.weatherapp.extentions.toast
import com.dvt.weatherapp.models.ForecastResponse
import com.dvt.weatherapp.models.WeatherItem
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.viewmodels.LocalWeatherViewModel
import com.dvt.weatherapp.viewmodels.WeatherResponseViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val weatherResponseViewModel: WeatherResponseViewModel by viewModels()
    private val localWeatherViewModel: LocalWeatherViewModel by activityViewModels()

    private lateinit var currentWeather: WeatherResponse
    private lateinit var forecastResponse: ForecastResponse
    private lateinit var weatherItemAdapter: WeatherItemAdapter
    private lateinit var availableForecast: List<WeatherItem>
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        binding.cpLoadCurrentWeather.visibility = View.VISIBLE

        binding.fab.visibility = View.GONE

        getWeatherDataWithLocationPermission()

        return root
    }

    private fun favoriteWeather(weatherResponse: WeatherResponse) {
        weatherResponse?.let {
            localWeatherViewModel.insert(it)
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
            context?.toast("Added to favorites")
        }
    }

    private fun removeCurrentWeatherFromFavorities(weatherResponse: WeatherResponse) {
        weatherResponse?.let {
            localWeatherViewModel.delete(it)
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            context?.toast("removed from favorites")
        }

    }

    private fun setupRecyclerView(items: List<WeatherItem>) {
        weatherItemAdapter = WeatherItemAdapter(context!!, items)
        binding.recyclerView.adapter = weatherItemAdapter
    }

    private fun fetchCurrentWeatherInformation(lat: Double, lon: Double) {
        weatherResponseViewModel.getWeatherResponse(lat, lon)
            .observe(viewLifecycleOwner) { response ->
                binding.cpLoadCurrentWeather.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.i(TAG, "weather data loaded from API $response")
                    if (response.body()?.cod == 200) {
                        response.body()?.let {
                            binding.fab.visibility = View.VISIBLE
                            currentWeather = response.body()!!
                            binding.tvWeatherDescription.text =
                                currentWeather.weather?.get(0)?.main?.uppercase(Locale.getDefault())
                            binding.tvWeatherTemp.text =
                                "%.1f".format(currentWeather.main?.temp) + "\u00B0"
                            binding.tvMin.text =
                                "%.1f".format(currentWeather.main?.temp_min) + "\u00B0"
                            binding.tvCurrent.text =
                                "%.1f".format(currentWeather.main?.temp) + "\u00B0"
                            binding.tvMax.text =
                                "%.1f".format(currentWeather.main?.temp_max) + "\u00B0"
                            setBackgroundDisplay(currentWeather)

                            currentWeather.weather?.get(0)?.id?.let {
                                localWeatherViewModel.exists(it)?.observe(this, { boolean ->

                                    //TODO: still to be fixed, figure out best way to deal with weather ids
                                    if (boolean) {
                                        binding.fab.setOnClickListener {
                                            removeCurrentWeatherFromFavorities(
                                                currentWeather
                                            )
                                        }
                                        binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
                                    } else {
                                        binding.fab.setOnClickListener {
                                            favoriteWeather(
                                                currentWeather
                                            )
                                        }
                                        binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                                    }
                                })
                            }
                        }
                    } else {
                        context?.toast("Weather information not found for this location")
                    }
                } else {
                    Log.i(TAG, "error response from API $response")
                    context?.showErrorMessage(response.errorBody()!!)
                }
            }
    }

    private fun setBackgroundDisplay(currentWeather: WeatherResponse) {

        when (currentWeather.weather?.get(0)?.main) {
            "Clouds" -> {
                Glide.with(context!!).load(R.drawable.forest_cloudy)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.weather_cloudy
                    )
                )

                binding.tvWeatherDescription.text = "CLOUDY"
            }
            "Rain" -> {
                Glide.with(context!!).load(R.drawable.forest_rainy)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.weather_rainy
                    )
                )

                binding.tvWeatherDescription.text = "RAINY"
            }
            "Clear" -> {
                Glide.with(context!!).load(R.drawable.forest_sunny)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.weather_sunny
                    )
                )

                binding.tvWeatherDescription.text = "SUNNY"
            }
            else -> {
                Glide.with(context!!).load(R.drawable.forest_sunny)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.weather_sunny
                    )
                )
            }
        }
    }

    private fun fetchWeatherForecastInformation(lat: Double, lon: Double) {
        weatherResponseViewModel.getWeatherForecastResponse(lat, lon)
            .observe(viewLifecycleOwner) { response ->

                if (response.isSuccessful) {
                    Log.i(TAG, "forecast data loaded from API $response")
                    if (response.body()?.cod == 200) {
                        response.body()?.let {
                            forecastResponse = response.body()!!
                            availableForecast = forecastResponse.list!!
                        }
                        if (availableForecast.isNotEmpty()) setupRecyclerView(
                            filterList(
                                availableForecast
                            )
                        )
                        else context?.toast("No Items Found")
                    } else {
                        context?.toast("Weather information not found for this location")

                    }
                } else {
                    Log.i(TAG, "error response from API $response")
                    context?.showErrorMessage(response.errorBody()!!)
                }
            }
    }

    private fun filterList(availableForecast: List<WeatherItem>): List<WeatherItem> {
        val currentWeatherItem = availableForecast[0]
        val subSequence =
            currentWeatherItem.dt_txt?.subSequence(11, currentWeatherItem.dt_txt!!.length)
        return availableForecast.filter { it.dt_txt!!?.contains(subSequence.toString()) }
    }

    private fun getWeatherDataWithLocationPermission() {

        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        getLastKnowLocation()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) { //TODO: Show rational
                    }
                }).check()
        }else{
            getLastKnowLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            latitude = location.latitude
            longitude = location.longitude
            fetchCurrentWeatherInformation(latitude, longitude)
            fetchWeatherForecastInformation(latitude, longitude)
        }.addOnFailureListener {
            context?.toast("Failed on getting current location")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    context?.toast("You need to grant permission to access location")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}