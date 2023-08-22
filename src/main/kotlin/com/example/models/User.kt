package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val password: String
)
