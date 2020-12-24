package com.example.rickandmortygo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EpisodesResult(
    val info: Info,
    val results: List<Episode>
)