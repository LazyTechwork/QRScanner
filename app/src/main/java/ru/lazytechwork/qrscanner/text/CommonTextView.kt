package ru.lazytechwork.qrscanner.text

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class CommonTextView : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )
}