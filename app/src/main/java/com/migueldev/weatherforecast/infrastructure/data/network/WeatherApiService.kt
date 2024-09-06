package com.migueldev.weatherforecast.infrastructure.data.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("APPID") apiKey: String
    ): WeatherResponse
}