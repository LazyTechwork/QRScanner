package ru.lazytechwork.qrscanner.sql

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Scan(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean
)