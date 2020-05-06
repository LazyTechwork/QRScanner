package ru.lazytechwork.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.adapters.ScanItemsAdapter

class FavouritesFragment : Fragment() {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var recyclerView: RecyclerView
    private lateinit var scanAdapter: ScanItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        scanAdapter = ScanItemsAdapter((activity as MainActivity).db)
        recyclerView = view.findViewById<RecyclerView>(R.id.scanlist).apply {
            setHasFixedSize(true)
            adapter = scanAdapter
        }

        ioScope.launch {
            scanAdapter.items = (activity as MainActivity).getFavouriteScans()
        }
        return view
    }

}
