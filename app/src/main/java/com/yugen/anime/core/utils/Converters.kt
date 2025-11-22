package com.yugen.anime.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yugen.anime.domain.model.Images

class Converters {
    @TypeConverter
    fun fromImagesString(value: String?): Images? {
        if (value == null) return null
        val objectType = object : TypeToken<Images>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromImagesObject(images: Images?): String =
        Gson().toJson(images)
}