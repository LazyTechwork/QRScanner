package ru.lazytechwork.qrscanner.data

import ezvcard.Ezvcard
import ru.lazytechwork.qrscanner.sql.Scan
import java.util.*
import kotlin.random.Random

object ScanParser {
    fun parseScan(scanData: String): Scan {
        if (scanData.startsWith("BEGIN:VCARD")) {
            val vcard = Ezvcard.parse(scanData).first()
            val name = vcard.structuredName
            return Scan(
                Random.nextInt(),
                "${name.given} ${name.family}",
                "Откройте сканирование для получения всех подробностей контакта",
                scanData,
                Date(),
                ScanType.CONTACT,
                false
            )
        }
        return Scan(
            Random.nextInt(),
            "Новое сканирование",
            scanData,
            scanData,
            Date(),
            ScanType.TEXT,
            false
        )

    }
}