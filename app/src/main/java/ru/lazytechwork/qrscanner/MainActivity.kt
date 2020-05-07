package ru.lazytechwork.qrscanner

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.lazytechwork.qrscanner.data.cache.CacheMaster
import ru.lazytechwork.qrscanner.fragments.FavouritesFragment
import ru.lazytechwork.qrscanner.fragments.HistoryFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("ru"))
    }

    private val historyFragment: HistoryFragment = HistoryFragment()
    private val favouritesFragment: FavouritesFragment = FavouritesFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navbar: BottomNavigationView = findViewById(R.id.navbar)
        navbar.setOnNavigationItemSelectedListener(navbarListener)
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_container, historyFragment)
            .commit()
        startService(Intent(this, AppService::class.java))
    }

    override fun onPause() {
        super.onPause()
        CacheMaster.syncCache()
    }

    private val navbarListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.navitem_history -> {
//                    val random = Random()
//                    val types = ScanType.values()
//                    ioScope.launch {
//                        db.scansInterface().insertAll(
//                            Scan(
//                                random.nextInt(),
//                                "123",
//                                "123",
//                                Date(),
//                                types[random.nextInt(types.size)],
//                                false
//                            )
//                        )
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.app_container,
                            historyFragment
                        ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
//                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navitem_favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.app_container,
                            favouritesFragment
                        ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}
