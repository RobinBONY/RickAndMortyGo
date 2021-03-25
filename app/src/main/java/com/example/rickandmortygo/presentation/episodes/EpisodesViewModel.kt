package com.example.rickandmortygo.presentation.episodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.rickandmortygo.data.Episode
import com.example.rickandmortygo.repository.EpisodeRepository

class EpisodesViewModel: ViewModel() {
    var episodesList = MutableLiveData<List<Episode>?>()
    var error = MutableLiveData<String>()

    fun clear(){
        episodesList.value =  null
        EpisodeRepository.clearData()
    }

    fun fetchAllEpisodes() {
        EpisodeRepository.fetchAllEpisodes()
    }

    fun fetchNextEpisodesPage(){
       EpisodeRepository.fetchNextEpisodesPage()
    }

    fun observeEpisodes(episodesFragment: EpisodesFragment) {
        EpisodeRepository.episodesList.observe(episodesFragment, Observer {
                data ->
            episodesList.postValue(data)
        })
    }

    fun observeError(episodesFragment: EpisodesFragment) {
        EpisodeRepository.error.observe(episodesFragment, Observer {
                data ->
            error.postValue(data)
        })
    }
}