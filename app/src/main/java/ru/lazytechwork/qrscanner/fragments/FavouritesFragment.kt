package ru.lazytechwork.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.adapters.ScanItemsAdapter
import ru.lazytechwork.qrscanner.data.cache.CacheMaster

class FavouritesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scanAdapter: ScanItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        scanAdapter =
            ScanItemsAdapter(
                freshDataFunction = { CacheMaster.getFavouriteScans() },
                activity = activity as MainActivity
            )
        recyclerView = view.findViewById<RecyclerView>(R.id.scanlist).apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            adapter = scanAdapter
        }

        return view
    }

}
