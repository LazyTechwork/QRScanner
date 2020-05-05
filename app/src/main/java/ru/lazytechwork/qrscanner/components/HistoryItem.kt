package ru.lazytechwork.qrscanner.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.history_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.HistoryType
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan


class HistoryItem(
    private val itemContext: Context,
    private val parent: ViewGroup,
    private val db: AppDatabase
) :
    ConstraintLayout(itemContext) {
    private val nameView: TextView
    private val dateView: TextView
    private val dataView: TextView
    private val typeView: ImageView
    private val favouriteSwitch: CheckBox
    private lateinit var scan: Scan
    private val view: View =
        LayoutInflater.from(itemContext).inflate(R.layout.history_item, parent, false)

    init {
        typeView = view.history_type
        dateView = view.history_date
        nameView = view.history_name
        dataView = view.history_data
        favouriteSwitch = view.favourite_switch
        favouriteSwitch.setOnCheckedChangeListener(FavouriteSwitcher(db, this))
    }


    fun setScan(scan: Scan) {
        this.scan = scan
        this.setFavouriteSwitch(scan.isFavourite)
        this.setHistoryType(scan.type)
        this.setName(scan.name)
        this.setData(scan.data)
        this.setDate(MainActivity.DATE_FORMAT.format(scan.date))
    }

    fun getScan(): Scan = this.scan

    private fun setHistoryType(type: HistoryType) = typeView.setImageResource(
        when (type) {
            HistoryType.LINK -> R.drawable.ic_link_outline
            HistoryType.TEXT -> R.drawable.ic_text_outline
            HistoryType.CONTACT -> R.drawable.ic_contact_outline
        }
    )

    private fun setName(name: String) {
        nameView.text = name
    }

    private fun setData(data: String) {
        dataView.text = data
    }

    private fun setDate(date: String) {
        dateView.text = date
    }

    private fun setFavouriteSwitch(switch: Boolean) {
        favouriteSwitch.isChecked = switch
    }
}

class FavouriteSwitcher(private val db: AppDatabase, private val historyItem: HistoryItem) :
    CompoundButton.OnCheckedChangeListener {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        ioScope.launch {
            if (isChecked)
                db.scansInterface().makeFavourite(historyItem.getScan())
            else
                db.scansInterface().removeFavourite(historyItem.getScan())
        }
    }
}