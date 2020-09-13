package com.bitcoin.ticker.util.sealed


sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val value: T) : Resource<T>()
    data class Error(val message: String, val cause: Exception? = null) : Resource<Nothing>()
}