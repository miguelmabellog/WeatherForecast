package com.migueldev.weatherforecast.domain.usecase

import com.migueldev.weatherforecast.domain.WeatherDomain
import com.migueldev.weatherforecast.infrastructure.data.network.WeatherResponse
import com.migueldev.weatherforecast.infrastructure.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository
) : UseCase<GetWeatherUseCase.Params, WeatherDomain>() {

    override suspend fun execute(params: Params): Result<WeatherDomain> {
        return weatherRepository.getWeatherByCity(params.city, params.apiKey).let { result ->
            when {
                result.isSuccess -> result
                else -> {
                    Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
                }
            }

    }


    }
    data class Params(
        val city: String,
        val apiKey: String
    )
}
