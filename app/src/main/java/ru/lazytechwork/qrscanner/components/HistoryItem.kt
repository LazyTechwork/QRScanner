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
import ru.lazytechwork.qrscanner.data.HistoryTypes
import ru.lazytechwork.qrscanner.sql.AppDatabase
import ru.lazytechwork.qrscanner.sql.Scan


class HistoryItem : ConstraintLayout {
    private val nameView: TextView
    private val dateView: TextView
    private val dataView: TextView
    private val typeView: ImageView
    private val db: AppDatabase
    private val scan: Scan

    constructor(context: Context, scan: Scan, db: AppDatabase) : super(context) {
        this.db = db
        this.scan = scan

        inflate(context, R.layout.history_item, this)

        typeView = findViewById(R.id.history_type)
        dateView = findViewById(R.id.history_date)
        nameView = findViewById(R.id.history_name)
        dataView = findViewById(R.id.history_data)

        favourite_switch.isChecked = scan.isFavourite
        favourite_switch.setOnCheckedChangeListener(FavouriteSwitcher(db, scan))

        setHistoryType(scan.type)
        nameView.text = scan.name
        dataView.text = scan.data
        dateView.text = MainActivity.DATE_FORMAT.format(scan.date)
    }

    private fun setHistoryType(type: HistoryTypes) = typeView.setImageResource(
        when (type) {
            HistoryTypes.LINK -> R.drawable.ic_link_outline
            HistoryTypes.TEXT -> R.drawable.ic_text_outline
            HistoryTypes.CONTACT -> R.drawable.ic_contact_outline
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
}

class FavouriteSwitcher(private val db: AppDatabase, private val scan: Scan) :
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