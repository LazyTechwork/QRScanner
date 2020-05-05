package ru.lazytechwork.qrscanner.components

import android.content.Context
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


class HistoryItem : ConstraintLayout {
    private val nameView: TextView
    private val dateView: TextView
    private val dataView: TextView
    private val typeView: ImageView
    private val db: AppDatabase
    private lateinit var scan: Scan

    constructor(context: Context, db: AppDatabase) : super(context) {
        this.db = db

        inflate(context, R.layout.history_item, this)

        typeView = findViewById(R.id.history_type)
        dateView = findViewById(R.id.history_date)
        nameView = findViewById(R.id.history_name)
        dataView = findViewById(R.id.history_data)

        favourite_switch.setOnCheckedChangeListener(FavouriteSwitcher(db, this))
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

    fun setHistoryType(type: HistoryType) = typeView.setImageResource(
        when (type) {
            HistoryType.LINK -> R.drawable.ic_link_outline
            HistoryType.TEXT -> R.drawable.ic_text_outline
            HistoryType.CONTACT -> R.drawable.ic_contact_outline
        }
    )

    fun setName(name: String) {
        nameView.text = name
    }

    fun setData(data: String) {
        dataView.text = data
    }

    fun setDate(date: String) {
        dateView.text = date
    }

    fun setFavouriteSwitch(switch: Boolean) {
        favourite_switch.isChecked = switch
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