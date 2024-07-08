package com.test.filmstest.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromStringList(value: String?): List<String>? {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(list: List<String>?): String {
        return gson.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromIntList(value: String?): List<Int>? {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun toIntList(list: List<Int>?): String {
        return gson.toJson(list)
    }
}
