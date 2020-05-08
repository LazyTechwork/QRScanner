package ru.lazytechwork.qrscanner.data.cache

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan
import java.util.logging.Logger

object CacheMaster {
    private val scans: ArrayList<Scan> = ArrayList()
    private val modifiedScans: ArrayList<Scan> = ArrayList()
    private val favouriteScans: ArrayList<Scan> = ArrayList()
    private val newScans: ArrayList<Scan> = ArrayList()
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val cacheThread: HandlerThread = HandlerThread("Cache Master")
    private val cacheHandler: Handler
    private const val cacheInterval: Long = 60_000L
    private lateinit var db: AppDatabase
    private val logger: Logger = Logger.getLogger("Cache Master")

    init {
        cacheThread.start()
        cacheHandler = Handler(cacheThread.looper)
    }

    fun initializeCache(applicationContext: Context) {
        ioScope.launch {
            db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "ltw_qrscanner")
                .build()
            logger.info("Initializing cache")
            scans.addAll(db.scansInterface().getAll())
            logger.info("Cache initialized")
            for (scan in scans)
                if (scan.isFavourite)
                    favouriteScans.add(scan)
            logger.info("Cache formed favourites list")

            cacheHandler.postDelayed(object : Runnable {
                override fun run() {
                    syncCache()
                    cacheHandler.postDelayed(this, cacheInterval)
                }
            }, cacheInterval)

            logger.info("Cache handler started")
        }
    }

    fun makeFavourite(i: Int): ArrayList<Scan> {
        val scan = getScan(i)
        scan.isFavourite = true
        favouriteScans.add(scan)
        saveScan(scan)
        return scans
    }

    fun removeFavourite(i: Int): ArrayList<Scan> {
        val scan = getScan(i)
        scan.isFavourite = false
        favouriteScans.remove(favouriteScans.find { it.id == i })
        saveScan(scan)
        return scans
    }

    fun changeName(i: Int, name: String): ArrayList<Scan> {
        val scan = getScan(i)
        scan.name = name
        saveScan(scan)
        return scans
    }

    private fun getScan(i: Int): Scan = scans.find { it.id == i }!!
    fun getScans() = scans
    fun getFavouriteScans() = favouriteScans

    fun isDirty() = modifiedScans.size > 0 || newScans.size > 0

    private fun saveScan(scan: Scan): ArrayList<Scan> {
        val mScan: Scan? = modifiedScans.find { it.id == scan.id }
        if (mScan != null) mScan.apply {
            name = scan.name
            data = scan.data
            type = scan.type
        }
        else modifiedScans.add(scan)
        scans.sortBy { -it.date.time }
        return scans
    }

    fun newScan(scan: Scan): ArrayList<Scan> {
        scans.add(scan)
        newScans.add(scan)
        return scans
    }

    fun syncCache() {
        logger.info("Started synchronization with database")
        ioScope.launch {
            if (modifiedScans.size != 0) {
                db.scansInterface().updateAll(*modifiedScans.toTypedArray())
                modifiedScans.clear()
            }

            if (newScans.size != 0) {
                db.scansInterface().insertAll(*newScans.toTypedArray())
                newScans.clear()
            }
            logger.info("Successfull synchronization with database")
        }
    }

    fun destroyCache() {
        syncCache()
        cacheThread.quit()
        ioScope.launch {
            logger.info("Closing access to database")
            db.close()
        }
    }
}