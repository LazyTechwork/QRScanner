package ru.lazytechwork.qrscanner.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_scan.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.ScanType
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan

class ScanItemsAdapter(val db: AppDatabase) :
    RecyclerView.Adapter<ScanItemsAdapter.ScanViewHolder>() {
    var items: List<Scan> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ScanViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_scan,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ScanViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        fun bind(positon: Int) {
            val scan = items[positon]
            with(itemView) {
                history_name.text = scan.name
                history_date.text = MainActivity.DATE_FORMAT.format(scan.date)
                history_data.text = scan.data
                history_type.setImageResource(
                    when (scan.type) {
                        ScanType.LINK -> R.drawable.ic_link_outline
                        ScanType.TEXT -> R.drawable.ic_text_outline
                        ScanType.CONTACT -> R.drawable.ic_contact_outline
                    }
                )
                favourite_switch.apply {
                    isChecked = scan.isFavourite
                    setOnCheckedChangeListener(FavouriteSwitcher(db, scan))
                }
            }
        }
    }

    inner class FavouriteSwitcher(private val db: AppDatabase, private val scan: Scan) :
        CompoundButton.OnCheckedChangeListener {
        private val ioScope = CoroutineScope(Dispatchers.IO)
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            ioScope.launch {
                if (isChecked)
                    db.scansInterface().makeFavourite(scan)
                else
                    db.scansInterface().removeFavourite(scan)
            }
        }
    }
}