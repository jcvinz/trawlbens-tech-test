package com.calvin.trawlbenstechtest.domain.util

sealed class ViewState<out T>(
    val value: T? = null
) {
    class Success<out T>(val data: T) : ViewState<T>(data)
    class Error<out T>(val exception: Exception) : ViewState<T>()
    class Loading<out T>(val isLoading: Boolean) : ViewState<T>()
}