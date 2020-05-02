package ru.lazytechwork.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import ru.lazytechwork.qrscanner.R
import ru.lazytechwork.qrscanner.components.HistoryItem
import ru.lazytechwork.qrscanner.data.HistoryTypes
import ru.lazytechwork.qrscanner.data.ItemScan

class HistoryFragment : Fragment() {
    private lateinit var list: LinearLayout
    private val scans = listOf<ItemScan>(
        ItemScan(
            "Новая ссылка",
            "https://vk.com/ipetrovofficial",
            "02.05.2020 13:47",
            HistoryTypes.LINK
        ),
        ItemScan(
            "Новый текст",
            "Пример текста отсканированного при помощи QRScanner",
            "02.05.2020 13:42",
            HistoryTypes.TEXT
        ),
        ItemScan(
            "Новый контакт",
            "Петров Иван Владимирович <IRuPetrov@gmail.com>",
            "02.05.2020 13:15",
            HistoryTypes.CONTACT
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list = view!!.findViewById(R.id.historylist)
        for (scan in scans) {
            val view: HistoryItem = HistoryItem(view!!.context)
        }
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

}
