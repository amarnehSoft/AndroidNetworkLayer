package com.amarnehsoft.networklayer

typealias ApiRemoteResponse<T> = RemoteResponse<T, Exception>

interface RemoteDataSource {
    suspend fun getCustomers(): ApiRemoteResponse<List<Customer>>
    suspend fun getUsers(): ApiRemoteResponse<List<User>>
    suspend fun getSuppliers(): ApiRemoteResponse<List<Supplier>>
    suspend fun getCustomerById(customerId: Long): RemoteResponse<Customer, CustomerRemoteError>
    suspend fun getSupplier(supplierId: Long): RemoteResponse<Supplier, Exception>
}