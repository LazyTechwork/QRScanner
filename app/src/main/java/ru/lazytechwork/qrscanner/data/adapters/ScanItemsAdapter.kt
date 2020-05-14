package ru.lazytechwork.qrscanner.data.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ezvcard.Ezvcard
import kotlinx.android.synthetic.main.item_scan.view.*
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.ScanType
import ru.lazytechwork.qrscanner.data.cache.CacheMaster
import ru.lazytechwork.qrscanner.data.parser.IntentMaker
import ru.lazytechwork.qrscanner.sql.Scan

class ScanItemsAdapter : RecyclerView.Adapter<ScanItemsAdapter.ScanViewHolder> {
    var items: ArrayList<Scan>
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val getFreshData: () -> ArrayList<Scan>
    private lateinit var recyclerView: RecyclerView
    private var parent: ViewGroup? = null
    private var toastView: View? = null
    private val mainActivity: MainActivity

    constructor(freshDataFunction: () -> ArrayList<Scan>, activity: MainActivity) {
        this.getFreshData = freshDataFunction
        this.items = getFreshData()
        this.mainActivity = activity
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        this.parent = parent
        this.toastView = LayoutInflater.from(parent.context).inflate(
            R.layout.toast_favouriteadded,
            parent.findViewById(R.id.toast_favouriteadded_container)
        )
        return ScanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scan, parent, false)
        )
    }

    private fun callToast(resId: Int) =
        this.parent?.context?.getString(resId)?.let { callToast(it) }


    private fun callToast(customText: String) =
        if (toastView != null && parent != null) {
            toastView!!.findViewById<TextView>(R.id.toast_text).text = customText
            Toast(parent!!.context).apply {
                view = toastView
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 25)
                show()
            }
        } else null

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = items.size


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
                            callToast(if (isChecked) R.string.toast_favouriteadded else R.string.toast_favouriteremoved)
                        }
                    }
                }

                edit_button.setOnClickListener {
                    mainActivity.loadFloatingFragment(mainActivity.editFragment)
                }

                delete_button.setOnClickListener {
                    CacheMaster.removeScan(scan.id)
                    recyclerView.post {
                        items = getFreshData()
                    }
                }

                share_button.setOnClickListener {
                    when (scan.type) {
                        ScanType.CONTACT -> view.context.applicationContext.startActivity(
                            IntentMaker.fromContact(Ezvcard.parse(scan.rawData).first())
                        )
                        ScanType.LINK -> view.context.applicationContext.startActivity(
                            IntentMaker.fromLink(
                                scan.rawData
                            )
                        )
                        else -> return@setOnClickListener
                    }
                }
            }
        }
    }
}