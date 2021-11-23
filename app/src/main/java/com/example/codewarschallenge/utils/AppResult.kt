package com.example.codewarschallenge.utils

sealed class AppResult<out T>{
    data class Success<out T>(val successData: T): AppResult<T>()

    class Error(val exception: Exception, val message: String? = exception.localizedMessage) : AppResult<Nothing>()

    class Warning(val message: String) : AppResult<Nothing>()
}
