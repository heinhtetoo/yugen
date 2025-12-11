package com.yugen.animeapp.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yugen.animeapp.domain.model.Images
import com.yugen.animeapp.domain.model.WatchStatus

class Converters {
    @TypeConverter
    fun toImagesObject(value: String?): Images? {
        if (value == null) return null
        val objectType = object : TypeToken<Images>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromImagesObject(images: Images?): String =
        Gson().toJson(images)

    @TypeConverter
    fun toWatchStatus(value: Int): WatchStatus =
        WatchStatus.entries.find { it.id == value } ?: WatchStatus.PLAN_TO_WATCH

    @TypeConverter
    fun fromWatchStatus(status: WatchStatus): Int = status.id
}