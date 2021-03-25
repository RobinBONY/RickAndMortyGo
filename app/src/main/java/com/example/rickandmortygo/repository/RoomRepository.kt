package com.example.rickandmortygo.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.rickandmortygo.data.Character
import com.example.rickandmortygo.data.CharacterDatabase

object RoomRepository {
    // DATASource
    var collectionList = MutableLiveData<List<Character>?>()

    /* COLLECTION */
    fun fetchCollection(context: Context) {
        val db = CharacterDatabase.getDatabase(context)
        val dao = db.characterDao()

        val t: Thread = object : Thread() {
            override fun run() {
                val character = dao.getCollection()
                collectionList.postValue(character)
            }
        }
        t.start()
    }

    fun clearData() {
        collectionList.value =  null
    }

    fun raz(context: Context) {
        val db = CharacterDatabase.getDatabase(context)
        val dao = db.characterDao()

        val t: Thread = object : Thread() {
            override fun run() {
                dao.raz()
            }
        }
        t.start()
    }
}