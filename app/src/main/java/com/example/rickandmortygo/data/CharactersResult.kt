package com.example.rickandmortygo.data

import kotlinx.serialization.Serializable

@Serializable
data class CharactersResult(
    val info: Info,
    val results: List<Character>
)