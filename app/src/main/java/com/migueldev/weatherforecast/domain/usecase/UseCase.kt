package com.migueldev.weatherforecast.domain.usecase


abstract class UseCase<in Params, Type> where Type : Any {


    abstract suspend fun execute(params: Params): Result<Type>

    suspend operator fun invoke(params: Params, onResult: (Result<Type>) -> Unit = {}) {
        try {
            val result = execute(params)
            onResult(result)
        } catch (e: Exception) {
            onResult(Result.failure(e))
        }
    }
}