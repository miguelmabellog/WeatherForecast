package com.migueldev.weatherforecast.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.migueldev.weatherforecast.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    var cityName: String = "city Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
             cityName = it.getString(ARG_CITY_NAME).toString()
            viewModel.getWeather(cityName ?: "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeWeatherResult()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.title = cityName
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeWeatherResult() {
        viewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                val weather = result.getOrNull()
                weather?.let {
                    binding.weatherResult.text = "Temperature: ${it.temperature}°C, " +
                            "Description: ${it.description}"
                } ?: run {
                    binding.weatherResult.text = "Unknown error occurred"
                }
            } else if (result.isFailure) {
                binding.weatherResult.text = "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    companion object {
        private const val ARG_CITY_NAME = "city_name"
        @JvmStatic
        fun newInstance(cityName: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CITY_NAME, cityName)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}