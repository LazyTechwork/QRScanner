package ru.lazytechwork.qrscanner.sql

import androidx.room.*

@Dao
interface ScanSQLInterface {
    @Query("SELECT * FROM scans")
    fun getAll(): List<Scan>

    @Query("SELECT * FROM scans WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Scan>

    @Query("SELECT * FROM scans WHERE favourite = 1")
    fun getFavourites(): List<Scan>

    @Update
    fun update(scan: Scan)

    @Transaction
    fun makeFavourite(scan: Scan) {
        scan.isFavourite = true
        update(scan)
    }

    @Transaction
    fun removeFavourite(scan: Scan) {
        scan.isFavourite = false
        update(scan)
    }

    @Insert
    fun insertAll(vararg scans: Scan)

    @Delete
    fun delete(scan: Scan)
}