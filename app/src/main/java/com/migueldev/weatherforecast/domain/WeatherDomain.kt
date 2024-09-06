package com.migueldev.weatherforecast.domain

import com.migueldev.weatherforecast.infrastructure.data.network.WeatherResponse

data class WeatherDomain(
    val temperature:Double,
    val description: String,
    val windSpeed: Double
)
fun WeatherResponse.toDomain(): WeatherDomain {
    return WeatherDomain(
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "No description",
        windSpeed = wind.speed
    )
}