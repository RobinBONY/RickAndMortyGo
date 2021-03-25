package com.example.rickandmortygo.data

import kotlinx.serialization.Serializable

@Serializable
data class EpisodesResult(
    val info: Info,
    val results: List<Episode>
)