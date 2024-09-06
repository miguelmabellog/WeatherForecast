package com.migueldev.weatherforecast.infrastructure.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject



class WeatherApiClient @Inject constructor(
    private val weatherApiService: WeatherApiService
) {

    suspend fun getWeatherByCity(city: String, apiKey: String): Result<WeatherResponse> {
        return try {
            val weatherResponse = withContext(Dispatchers.IO) {
                weatherApiService.getWeatherByCity(city, apiKey)
            }
            Result.success(weatherResponse)
        } catch (e: Exception) {
            // En caso de error, devuelve un resultado fallido
            Result.failure(e)
        }
    }
}