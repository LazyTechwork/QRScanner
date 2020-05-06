package ru.lazytechwork.qrscanner.sql

import androidx.room.TypeConverter
import ru.lazytechwork.qrscanner.data.ScanType
import java.util.*

class SQLTypes {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time


    @TypeConverter
    fun historyTypeToInt(type: ScanType): Int = type.ordinal

    @TypeConverter
    fun intToHistoryType(typeOrdinal: Int): ScanType = ScanType.values()[typeOrdinal]
}