package com.example.rickandmortygo.data

import kotlinx.serialization.Serializable
import java.lang.reflect.Constructor

@Serializable
data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String

)