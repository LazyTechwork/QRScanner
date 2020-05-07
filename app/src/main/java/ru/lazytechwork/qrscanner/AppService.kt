package ru.lazytechwork.qrscanner

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.lazytechwork.qrscanner.data.cache.CacheMaster

class AppService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onCreate() {
        super.onCreate()
        CacheMaster.initializeCache(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        CacheMaster.destroyCache()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        CacheMaster.destroyCache()
    }
}