package ru.lazytechwork.qrscanner

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.data.HistoryTypes
import ru.lazytechwork.qrscanner.fragments.FavouritesFragment
import ru.lazytechwork.qrscanner.fragments.HistoryFragment
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("dd.mm.yyyy HH:mm:ss", Locale("ru"))
    }

    private val historyFragment: HistoryFragment = HistoryFragment()
    private val favouritesFragment: FavouritesFragment = FavouritesFragment()
    lateinit var db: AppDatabase
    private val ioScope = CoroutineScope(Dispatchers.IO)

    suspend fun getScans(): List<Scan> =
        db.scansInterface().getAll()

    suspend fun getFavouriteScans(): List<Scan> =
        db.scansInterface().getFavourites()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "ltw_qrscanner")
            .build()

        val navbar: BottomNavigationView = findViewById(R.id.navbar)
        navbar.setOnNavigationItemSelectedListener(navbarListener)
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_container, historyFragment, historyFragment.javaClass.simpleName)
            .commit()
    }

    private val navbarListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.navitem_history -> {
                    val random = Random()
                    val types = HistoryTypes.values()
                    ioScope.launch {
                        db.scansInterface().insertAll(
                            Scan(
                                random.nextInt(),
                                "123",
                                "123",
                                Date(),
                                types[random.nextInt(types.size)],
                                false
                            )
                        )
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.app_container,
                                historyFragment,
                                historyFragment.javaClass.simpleName
                            ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navitem_favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.app_container,
                            favouritesFragment,
                            favouritesFragment.javaClass.simpleName
                        ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}
