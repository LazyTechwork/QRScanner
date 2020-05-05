package ru.lazytechwork.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.adapters.ScanHistoryAdapter
import ru.lazytechwork.qrscanner.sql.Scan

class HistoryFragment : Fragment() {
    private var scans: List<Scan> = emptyList()
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ScanHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        viewAdapter = ScanHistoryAdapter(scans, (activity as MainActivity).db)
        recyclerView = view.findViewById<RecyclerView>(R.id.scanlist).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        ioScope.launch {
            scans = (activity as MainActivity).getScans()
            viewAdapter.updateDataset(scans)
        }
        return view
    }

}
