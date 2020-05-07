package ru.lazytechwork.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.solver.Cache
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.adapters.ScanItemsAdapter
import ru.lazytechwork.qrscanner.data.cache.CacheMaster
import java.util.logging.Logger

class FavouritesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scanAdapter: ScanItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        scanAdapter = ScanItemsAdapter(CacheMaster.getFavouriteScans())
        recyclerView = view.findViewById<RecyclerView>(R.id.scanlist).apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            adapter = scanAdapter
        }

        return view
    }

}
