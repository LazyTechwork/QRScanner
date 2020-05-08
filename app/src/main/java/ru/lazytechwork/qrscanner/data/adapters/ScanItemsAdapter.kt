package ru.lazytechwork.qrscanner.data.adapters

import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ezvcard.Ezvcard
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
                        val vCard = Ezvcard.parse(scan.rawData).first()
                        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                            type = ContactsContract.RawContacts.CONTENT_TYPE
                            val name = vCard.structuredName
                            val fullname = ArrayList<String>()
                            if (name.given != null)
                                fullname.add(name.given)
                            if (name.family != null)
                                fullname.add(name.family)
                            if (name.prefixes.size > 0) {
                                val prefixes = name.prefixes.reversed()
                                for (prefix in prefixes)
                                    fullname.add(0, prefix)
                            }
                            if (name.suffixes.size > 0) {
                                val suffixes = name.suffixes.reversed()
                                for (suffix in suffixes)
                                    fullname.add(0, suffix)
                            }
                            if (fullname.size > 0)
                                putExtra(ContactsContract.Intents.Insert.NAME, fullname.joinToString(" "))
                            val email = if (vCard.emails.size > 0) vCard.emails[0].value else null
                            if (email != null) {
                                putExtra(ContactsContract.Intents.Insert.EMAIL, email)
                                putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM)
                                putExtra(ContactsContract.Intents.Insert.EMAIL_ISPRIMARY, true)
                            }
                            val phone = if (vCard.telephoneNumbers.size > 0) vCard.telephoneNumbers[0].text else null
                            if (phone != null) {
                                putExtra(ContactsContract.Intents.Insert.PHONE, phone)
                                putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
                                putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, true)
                            }
                            val organization = if (vCard.organization.values.size > 0) vCard.organization.values[0] else null
                            if (organization != null)
                                putExtra(ContactsContract.Intents.Insert.COMPANY, organization)
                            val job = if (vCard.titles.size > 0) vCard.titles[0].value else null
                            if (job != null)
                                putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job)
                        }
                        view.context.applicationContext.startActivity(intent)
                    }
                }
            }
        }
    }
}