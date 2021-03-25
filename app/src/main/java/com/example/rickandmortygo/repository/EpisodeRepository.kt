package com.example.rickandmortygo.repository

import androidx.lifecycle.MutableLiveData
import com.example.rickandmortygo.api.ApiManager
import com.example.rickandmortygo.data.Episode
import com.example.rickandmortygo.data.EpisodesResult
import com.example.rickandmortygo.data.Info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber



object EpisodeRepository {
    // DATASource
    val error = MutableLiveData<String?>()

    var episodesList = MutableLiveData<List<Episode>?>()
    val nextEpisodesPage = MutableLiveData<Info>()

    /* EPISODES */
    fun clearData(){
        episodesList.value =  null
    }

    fun fetchAllEpisodes() {
        val call = ApiManager.rickEtMortyApi.fetchEpisodes()
        fetchEpisodes(call)
    }

    fun fetchNextEpisodesPage() {
        if(nextEpisodesPage.value?.next != null) {
            val regex = "[0-9]+".toRegex()
            var page = regex.find(nextEpisodesPage.value?.next!!)
            if (page != null) {
                val call =
                    ApiManager.rickEtMortyApi.fetchNextEpisodes(page.groups[0]?.value!!)
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
}