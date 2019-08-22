package com.amarnehsoft.networklayer

import retrofit2.Response

/**
 * Converts the retrofit [Response] that hold [ApiResponse] into [RemoteResponse].
 *
 * Custom implementation to convert [ApiResponse] into [RemoteResponse].
 * This implementation depends on the structure of [ApiResponse] model.
 *
 * @param T api response data type, and remote response body type.
 * @param E remote response error type.
 */
fun <T, E> ApiResponse<T>.toRemoteResponse(
        remoteError: (RemoteError) -> E,
        brandError: (BrandError) -> E
): RemoteResponse<T, E> {
    val body = this.data
    val success = this.result == 1
    val error = this.error

    return if (success) {
        if (body == null)
            RemoteResponse.createError(remoteError(RemoteError.general(Exception("ApiResponse body is null!"))))
        else
            RemoteResponse.createSuccess(body)
    } else {
        if (error == null)
            RemoteResponse.createError(remoteError(RemoteError.general(Exception("ApiResponse error is null, and result =$result"))))
        else
            RemoteResponse.createError(brandError(error))
    }
}

/**
 * Converts the retrofit [response] that holds [ApiResponse] into [RemoteResponse],
 * and safely map the exceptions to [RemoteResponse.Error].
 *
 * This is a custom implementation depends on [ApiResponse] structure.
 *
 * @param T retrofit body type, and remote response body type.
 * @param E remote response error type.
 */
fun <T, E> safeApiResponseCall(
        response: Response<ApiResponse<T>>,
        remoteError: (RemoteError) -> E,
        brandError: (BrandError) -> E
): RemoteResponse<T, E> {
    return safeApiCall(
            response = response,
            bodyToRemote = {
                it.toRemoteResponse(remoteError, brandError)
            },
            remoteError = remoteError
    )
}

/**
 * Converts the retrofit [response] that holds [ApiResponse] into [RemoteResponse],
 * and safely map the exceptions to [RemoteResponse.Error].
 *
 * This is a custom implementation depends on [ApiResponse] structure.
 *
 * Note that the desired error type is defined as [Exception].
 *
 * @param T retrofit body type, and remote response body type.
 *
 * @return [RemoteResponse] with [T] body, and error of type [Exception].
 */
fun <T> safeApiResponseCall(
        response: Response<ApiResponse<T>>
): RemoteResponse<T, Exception> {
    return safeApiResponseCall(
            response = response,
            remoteError = {
                it.asException()
            },
            brandError = {
                Exception(it.toString())
            }
    )
}