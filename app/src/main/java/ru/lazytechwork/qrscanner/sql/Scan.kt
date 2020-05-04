package ru.lazytechwork.qrscanner.sql

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lazytechwork.qrscanner.data.HistoryTypes

@Entity(tableName = "scans")
data class Scan(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") @NonNull var name: String,
    @ColumnInfo(name = "data") @NonNull var data: String,
    @ColumnInfo(name = "date") @NonNull val date: Long,
    @ColumnInfo(name = "type") @NonNull var type: HistoryTypes,
    @ColumnInfo(name = "favourite") @NonNull var isFavourite: Boolean
)