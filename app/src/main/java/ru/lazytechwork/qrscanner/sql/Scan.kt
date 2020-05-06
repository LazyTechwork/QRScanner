package ru.lazytechwork.qrscanner.sql

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lazytechwork.qrscanner.data.ScanType
import java.util.*

@Entity(tableName = "scans")
data class Scan(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") @NonNull var name: String,
    @ColumnInfo(name = "data") @NonNull var data: String,
    @ColumnInfo(name = "date") @NonNull val date: Date,
    @ColumnInfo(name = "type") @NonNull var type: ScanType,
    @ColumnInfo(name = "favourite") @NonNull var isFavourite: Boolean
) {
    override fun toString() = "Scan [ID: $id]: $name / $data (Type: $type)"
}