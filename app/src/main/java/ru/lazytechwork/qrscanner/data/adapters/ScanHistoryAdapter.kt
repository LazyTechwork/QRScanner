package ru.lazytechwork.qrscanner.data.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.lazytechwork.qrscanner.components.HistoryItem
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan

class ScanHistoryAdapter(private var dataset: List<Scan>, private val db: AppDatabase) :
    RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder>() {
    class ViewHolder(val historyItem: HistoryItem) : RecyclerView.ViewHolder(historyItem)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScanHistoryAdapter.ViewHolder =
        ViewHolder(HistoryItem(parent.context, db))


    fun updateDataset(scans: List<Scan>) {
        dataset = scans
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewItem = holder.historyItem
        val scan = dataset[position]
        viewItem.setScan(scan)
    }
}