package com.example.rickandmortygo.presentation.collection

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortygo.MainActivity
import com.example.rickandmortygo.data.Character
import com.example.rickandmortygo.repository.CharacterRepository
import com.example.rickandmortygo.repository.FirebaseRepository
import com.example.rickandmortygo.repository.RoomRepository
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.launch

class CollectionViewModel:ViewModel() {
    var collectionList = MutableLiveData<List<Character>?>()
    var error = MutableLiveData<String>()

    fun clear(){
        collectionList.value =  null
        RoomRepository.clearData()
    }

    fun fetchCollection(context: Context) {
        RoomRepository.fetchCollection(context)
    }

    fun observeCharacters(collectionFragment: CollectionFragment) {
        RoomRepository.collectionList.observe(collectionFragment, Observer {
                data ->
            collectionList.postValue(data)
        })
    }

    fun observeError(collectionFragment: CollectionFragment) {
        CharacterRepository.error.observe(collectionFragment, Observer {
                data ->
            error.postValue(data)
        })
    }

    fun fetchScannedCharacter(result: IntentResult?, context: Context) {
        viewModelScope.launch {
            CharacterRepository.fetchCharacter(result?.contents!!, context)
           RoomRepository.fetchCollection(context)
        }
    }

    fun getUserData(context: Context) {
        viewModelScope.launch {
            if(FirebaseRepository.getUserData()){
                clear()
                fetchCollection(context)
            }
            else{
                sendUserData()
            }
        }
    }

    fun sendUserData(){
        FirebaseRepository.sendUserData(RoomRepository.collectionList.value!!)
    }
}