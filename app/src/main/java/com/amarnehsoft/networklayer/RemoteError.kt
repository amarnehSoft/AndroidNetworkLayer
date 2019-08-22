package com.amarnehsoft.networklayer

/**
 * Every Network request may have one of this two error.
 */
sealed class RemoteError {

    /**
     * Indicating that the error is a thrown exception.
     */
    data class General(val throwable: Throwable) : RemoteError()

    /**
     * Indicating that the error is that the response code is not within 200..299
     */
    data class Failed(val responseCode: Int) : RemoteError()

    fun asException(): Exception {
        return when (this) {
            is General -> Exception(this.throwable)
            is Failed -> Exception("Request failed with response code: ${this.responseCode}")
        }
    }

    companion object {
        fun failed(responseCode: Int): RemoteError {
            return Failed(responseCode)
        }

        fun general(throwable: Throwable): RemoteError {
            return General(throwable)
        }
    }
}