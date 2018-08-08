package com.marlonmoorer.streamkast.data

import android.arch.persistence.room.TypeConverter

import java.text.SimpleDateFormat
import java.util.*


class Converters{

    companion object {
       val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }
    @TypeConverter
    fun fromTimeStamp(value:String?): Date {
        return dateFormat.parse(value)
    }

    @TypeConverter
    fun fromDate(value:Date?): String? {
        value?.let {  return dateFormat.format(value) }
        return null
    }
}
