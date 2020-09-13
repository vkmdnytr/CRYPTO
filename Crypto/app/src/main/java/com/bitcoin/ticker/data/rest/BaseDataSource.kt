package com.bitcoin.ticker.data.rest

import com.bitcoin.ticker.util.sealed.Resource
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {

    protected suspend fun <T : Any> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.Success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T : Any> error(message: String): Resource<T> {
        Timber.d(message)
        return Resource.Error("Network call has failed for a following reason: $message")
    }

}