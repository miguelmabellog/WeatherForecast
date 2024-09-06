package com.migueldev.weatherforecast.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class FragmentMain : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap

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


        binding.button.setOnClickListener {
            val cityName = "Bogota,Colombia"
            val detailFragment = DetailFragment.newInstance(cityName)


            findNavController().navigate(R.id.action_fragmentMain_to_fragmentDetail, detailFragment.arguments)
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


}