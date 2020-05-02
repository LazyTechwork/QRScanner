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
import java.util.logging.Logger

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
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        list = view!!.findViewById(R.id.historylist)
        for (scan in scans) {
            list.addView(HistoryItem(view.context, scan))
        }
        Logger.getLogger("QRS").info("Container not null")
        return view
    }

}
