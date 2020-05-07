package ru.lazytechwork.qrscanner.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan

object CacheMaster {
    lateinit var scans: List<Scan>
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase

    fun initializeCache(applicationContext: Context) {
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "ltw_qrscanner").build()
        ioScope.launch {

        }
    }

    fun syncCache() {

    }

    fun destroyCache() {
        db.close()
    }
}