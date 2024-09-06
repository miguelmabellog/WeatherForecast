package com.migueldev.weatherforecast.infrastructure.di

import com.migueldev.weatherforecast.domain.usecase.GetWeatherUseCase
import com.migueldev.weatherforecast.infrastructure.data.network.WeatherApiClient
import com.migueldev.weatherforecast.infrastructure.data.network.WeatherApiService
import com.migueldev.weatherforecast.infrastructure.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherApiClient(weatherApiService: WeatherApiService): WeatherApiClient {
        return WeatherApiClient(weatherApiService)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApiClient: WeatherApiClient): WeatherRepository {
        return WeatherRepository(weatherApiClient)
    }

    @Provides
    @Singleton
    fun provideGetWeatherUseCase(weatherRepository: WeatherRepository): GetWeatherUseCase {
        return GetWeatherUseCase(weatherRepository)
    }
}

