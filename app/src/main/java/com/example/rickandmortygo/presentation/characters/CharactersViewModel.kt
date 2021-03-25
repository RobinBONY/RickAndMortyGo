package com.example.rickandmortygo.presentation.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.rickandmortygo.data.Character
import com.example.rickandmortygo.repository.CharacterRepository

class CharactersViewModel: ViewModel() {
    var charactersList = MutableLiveData<List<Character>?>()
    var error = MutableLiveData<String>()

    fun clear(){
        charactersList.value =  null
        CharacterRepository.clearData()
    }

    fun fetchAllCharacters() {
        CharacterRepository.fetchAllCharacters()
    }

    fun fetchNextCharactersPage(){
        CharacterRepository.fetchNextCharactersPage()
    }

    fun observeCharacters(charactersFragment: CharactersFragment) {
        CharacterRepository.charactersList.observe(charactersFragment, Observer {
                data ->
            charactersList.postValue(data)
        })
    }

    fun observeError(charactersFragment: CharactersFragment) {
        CharacterRepository.error.observe(charactersFragment, Observer {
            data ->
            error.postValue(data)
        })
    }
}