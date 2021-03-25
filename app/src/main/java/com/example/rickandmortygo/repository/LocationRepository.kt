package com.example.rickandmortygo.repository

import androidx.lifecycle.MutableLiveData
import com.example.rickandmortygo.api.ApiManager
import com.example.rickandmortygo.data.Info
import com.example.rickandmortygo.data.Location
import com.example.rickandmortygo.data.LocationsResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber



object LocationRepository {
    // DATASource
    val error = MutableLiveData<String?>()

    var locationsList = MutableLiveData<List<Location>?>()
    val nextLocationsPage = MutableLiveData<Info>()

    /* LOCATIONS */
    fun clearData(){
        locationsList.value =  null
    }

    fun fetchAllLocations() {
        val call = ApiManager.rickEtMortyApi.fetchLocations()
        fetchLocations(call)
    }

    fun fetchNextLocationsPage(){
        if(nextLocationsPage.value?.next != null) {
            val regex = "[0-9]+".toRegex()
            var page = regex.find(nextLocationsPage.value?.next!!)
            if (page != null) {
                val call =
                    ApiManager.rickEtMortyApi.fetchNextLocations(page.groups[0]?.value!!)
                fetchLocations(call)
            }
        }
    }

    private fun fetchLocations(call: Call<LocationsResult>){
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
}