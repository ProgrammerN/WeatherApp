package com.dvt.weatherapp.activities.home.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dvt.weatherapp.R
import com.dvt.weatherapp.adapters.WeatherItemAdapter
import com.dvt.weatherapp.component.DaggerAppComponent
import com.dvt.weatherapp.databinding.FragmentHomeBinding
import com.dvt.weatherapp.extentions.showErrorMessage
import com.dvt.weatherapp.extentions.toast
import com.dvt.weatherapp.models.ForecastResponse
import com.dvt.weatherapp.models.WeatherItem
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.util.Utils
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
import timber.log.Timber
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val weatherResponseViewModel: WeatherResponseViewModel by viewModels()
    private lateinit var localWeatherViewModel: LocalWeatherViewModel
    private lateinit var currentWeather: WeatherResponse
    private lateinit var forecastResponse: ForecastResponse
    private lateinit var weatherItemAdapter: WeatherItemAdapter
    private lateinit var availableForecast: List<WeatherItem>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.cpLoadCurrentWeather.visibility = View.VISIBLE

        binding.fab.visibility = View.GONE

        var appComponent = DaggerAppComponent.create()

        localWeatherViewModel = appComponent.localWeatherViewModel()

        getWeatherDataWithLocationPermission()

        return root
    }

    private fun favoriteWeather(weatherResponse: WeatherResponse) {
        weatherResponse.let {
            localWeatherViewModel.insert(it)
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
            context?.toast("Added to favorites")
        }
    }

    private fun removeCurrentWeatherFromFavorites(weatherResponse: WeatherResponse) {
        weatherResponse.let {
            localWeatherViewModel.delete(it)
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            context?.toast("removed from favorites")
        }

    }

    private fun setupRecyclerView(items: List<WeatherItem>) {
        weatherItemAdapter = WeatherItemAdapter(requireContext(), items)
        binding.recyclerView.adapter = weatherItemAdapter
    }

    private fun fetchCurrentWeatherInformation(lat: Double, lon: Double) {
        weatherResponseViewModel.getWeatherResponse(lat, lon)
            .observe(viewLifecycleOwner) { response ->
                binding.cpLoadCurrentWeather.visibility = View.GONE
                if (response.isSuccessful) {
                    Timber.i("weather data loaded from API $response")
                    if (response.body()?.cod == 200) {
                        response.body()?.let {
                            binding.fab.visibility = View.VISIBLE
                            currentWeather = response.body()!!
                            binding.tvWeatherDescription.text =
                                currentWeather.weather?.get(0)?.main?.uppercase(Locale.getDefault())

                            binding.tvWeatherTemp.text =
                                Utils.formatTemperature(currentWeather.main?.temp!!)
                            binding.tvMin.text =
                                Utils.formatTemperature(currentWeather.main?.temp_min!!)
                            binding.tvCurrent.text =
                                Utils.formatTemperature(currentWeather.main?.temp!!)
                            binding.tvMax.text =
                                Utils.formatTemperature(currentWeather.main?.temp_max!!)

                            setBackgroundDisplay(currentWeather.weather?.get(0)?.main.toString())

                            currentWeather.id?.let {
                                localWeatherViewModel.exists(it)
                                    ?.observe(viewLifecycleOwner) { boolean ->
                                        if (boolean) {
                                            binding.fab.setOnClickListener {
                                                removeCurrentWeatherFromFavorites(
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
                                    }
                            }
                        }
                    } else {
                        context?.toast("Weather information not found for this location")
                    }
                } else {
                    Timber.i("error response from API $response")
                    context?.showErrorMessage(response.errorBody()!!)
                }
            }
    }


    @VisibleForTesting
    private fun setBackgroundDisplay(weather: String) {
        when (weather) {
            "Clouds" -> {
                Glide.with(requireContext()).load(R.drawable.forest_cloudy)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.weather_cloudy
                    )
                )
                binding.tvWeatherDescription.text = "CLOUDY"
            }
            "Rain" -> {
                Glide.with(requireContext()).load(R.drawable.forest_rainy)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.weather_rainy
                    )
                )

                binding.tvWeatherDescription.text = "RAINY"
            }
            "Clear" -> {
                Glide.with(requireContext()).load(R.drawable.forest_sunny)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.weather_sunny
                    )
                )

                binding.tvWeatherDescription.text = "SUNNY"
            }
            else -> {
                Glide.with(requireContext()).load(R.drawable.forest_sunny)
                    .into(_binding?.imageViewBackground!!)
                layout5DayForecast.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
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
                    Timber.i("forecast data loaded from API $response")
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
                    Timber.i("error response from API $response")
                    context?.showErrorMessage(response.errorBody()!!)
                }
            }
    }

    private fun filterList(availableForecast: List<WeatherItem>): List<WeatherItem> {
        val currentWeatherItem = availableForecast[0]
        val subSequence =
            currentWeatherItem.dt_txt?.subSequence(11, currentWeatherItem.dt_txt!!.length)
        return availableForecast.filter { it.dt_txt!!.contains(subSequence.toString()) }
    }

    private fun getWeatherDataWithLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
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
        } else {
            getLastKnowLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            /*latitude = location.latitude
            longitude = location.longitude*/
            fetchCurrentWeatherInformation(35.0, 139.0)
            fetchWeatherForecastInformation(35.0, 139.0)
        }.addOnFailureListener {
            requireContext().toast("Failed on getting current location")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}