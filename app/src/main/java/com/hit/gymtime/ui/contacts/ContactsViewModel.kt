package com.hit.gymtime.ui.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class ContactsViewModel(application: Application) : AndroidViewModel(application){

    private val contactsHelper = ContactHelper()

    val contacts : LiveData<ContactResult> = liveData(Dispatchers.IO) {

        emit(ContactResult.Loading)

        val contactsList = contactsHelper.getContacts(getApplication())

        if(contactsList.isEmpty()){
            emit(ContactResult.Failure("No Contacts Found"))
        }else{
            emit(ContactResult.Success(contactsList))
        }

    }

}

sealed class ContactResult {
    data class Success(val contacts : List<Contact>) : ContactResult()
    data class Failure(val message : String) : ContactResult()
    object Loading : ContactResult()
}