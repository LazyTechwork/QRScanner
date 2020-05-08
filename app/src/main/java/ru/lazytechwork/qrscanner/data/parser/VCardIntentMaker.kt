package ru.lazytechwork.qrscanner.data.parser

import android.content.Intent
import android.provider.ContactsContract
import ezvcard.VCard

object VCardIntentMaker {
    fun createIntentFromVCard(vCard: VCard) = Intent(ContactsContract.Intents.Insert.ACTION).apply {
        type = ContactsContract.RawContacts.CONTENT_TYPE
        val name = vCard.structuredName
        val fullname = ArrayList<String>()
        if (name.given != null)
            fullname.add(name.given)
        if (name.family != null)
            fullname.add(name.family)
        if (name.prefixes.size > 0) {
            val prefixes = name.prefixes.reversed()
            for (prefix in prefixes)
                fullname.add(0, prefix)
        }
        if (name.suffixes.size > 0) {
            val suffixes = name.suffixes.reversed()
            for (suffix in suffixes)
                fullname.add(0, suffix)
        }
        if (fullname.size > 0)
            putExtra(ContactsContract.Intents.Insert.NAME, fullname.joinToString(" "))
        val email = if (vCard.emails.size > 0) vCard.emails[0].value else null
        if (email != null) {
            putExtra(ContactsContract.Intents.Insert.EMAIL, email)
            putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM)
            putExtra(ContactsContract.Intents.Insert.EMAIL_ISPRIMARY, true)
        }
        val phone = if (vCard.telephoneNumbers.size > 0) vCard.telephoneNumbers[0].text else null
        if (phone != null) {
            putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
            putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, true)
        }
        val organization = if (vCard.organization.values.size > 0) vCard.organization.values[0] else null
        if (organization != null)
            putExtra(ContactsContract.Intents.Insert.COMPANY, organization)
        val job = if (vCard.titles.size > 0) vCard.titles[0].value else null
        if (job != null)
            putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
}