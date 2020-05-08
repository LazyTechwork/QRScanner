package ru.lazytechwork.qrscanner.data.adapters

import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_scan.view.*
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.ScanType
import ru.lazytechwork.qrscanner.data.cache.CacheMaster
import ru.lazytechwork.qrscanner.sql.Scan

class ScanItemsAdapter : RecyclerView.Adapter<ScanItemsAdapter.ScanViewHolder> {
    var items: ArrayList<Scan>
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val getFreshData: () -> ArrayList<Scan>
    private lateinit var recyclerView: RecyclerView

    constructor(freshDataFunction: () -> ArrayList<Scan>) {
        this.getFreshData = freshDataFunction
        this.items = getFreshData()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ScanViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_scan,
            parent,
            false
        )
    )

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ScanViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @ExperimentalStdlibApi
        fun bind(itemPosition: Int) {
            val scan = items[itemPosition]

            with(itemView) {
                history_name.text = scan.name
                history_date.text = MainActivity.DATE_FORMAT.format(scan.date)
                history_data.text = scan.data

                history_type.setImageResource(
                    when (scan.type) {
                        ScanType.LINK -> R.drawable.ic_link_outline
                        ScanType.TEXT -> R.drawable.ic_text_outline
                        ScanType.CONTACT -> R.drawable.ic_contact_outline
                        ScanType.GEOLOCATION -> R.drawable.ic_geolocation_outline
                        ScanType.WIFI -> R.drawable.ic_wifi_outline
                    }
                )

                favourite_switch.apply {
                    isChecked = scan.isFavourite
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked)
                            CacheMaster.makeFavourite(scan.id)
                        else
                            CacheMaster.removeFavourite(scan.id)
                        recyclerView.post {
                            items = getFreshData()
                        }
                    }
                }

                delete_button.setOnClickListener {
                    CacheMaster.removeScan(scan.id)
                    recyclerView.post {
                        items = getFreshData()
                    }
                }

                share_button.setOnClickListener {
                    if (scan.type == ScanType.CONTACT) {
                        view.context.applicationContext.startActivity(Intent().apply {
                            action = Intent.ACTION_VIEW
                            setDataAndType(
                                Uri.parse(
                                    "data:text/x-vcard;base64," + Base64.encodeToString(
                                        scan.rawData.encodeToByteArray(),
                                        Base64.DEFAULT
                                    )
                                ), "text/x-vcard"
                            )
                        })
                    }
                }
            }
        }
    }
}