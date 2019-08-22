package com.amarnehsoft.networklayer

/**
 * @param T type of body when the response is [Success].
 * @param E type of error when the response is [Error].
 */
sealed class RemoteResponse<T, E> {

    data class Success<T, E>(val body: T) : RemoteResponse<T, E>()

    data class Error<T, E>(val error: E) : RemoteResponse<T, E>()

    /**
     * Apply the [mapper] if the response is [Success].
     */
    fun <R> map(mapper: (T) -> R): RemoteResponse<R, E> {
        return when (this) {
            is Success -> createSuccess(mapper(body))
            is Error -> createError(error)
        }
    }

    /**
     * Apply the [mapper] if the response is [Error].
     */
    fun <R> mapError(mapper: (E) -> R): RemoteResponse<T, R> {
        return when (this) {
            is Success -> createSuccess(body)
            is Error -> createError(mapper(error))
        }
    }

    companion object {
        fun <T, E> createError(error: E): RemoteResponse<T, E> {
            return Error(error)
        }

        fun <T, E> createSuccess(data: T): RemoteResponse<T, E> {
            return Success(data)
        }
    }
}