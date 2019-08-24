package com.amarnehsoft.networklayer

class RemoteDataSourceImpl(
        private val endPoint: EndPoint
) : RemoteDataSource {

    override suspend fun getCustomers(): ApiRemoteResponse<List<Customer>> {
        return safeApiResponseCall(endPoint.getCustomers())
    }

    override suspend fun getUsers(): ApiRemoteResponse<List<User>> {
        return safeApiResponseCall(endPoint.getUsers())
    }

    override suspend fun getSuppliers(): ApiRemoteResponse<List<Supplier>> {
        return safeApiResponseCall(endPoint.getSuppliers())
    }

    override suspend fun getSupplier(supplierId: Long): RemoteResponse<Supplier, Exception> {
        return safeApiCall(
                response = endPoint.getSupplierById(supplierId),
                bodyToRemote = {
                    RemoteResponse.createSuccess<Supplier, Exception>(it)
                },
                remoteError = {
                    it.asException()
                }
        )
    }

    override suspend fun getCustomerById(customerId: Long): RemoteResponse<Customer, CustomerRemoteError> {
        return safeApiResponseCall(
                response = endPoint.getCustomerById(customerId),
                remoteError = {
                    CustomerRemoteError.General(it.asException())
                },
                brandError = { (messageId, message) ->
                    when (messageId) {
                        CUSTOMER_IS_NOT_FOUND_ID -> CustomerRemoteError.CustomerNotFound
                        CUSTOMER_IS_INVALID_ID -> CustomerRemoteError.CustomerIsInvalid
                        USER_IS_NOT_AUTHORIZED_TO_GET_THIS_CUSTOMER -> CustomerRemoteError.UserIsNotAuthorizedToGetThisCustomer
                        else -> CustomerRemoteError.General(Exception("unknown messageId: $messageId, message=$message"))
                    } as CustomerRemoteError
                }
        )
    }

    companion object {
        const val CUSTOMER_IS_NOT_FOUND_ID = 1L
        const val CUSTOMER_IS_INVALID_ID = 2L
        const val USER_IS_NOT_AUTHORIZED_TO_GET_THIS_CUSTOMER = 3L
    }
}