package ru.lazytechwork.qrscanner

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.lazytechwork.qrscanner.fragments.FavouritesFragment
import ru.lazytechwork.qrscanner.fragments.HistoryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navbar: BottomNavigationView = findViewById(R.id.navbar)
        navbar.setOnNavigationItemSelectedListener(navbarListener)
        supportFragmentManager.beginTransaction()
        val fragment = HistoryFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_container, fragment, fragment.javaClass.simpleName)
    }

    private val navbarListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.navitem_history -> {
                    val fragment = HistoryFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.app_container, fragment, fragment.javaClass.simpleName)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navitem_favourites -> {
                    val fragment = FavouritesFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.app_container, fragment, fragment.javaClass.simpleName)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}
