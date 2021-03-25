package com.example.rickandmortygo.presentation.locations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.rickandmortygo.data.Location
import com.example.rickandmortygo.repository.LocationRepository

class LocationsViewModel:ViewModel() {
    var locationsList = MutableLiveData<List<Location>?>()
    val error = MutableLiveData<String>()

    fun clear(){
        locationsList.value =  null
        LocationRepository.clearData()
    }

    fun fetchAllLocations() {
        LocationRepository.fetchAllLocations()
    }

    fun fetchNextLocationsPage() {
        LocationRepository.fetchNextLocationsPage()
    }

    fun observeLocations(locationsFragment: LocationsFragment) {
        LocationRepository.locationsList.observe(locationsFragment, Observer{
                data ->
            locationsList.postValue(data)
        })
    }

    fun observeError(locationsFragment: LocationsFragment) {
        LocationRepository.error.observe(locationsFragment, Observer{
            data ->
            error.postValue(data)
        })
    }
}