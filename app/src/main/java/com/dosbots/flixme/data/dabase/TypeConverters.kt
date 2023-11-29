package com.dosbots.flixme.data.dabase

import androidx.room.TypeConverter

object DatabaseTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToList(data: String?): List<String>? {
        return data?.split(",")
    }

    @TypeConverter
    @JvmStatic
    fun listToString(data: List<String>?): String? {
        return data?.joinToString(",")
    }
}