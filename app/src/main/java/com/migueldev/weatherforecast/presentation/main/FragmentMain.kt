package com.migueldev.weatherforecast.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.Transliterator
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.migueldev.weatherforecast.R
import com.migueldev.weatherforecast.databinding.FragmentDetailBinding
import com.migueldev.weatherforecast.databinding.FragmentMainBinding
import com.migueldev.weatherforecast.presentation.detail.DetailFragment
import java.util.Locale


class FragmentMain : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val aLocale = Locale.Builder()
            .setLanguage("en")
            .setScript("Latn")
            .setRegion("RS")
            .build()

        geocoder = Geocoder(requireContext(), aLocale)

        binding.searchButton.setOnClickListener {
            val cityName = binding.searchInput.text.toString()
            searchCity(cityName)
        }

        binding.button.setOnClickListener {
            val markerLocation = mMap.cameraPosition.target
            getCityAndCountry(markerLocation) { city, country ->
                if (city != null && country != null) {
                    val locationName = "$city,$country"

                    val detailFragment = DetailFragment.newInstance(locationName)

                    findNavController().navigate(
                        R.id.action_fragmentMain_to_fragmentDetail,
                        detailFragment.arguments
                    )
                } else {
                    Toast.makeText(requireContext(), "Unable to fetch location details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        }

        val bogota = LatLng(4.60971, -74.08175)
        mMap.addMarker(MarkerOptions().position(bogota).title("Marker in Bogotá"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 12f))
    }

    private fun searchCity(cityName: String) {

        val addresses = geocoder.getFromLocationName(cityName, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val location = addresses[0]
            val latLng = LatLng(location.latitude, location.longitude)

            mMap.clear() // Clear previous markers
            mMap.addMarker(MarkerOptions().position(latLng).title(cityName))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        } else {
            Toast.makeText(requireContext(), "City not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCityAndCountry(latLng: LatLng, callback: (String?, String?) -> Unit) {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val city = address.locality ?: address.subAdminArea
            val country = address.countryName
            callback(city, country)
        } else {
            callback(null, null)
        }
    }



}