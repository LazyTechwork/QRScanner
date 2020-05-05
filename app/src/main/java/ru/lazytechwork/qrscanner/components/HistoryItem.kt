package ru.lazytechwork.qrscanner.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lazytechwork.qrscanner.MainActivity
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.HistoryType
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan
import java.util.logging.Logger


class HistoryItem(
    private val parent: ViewGroup,
    private val db: AppDatabase
) :
    ConstraintLayout(parent.context) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
    private val view: View =
        layoutInflater.inflate(R.layout.history_item, parent, false)
    private val nameView: TextView = view.findViewById<TextView>(R.id.history_name)
    private val dateView: TextView = view.findViewById<TextView>(R.id.history_date)
    private val dataView: TextView = view.findViewById<TextView>(R.id.history_data)
    private val typeView: ImageView = view.findViewById<ImageView>(R.id.history_type)
    private val favouriteSwitch: CheckBox = view.findViewById<CheckBox>(R.id.favourite_switch)
    private lateinit var scan: Scan

    init {
        favouriteSwitch.setOnCheckedChangeListener(FavouriteSwitcher(db, this))
    }

    fun setScan(scan: Scan) {
        this.scan = scan
        this.setFavouriteSwitch(scan.isFavourite)
        this.setHistoryType(scan.type)
        this.setName(scan.name)
        this.setData(scan.data)
        this.setDate(MainActivity.DATE_FORMAT.format(scan.date))
        Logger.getLogger("HistoryItem").info(scan.toString())
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