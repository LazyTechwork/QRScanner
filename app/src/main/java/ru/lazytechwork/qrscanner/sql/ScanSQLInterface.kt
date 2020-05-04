package ru.lazytechwork.qrscanner.sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanSQLInterface {
    @Query("SELECT * FROM scans")
    fun getAll(): List<Scan>

    @Query("SELECT * FROM scans WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Scan>

    @Query("SELECT * FROM scans WHERE is_favourite = true")
    fun getFavourites(): List<Scan>

    @Insert
    fun insertAll(vararg scans: Scan)

    @Delete
    fun delete(scan: Scan)
}