package com.migueldev.weatherforecast.infrastructure.data.repository

import com.migueldev.weatherforecast.domain.WeatherDomain
import com.migueldev.weatherforecast.domain.toDomain
import com.migueldev.weatherforecast.infrastructure.data.network.WeatherApiClient


class WeatherRepository(private val weatherApiClient: WeatherApiClient) {

    suspend fun getWeatherByCity(city: String, apiKey: String): Result<WeatherDomain> {
        return try {
            // Llama directamente a la función suspendida de WeatherApiClient
            val weatherResponseResult = weatherApiClient.getWeatherByCity(city, apiKey)

            when {
                weatherResponseResult.isSuccess -> {
                    weatherResponseResult.getOrNull()?.let { weatherResponse ->
                        // Convertir el WeatherResponse a WeatherDomain
                        val weatherDomain = weatherResponse.toDomain()
                        Result.success(weatherDomain)
                    } ?: Result.failure(Exception("No data received"))
                }
                else -> Result.failure(weatherResponseResult.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}