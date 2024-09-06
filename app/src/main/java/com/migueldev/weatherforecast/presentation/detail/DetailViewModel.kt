package com.migueldev.weatherforecast.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.migueldev.weatherforecast.domain.WeatherDomain
import com.migueldev.weatherforecast.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weatherResult = MutableLiveData<Result<WeatherDomain>>()
    val weatherResult: LiveData<Result<WeatherDomain>> get() = _weatherResult

    fun getWeather(cityName: String) {
        viewModelScope.launch {
           val result = getWeatherUseCase.execute(GetWeatherUseCase.Params(cityName, "ccd4d2cb8d1a04e53095adb44c3bd84e"))
            _weatherResult.value = result
        }
    }
}