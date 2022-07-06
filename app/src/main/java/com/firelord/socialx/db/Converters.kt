package com.firelord.socialx.db

import androidx.room.TypeConverter
import com.firelord.socialx.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source):String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name,name)
    }
}