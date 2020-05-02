package ru.lazytechwork.qrscanner.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleableRes
import ru.lazytechwork.qrscanner.R


class HistoryItem : LinearLayout {

    @StyleableRes
    val index0: Int = 0

    @StyleableRes
    val index1: Int = 0

    @StyleableRes
    val index2: Int = 0

    @StyleableRes
    val index3: Int = 0

    lateinit var nameView: TextView
    lateinit var dateView: TextView
    lateinit var dataView: TextView
    lateinit var typeView: ImageView

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private fun init(context: Context, attributeSet: AttributeSet) {
        inflate(context, R.layout.history_item, this)
        val sets =
            intArrayOf(
                R.attr.historyName,
                R.attr.historyDate,
                R.attr.historyData,
                R.attr.historyType
            )
        val typedArray: TypedArray = context.obtainStyledAttributes(attributeSet, sets)
        val name = typedArray.getText(index0)
        val date = typedArray.getText(index1)
        val data = typedArray.getText(index2)
        val type = typedArray.getText(index3)
        typedArray.recycle()

        initComponents()
        setHistoryType(type)
        nameView.text = name
        dateView.text = date
        dataView.text = data
    }

    private fun initComponents() {
        typeView = findViewById(R.id.history_type)
        dateView = findViewById(R.id.history_date)
        nameView = findViewById(R.id.history_name)
        dataView = findViewById(R.id.history_data)
    }

    private fun setHistoryType(type: CharSequence) = typeView.setImageResource(
        when (HistoryTypes.valueOf(type.toString())) {
            HistoryTypes.LINK -> R.drawable.ic_link_outline
            HistoryTypes.TEXT -> R.drawable.ic_text_outline
            HistoryTypes.CONTACT -> R.drawable.ic_contact_outline
        }
    )
}