package com.migueldev.weatherforecast.domain.usecase

import com.migueldev.weatherforecast.domain.toDomain
import com.migueldev.weatherforecast.infrastructure.data.network.Clouds
import com.migueldev.weatherforecast.infrastructure.data.network.Coord
import com.migueldev.weatherforecast.infrastructure.data.network.Main
import com.migueldev.weatherforecast.infrastructure.data.network.Sys
import com.migueldev.weatherforecast.infrastructure.data.network.Weather
import com.migueldev.weatherforecast.infrastructure.data.network.WeatherResponse
import com.migueldev.weatherforecast.infrastructure.data.network.Wind
import com.migueldev.weatherforecast.infrastructure.data.repository.WeatherRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetWeatherUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @Before
    fun setUp() {
        weatherRepository = mockk()
        getWeatherUseCase = GetWeatherUseCase(weatherRepository)
    }

    @Test
    fun `execute should return weather domain when repository returns success`() = runBlocking {
        // Arrange
        val city = "London"
        val apiKey = "test_api_key"
        val params = GetWeatherUseCase.Params(city, apiKey)

        val weatherResponse = WeatherResponse(
            coord = Coord(lon = 0.0, lat = 0.0),
            weather = listOf(Weather(id = 0, main = "Clear", description = "Clear sky", icon = "01d")),
            base = "stations",
            main = Main(temp = 20.0, feels_like = 18.0, temp_min = 15.0, temp_max = 25.0, pressure = 1013, humidity = 60, sea_level = null, grnd_level = null),
            visibility = 10000,
            wind = Wind(speed = 5.0, deg = 180),
            clouds = Clouds(all = 0),
            dt = 1618317040,
            sys = Sys(type = 1, id = 1234, country = "GB", sunrise = 1618296000, sunset = 1618358400),
            timezone = 0,
            id = 2643743,
            name = "London",
            cod = 200
        ).toDomain()

        coEvery { weatherRepository.getWeatherByCity(city, apiKey) } returns Result.success(weatherResponse)

        // Act
        val result = getWeatherUseCase.execute(params)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(weatherResponse, result.getOrNull())
    }

    @Test
    fun `execute should return failure when repository returns failure`() = runBlocking {
        // Arrange
        val city = "London"
        val apiKey = "test_api_key"
        val params = GetWeatherUseCase.Params(city, apiKey)
        val exception = Exception("Network error")

        coEvery { weatherRepository.getWeatherByCity(city, apiKey) } returns Result.failure(exception)

        // Act
        val result = getWeatherUseCase.execute(params)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
