package com.example.rickandmortygo.data.database

import android.text.TextUtils
import androidx.room.TypeConverter
import com.example.rickandmortygo.data.model.LocationLink
import com.example.rickandmortygo.data.model.Origin
import com.google.gson.Gson

class RequestConverter {
    companion object {
        private val gson = Gson()

        @TypeConverter
        fun stringToLocationLink(string: String): LocationLink? {
            if (TextUtils.isEmpty(string))
                return null
            return gson.fromJson(string, LocationLink::class.java)
        }

        @TypeConverter
        private fun locationLinkToString(outboxItem: LocationLink): String {
            return gson.toJson(outboxItem)
        }

        @TypeConverter
        fun stringToOrigin(string: String): Origin? {
            if (TextUtils.isEmpty(string))
                return null
            return gson.fromJson(string, Origin::class.java)
        }

        @TypeConverter
        private fun originToString(outboxItem: Origin): String {
            return gson.toJson(outboxItem)
        }
    }
}
