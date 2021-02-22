package com.example.rickandmortygo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
class Origin(
    val name: String,
    val url: String
)