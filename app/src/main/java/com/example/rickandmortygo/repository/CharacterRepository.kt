package com.example.rickandmortygo.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.rickandmortygo.api.ApiManager
import com.example.rickandmortygo.data.Character
import com.example.rickandmortygo.data.CharacterDatabase
import com.example.rickandmortygo.data.CharactersResult
import com.example.rickandmortygo.data.Info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object CharacterRepository {
    // DATASource
    val error = MutableLiveData<String?>()

    var charactersList = MutableLiveData<List<Character>?>()
    val nextCharactersPage = MutableLiveData<Info>()

    /* CHARACTERS */
    fun clearData(){
        charactersList.value =  null
    }

    fun fetchAllCharacters(){
        val call = ApiManager.rickEtMortyApi.fetchCharacters()
        fetchCharacters(call)
    }

    fun fetchNextCharactersPage() {
        if(nextCharactersPage.value?.next != null) {
            val regex = "[0-9]+".toRegex()
            var page = regex.find(nextCharactersPage.value?.next!!)
            if (page != null) {
                val call =
                    ApiManager.rickEtMortyApi.fetchNextCharacters(page.groups[0]?.value!!)
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
                    charactersList.postValue(response.body()?.results!!)
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

    /* COLLECTION */
    fun fetchCharacter(link: String, context: Context) {

        val regex = "[0-9]+".toRegex()
        var page = regex.find(link)
        if (page != null) {
            val call = ApiManager.rickEtMortyApi.fetchCharacter(page.groups[0]?.value!!)
            fetchCharacter(call, context)
        }
    }

    private fun fetchCharacter(call: Call<Character>, context: Context) {
        call.enqueue(object : Callback<Character> {
            override fun onResponse(
                call: Call<Character>,
                response: Response<Character>
            ) {
                if (response.isSuccessful) {
                    val db = CharacterDatabase.getDatabase(context)
                    val dao = db.characterDao()
                    val t: Thread = object : Thread() {
                        override fun run() {
                            dao.addCharacter(response.body()!!)
                        }
                    }
                    t.start()

                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                Timber.e("fetchCharacters onFailure, ${t.localizedMessage}")
                Timber.e("fetchCharacters onFailure, $t")

                error.postValue("onFailure 404")
            }
        })
    }
}