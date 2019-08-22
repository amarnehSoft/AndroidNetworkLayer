package com.amarnehsoft.networklayer

/**
 * Custom error class that hold all possible cases when [RemoteDataSource.getCustomerById] failed.
 */
sealed class CustomerRemoteError {
    object CustomerNotFound : CustomerRemoteError()
    object UserIsNotAuthorizedToGetThisCustomer : CustomerRemoteError()
    object CustomerIsInvalid : CustomerRemoteError()
    data class general(val throwable: Throwable) : CustomerRemoteError()
}