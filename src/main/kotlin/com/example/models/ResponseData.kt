package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    val data: T,
    val isSuccess: Boolean
)
