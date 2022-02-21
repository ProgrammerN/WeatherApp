package com.dvt.weatherapp.activities.home.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvt.weatherapp.R
import com.dvt.weatherapp.extentions.toast
import com.dvt.weatherapp.viewmodels.LocalWeatherViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class FavoritesMapView : Fragment(), GoogleMap.OnMarkerClickListener {

    private val localWeatherViewModel: LocalWeatherViewModel by viewModels()
    private val callback = OnMapReadyCallback { googleMap ->
        showFavorites(googleMap)
        // Set a listener for marker click.
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun showFavorites(googleMap: GoogleMap) {
        localWeatherViewModel.getAllCurrentWeather()?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                for (weatherResponse in it) {
                    val marker = LatLng(weatherResponse.coord!!.lat, weatherResponse.coord!!.lon)
                    googleMap.addMarker(MarkerOptions()
                        .position(marker)
                        .title(weatherResponse.name))
                }

                val mountainView = LatLng(it[0].coord!!.lat, it[0].coord!!.lon)
                googleMap.setMinZoomPreference(6.0f)
                googleMap.setMaxZoomPreference(14.0f)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(mountainView))
                val cameraPosition = CameraPosition.Builder()
                    .target(mountainView) // Sets the center of the map to Mountain View
                    .zoom(17f)            // Sets the zoom
                    .tilt(30f)            // Sets the tilt of the camera to 30 degrees
                    .build()              // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }else{
                requireContext().toast("No favorites found")
            }
        }
    }

    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {
        requireContext().toast("${marker.title}")
        return false
    }
}