package com.nvalenti.journalite.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object: TypeToken<T>() {}.type)

class Converters {
    @TypeConverter
    fun fromStringToDayOfWeekSet(value: Set<DayOfWeek>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toDayOfWeekSetToString(value: String): Set<DayOfWeek> {
        return try {
            Gson().fromJson<Set<DayOfWeek>>(value)
        } catch (e: Exception) {
            setOf()
        }
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? {
        return if (value == null) {
            null
        } else {
            LocalDateTime.parse(value)
        }
    }

    @TypeConverter
    fun localDateTimeToString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun stringToLocalTime(value: String?): LocalTime? {
        return if (value == null) {
            null
        } else {
            LocalTime.parse(value)
        }
    }

    @TypeConverter
    fun localTimeToString(time: LocalTime?): String? {
        return time?.toString()
    }
}