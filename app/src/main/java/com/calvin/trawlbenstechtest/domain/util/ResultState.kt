package com.calvin.trawlbenstechtest.domain.util

sealed class ResultState<T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Failed<T>(val error: Exception) : ResultState<T>()
}

inline fun <T> ResultState<T>.onSuccess(action: (T) -> Unit): ResultState<T> {
    if (this is ResultState.Success) action(data)
    return this
}

inline fun <T> ResultState<T>.onFailure(action: (Exception) -> Unit): ResultState<T> {
    if (this is ResultState.Failed) action(error)
    return this
}