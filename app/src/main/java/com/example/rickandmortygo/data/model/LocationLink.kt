package com.example.rickandmortygo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationLink(
    val name: String,
    val url: String
)