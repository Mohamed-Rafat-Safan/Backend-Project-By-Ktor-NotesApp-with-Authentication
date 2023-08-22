package com.example.models


import kotlinx.serialization.Serializable

@Serializable
data class UserAuthResponse(
    val user: UserInfo,
    val token: String
)