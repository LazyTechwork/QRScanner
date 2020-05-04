package ru.lazytechwork.qrscanner.sql

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Scan::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scansInterface(): ScanSQLInterface
}