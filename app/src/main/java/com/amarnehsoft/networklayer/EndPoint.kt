package com.amarnehsoft.networklayer

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EndPoint {
    @GET("customers")
    suspend fun getCustomers(): Response<ApiResponse<List<Customer>>>

    @GET("users")
    suspend fun getUsers(): Response<ApiResponse<List<User>>>

    @GET("suppliers")
    suspend fun getSuppliers(): Response<ApiResponse<List<Supplier>>>

    @GET("customer/{customerId}")
    suspend fun getCustomerById(@Path("customerId") customerId: Long): Response<ApiResponse<Customer>>

    @GET("supplier/{supplierId}")
    suspend fun getSupplierById(@Path("supplierId") supplierId: Long): Response<Supplier>

    companion object {
        const val END_POINT = "www.endpoint.com"
    }
}