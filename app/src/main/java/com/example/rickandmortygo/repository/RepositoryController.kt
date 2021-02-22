package com.example.rickandmortygo.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.rickandmortygo.api.ApiManager
import com.example.rickandmortygo.data.database.CharacterDatabase
import com.example.rickandmortygo.data.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


object RepositoryController {
// DATASource

    val error = MutableLiveData<String>()

    val collectionList = MutableLiveData<List<Character>>()

    val charactersList = MutableLiveData<List<Character>>()
    val nextCharactersPage = MutableLiveData<Info>()

    val episodesList = MutableLiveData<List<Episode>>()
    val nextEpisodesPage = MutableLiveData<Info>()

    val locationsList = MutableLiveData<List<Location>>()
    val nextLocationsPage = MutableLiveData<Info>()

    /* CHARACTERS */
    fun fetchAllCharacters() {
        val call = ApiManager.rickEtMortyApi.fetchCharacters()
        fetchCharacters(call)
    }

    fun fetchNextCharactersPage() {
        if(nextCharactersPage.value?.next != null) {
            val regex = "\\?page=([0-9]*)".toRegex()
            var page = regex.find(nextCharactersPage.value?.next!!)
            if (page != null) {
                val call =
                    ApiManager.rickEtMortyApi.fetchNextCharacters(page.groups[1]?.value?.toInt()!!)
                fetchCharacters(call)
            }
        }
    }

    private fun fetchCharacters(call: Call<CharactersResult>) {
        call.enqueue(object : Callback<CharactersResult> {
            override fun onResponse(
                call: Call<CharactersResult>,
                response: Response<CharactersResult>
            ) {
                if (response.isSuccessful) {
                    charactersList.postValue(response.body()?.results)
                    nextCharactersPage.postValue(response.body()?.info)
                }
            }

            override fun onFailure(call: Call<CharactersResult>, t: Throwable) {
                Timber.e("fetchCharacters onFailure, ${t.localizedMessage}")
                Timber.e("fetchCharacters onFailure, $t")

                error.postValue("onFailure 404")

                charactersList.postValue(emptyList())
            }
        })
    }

    /* EPISODES */
    fun fetchAllEpisodes() {
        val call = ApiManager.rickEtMortyApi.fetchEpisodes()
        fetchEpisodes(call)
    }


    fun fetchNextEpisodesPage() {
        if(nextEpisodesPage.value?.next != null) {
            val regex = "\\?page=([0-9]*)".toRegex()
            var page = regex.find(nextEpisodesPage.value?.next!!)
            if (page != null) {
                val call =
                    ApiManager.rickEtMortyApi.fetchNextEpisodes(page.groups[1]?.value?.toInt()!!)
                fetchEpisodes(call)
            }
        }
    }

    private fun fetchEpisodes(call: Call<EpisodesResult>) {
        call.enqueue(object : Callback<EpisodesResult> {
            override fun onResponse(
                call: Call<EpisodesResult>,
                response: Response<EpisodesResult>
            ) {
                if (response.isSuccessful) {
                    episodesList.postValue(response.body()?.results)
                    nextEpisodesPage.postValue(response.body()?.info)
                }
            }

            override fun onFailure(call: Call<EpisodesResult>, t: Throwable) {
                Timber.e("fetchEpisodes onFailure, ${t.localizedMessage}")
                Timber.e("fetchEpisodes onFailure, $t")

                error.postValue("onFailure 404")

                episodesList.postValue(emptyList())
            }
        })
    }

    /* LOCATIONS */
    fun fetchAllLocations() {
        val call = ApiManager.rickEtMortyApi.fetchLocations()
        fetchLocations(call)
    }


    fun fetchNextLocationsPage() {
        if(nextLocationsPage.value?.next != null) {
            val regex = "\\?page=([0-9]*)".toRegex()
            var page = regex.find(nextLocationsPage.value?.next!!)
            if (page != null) {
                val call =
                    ApiManager.rickEtMortyApi.fetchNextLocations(page.groups[1]?.value?.toInt()!!)
                fetchLocations(call)
            }
        }
    }

    private fun fetchLocations(call: Call<LocationsResult>) {
        call.enqueue(object : Callback<LocationsResult> {
            override fun onResponse(
                call: Call<LocationsResult>,
                response: Response<LocationsResult>
            ) {
                if (response.isSuccessful) {
                    locationsList.postValue(response.body()?.results)
                    nextLocationsPage.postValue(response.body()?.info)
                }
            }

            override fun onFailure(call: Call<LocationsResult>, t: Throwable) {
                Timber.e("fetchLocations onFailure, ${t.localizedMessage}")
                Timber.e("fetchLocations onFailure, $t")

                error.postValue("onFailure 404")

                locationsList.postValue(emptyList())
            }
        })
    }

    /* COLLECTION */
    fun fetchCharacter(link: String,context: Context) {

        val regex = "[0-9]+".toRegex()
        var page = regex.find(link)
        if (page != null) {
            val call = ApiManager.rickEtMortyApi.fetchCharacter(page.groups[0]?.value!!)
            fetchCharacter(call, context)
        }
    }

    private fun fetchCharacter(call: Call<CharactersResult>,context: Context) {
        call.enqueue(object : Callback<CharactersResult> {
            override fun onResponse(
                call: Call<CharactersResult>,
                response: Response<CharactersResult>
            ) {
                if (response.isSuccessful) {
                    val db = CharacterDatabase.getDatabase(context)
                    val dao = db.characterDao()
                    dao.addCharacter(response.body()?.results?.get(0)!!)
                    fetchCollection(context)
                }
            }

            override fun onFailure(call: Call<CharactersResult>, t: Throwable) {
                Timber.e("fetchCharacters onFailure, ${t.localizedMessage}")
                Timber.e("fetchCharacters onFailure, $t")

                error.postValue("onFailure 404")

                charactersList.postValue(emptyList())
            }
        })
    }

    fun fetchCollection(context: Context){
        val db = CharacterDatabase.getDatabase(context)
        val dao = db.characterDao()
        val character = dao.getCollection()
        collectionList.postValue(character)
    }
}