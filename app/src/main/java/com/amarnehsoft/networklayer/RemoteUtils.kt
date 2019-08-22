package com.amarnehsoft.networklayer

import retrofit2.Response

/**
 * Converts the retrofit [Response] into [RemoteResponse].
 *
 * @param T retrofit body type.
 * @param R remote response body type.
 * @param E remote response error type.
 *
 * Note that this method will return ERROR remote response if retrofit response body was null.
 */
fun <T, R, E> Response<T>.toRemoteResponse(
        bodyToRemote: (T) -> RemoteResponse<R, E>,
        remoteError: (error: RemoteError) -> E
): RemoteResponse<R, E> {
    return if (isSuccessful) {
        val body = body()
        if (body == null) {
            RemoteResponse.createError(remoteError(RemoteError.general(Exception("empty response"))))
        } else {
            bodyToRemote(body)
        }
    } else {
        RemoteResponse.createError(remoteError(RemoteError.Failed(code())))
    }
}

/**
 * Converts the retrofit [Response] into [RemoteResponse].
 *
 * @param T retrofit body type.
 * @param R remote response body type.
 * @param E remote response error type.
 *
 * Note that this method will return SUCCESS remote response if retrofit response body was null.
 */
fun <T, R, E> Response<T>.toRemoteResponseAllowEmptyBody(
        bodyToRemote: (T?) -> RemoteResponse<R?, E>,
        remoteError: (error: RemoteError) -> E
): RemoteResponse<R?, E> {
    return if (isSuccessful) {
        val body = body()
        if (body == null) {
            RemoteResponse.createSuccess(null)
        } else {
            bodyToRemote(body)
        }
    } else {
        RemoteResponse.createError(remoteError(RemoteError.Failed(code())))
    }
}

/**
 * Converts the retrofit [response] into [RemoteResponse] by [Response.toRemoteResponse],
 * and wrap it inside a try catch to safely map the exception to [RemoteResponse.Error].
 *
 * @param T retrofit body type.
 * @param R remote response body type.
 * @param E remote response error type.
 *
 * Note that this method will return [RemoteResponse.Error] if retrofit response body was null.
 */
fun <T, R, E> safeApiCall(
        response: Response<T>,
        bodyToRemote: (T) -> RemoteResponse<R, E>,
        remoteError: (RemoteError) -> E
): RemoteResponse<R, E> {
    return try {
        response.toRemoteResponse(bodyToRemote, remoteError)
    } catch (e: Exception) {
        RemoteResponse.createError(remoteError(RemoteError.General(e)))
    }
}

/**
 * Converts the retrofit [response] into [RemoteResponse] by [Response.toRemoteResponse],
 * and wrap it inside a try catch to safely map the exception to [RemoteResponse.Error].
 *
 * @param T retrofit body type.
 * @param R remote response body type.
 * @param E remote response error type.
 *
 * Note that this method will return [RemoteResponse.Success] if retrofit response body was null.
 */
fun <T, R, E> safeApiCallAllowEmptyBody(
        response: Response<T>,
        bodyToRemote: (T?) -> RemoteResponse<R?, E>,
        remoteError: (RemoteError) -> E
): RemoteResponse<R?, E> {
    return try {
        response.toRemoteResponseAllowEmptyBody(bodyToRemote, remoteError)
    } catch (e: Exception) {
        RemoteResponse.createError(remoteError(RemoteError.General(e)))
    }
}