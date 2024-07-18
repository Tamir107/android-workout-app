package com.hit.gymtime.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hit.gymtime.data.LocationUpdatesLiveData
import com.hit.gymtime.data.models.Item
import com.hit.gymtime.data.repository.itemRepository
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = itemRepository(application)

    val items : LiveData<List<Item>>? = repository.getItems()

    val location : LiveData<String> = LocationUpdatesLiveData(application.applicationContext)

    val addresses = mapOf("Private Gym - Holon" to "Giv'at HaTahmoshet St 27, Holon",
        "Crossfit - Holon" to "Ha-Melakha St 18, Holon",
        "ICON - Holon" to "Rehov HaMerkava 38, Holon",
        "Space - Holon" to "Harokmim St 26, Holon")

    private val _chosenItem = MutableLiveData<Item>()
    val chosenItem : LiveData<Item> get() = _chosenItem

    fun setItem(item: Item) {
        _chosenItem.value = item
    }
    fun addItem(item: Item){
        viewModelScope.launch {
            repository.addItem(item)
        }
    }

    fun deleteItem(item: Item){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}