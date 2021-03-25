package com.example.rickandmortygo.data.converters

import android.text.TextUtils
import androidx.room.TypeConverter
import com.example.rickandmortygo.data.LocationLink
import com.example.rickandmortygo.data.Origin
import com.google.gson.Gson

class RequestConverter {
    companion object {
        private val gson = Gson()
    }

    @TypeConverter
    fun stringToLocationLink(string: String): LocationLink? {
        if (TextUtils.isEmpty(string))
            return null
        return gson.fromJson(string, LocationLink::class.java)
    }

    @TypeConverter
    fun locationLinkToString(outboxItem: LocationLink): String {
        return gson.toJson(outboxItem)
    }

    @TypeConverter
    fun stringToOrigin(string: String): Origin? {
        if (TextUtils.isEmpty(string))
            return null
        return gson.fromJson(string, Origin::class.java)
    }

    @TypeConverter
    fun originToString(outboxItem: Origin): String {
        return gson.toJson(outboxItem)
    }

    @TypeConverter
    fun stringToList(string: String): List<String>? {
        if (TextUtils.isEmpty(string))
            return null
        return gson.fromJson(string, List::class.java) as List<String>?
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return gson.toJson(list)
    }
}
