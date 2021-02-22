package com.example.rickandmortygo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortygo.data.model.Character

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCharacter(character: Character)

    @Query("SELECT * FROM character ORDER BY id ASC")
    fun getCollection(): List<Character>
}