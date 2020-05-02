package ru.lazytechwork.qrscanner.components

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.data.HistoryTypes
import ru.lazytechwork.qrscanner.data.ItemScan


class HistoryItem : ConstraintLayout {
    private val nameView: TextView
    private val dateView: TextView
    private val dataView: TextView
    private val typeView: ImageView

    constructor(context: Context, scan: ItemScan) : super(context) {
        inflate(context, R.layout.history_item, this)

        typeView = findViewById(R.id.history_type)
        dateView = findViewById(R.id.history_date)
        nameView = findViewById(R.id.history_name)
        dataView = findViewById(R.id.history_data)

        setHistoryType(scan.type)
        nameView.text = scan.name
        dataView.text = scan.data
        dateView.text = scan.date
    }

    fun setHistoryType(type: HistoryTypes) = typeView.setImageResource(
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