package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Notes(
    val id: Int,
    val title: String,
    val note: String
)
