package com.example.rickandmortygo.api

import com.example.rickandmortygo.data.Character
import com.example.rickandmortygo.data.CharactersResult
import com.example.rickandmortygo.data.EpisodesResult
import com.example.rickandmortygo.data.LocationsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickEtMortyApi {
    @GET("character")
    fun fetchCharacters(): Call<CharactersResult>

    @GET("character/{id}")
    fun fetchCharacter(@Path("id") id: String): Call<Character>

    @GET("character")
    fun fetchNextCharacters(@Query("page") page: String): Call<CharactersResult>

    @GET("episode")
    fun fetchEpisodes(): Call<EpisodesResult>

    @GET("episode")
    fun fetchNextEpisodes(@Query("page") page: String): Call<EpisodesResult>

    @GET("location")
    fun fetchLocations(): Call<LocationsResult>

    @GET("location")
    fun fetchNextLocations(@Query("page") page: String ): Call<LocationsResult>
}