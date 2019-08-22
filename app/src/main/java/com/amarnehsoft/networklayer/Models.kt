package com.amarnehsoft.networklayer

data class User(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val role: Role
)

data class Role(
        val roleId: Long,
        val roleName: String
)

data class Customer(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val phones: List<String>,
        val email: String,
        val address: String
)

data class Supplier(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val phones: List<String>,
        val email: String,
        val address: String
)