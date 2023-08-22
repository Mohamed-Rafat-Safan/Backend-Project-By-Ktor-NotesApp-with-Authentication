package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title: String,
    val note: String
)
