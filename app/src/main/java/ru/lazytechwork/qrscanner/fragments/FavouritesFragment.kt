package ru.lazytechwork.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.components.HistoryItem
import ru.lazytechwork.qrscanner.sql.Scan

class FavouritesFragment : Fragment() {
    private lateinit var list: LinearLayout
    private lateinit var scans: List<Scan>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        list = view!!.findViewById(R.id.favouriteslist)
        scans = (activity as MainActivity).getFavouriteScans()
        for (scan in scans)
            list.addView(HistoryItem(view.context, scan, (activity as MainActivity).db))
        return view
    }

}
