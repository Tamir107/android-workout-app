package com.hit.gymtime.ui.contacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactHelper {

    suspend fun getContacts(context: Context) : List<Contact> {
        return withContext(Dispatchers.IO) {
            val cursor = getContactCursor(context)
            val contacts = resolveContactDataFromCursor(cursor)

            cursor?.close()

            contacts
        }
    }

    private fun getContactCursor(context: Context) : Cursor? {

        return context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            ContactsContract.Data.HAS_PHONE_NUMBER + "!=0 AND(" + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?)"
            , arrayOf(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE),
            ContactsContract.Data.DISPLAY_NAME

        )
    }

    private fun resolveContactDataFromCursor(cursor: Cursor?) : List<Contact> {
        val contacts = arrayListOf<Contact>()

        if(cursor != null && cursor.count > 0 && cursor.moveToFirst()){
            do {

                val contactId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID))

                val contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME_PRIMARY))

                val data1 = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DATA1))

                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE))

                contacts.addContact(contactId, contactName, data1, mimeType)

            }while (cursor.moveToNext())
        }
        return contacts
    }

    private fun ArrayList<Contact>.addContact(
        contactId : Int,
        contactName : String,
        data1 : String,
        mimeType : String
    ) {
        if (data1 != null) {
            val contact = Contact()

            when(mimeType) {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> contact.email = data1
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> contact.phoneNumber = data1
            }

            contact.id = contactId
            contact.name = contactName

            add(contact)
        }
    }
}