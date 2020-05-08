package ru.lazytechwork.qrscanner.data

import ezvcard.Ezvcard
import ru.lazytechwork.qrscanner.sql.Scan
import java.util.*
import kotlin.random.Random

object ScanParser {
    fun parseScan(scanData: String): Scan {
        if (scanData.startsWith("BEGIN:VCARD")) {
            val vcard = Ezvcard.parse(scanData).first()
            return Scan(
                Random.nextInt(),
                vcard.formattedName.value,
                vcard.write(),
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