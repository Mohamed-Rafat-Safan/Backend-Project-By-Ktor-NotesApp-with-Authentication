package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Int,
    val fullName: String,
    val email: String,
)
