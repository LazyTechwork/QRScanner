package ru.lazytechwork.qrscanner.sql

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Scan::class], version = 1)
@TypeConverters(SQLTypes::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scansInterface(): ScanSQLInterface
}