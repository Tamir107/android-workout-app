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

    val workoutTypes = arrayOf("Cardio", "Strength", "Flexibility", "Balance")

    //val workoutLocations = arrayOf("Private Gym - Holon" , "Crossfit - Holon", "ICON - Holon", "Space - Holon")

    val gymsHolon = arrayOf("Private Gym - Holon" , "Crossfit - Holon", "ICON - Holon", "Space - Holon")
    val gymsTelAviv = arrayOf("ICON - Tel Aviv" , "Crossfit - Tel Aviv", "Gordon Gym - Tel Aviv", "Profit - Tel Aviv")
    val gymsBatYam = arrayOf("Profit - Bat Yam" , "SEA+ - Bat Yam", "CrossFit - Bat Yam", "GO Active - Bat Yam")

    private val _gyms = MutableLiveData<List<String>>()
    val gyms : LiveData<List<String>> get() = _gyms

    val addresses = mapOf("Private Gym - Holon" to "Giv'at HaTahmoshet St 27, Holon",
        "Crossfit - Holon" to "Ha-Melakha St 18, Holon",
        "ICON - Holon" to "Rehov HaMerkava 38, Holon",
        "Space - Holon" to "Harokmim St 26, Holon",
        "ICON - Tel Aviv" to "Ahad Ha'Am St 9, Tel Aviv-Yafo",
        "Crossfit - Tel Aviv" to "HaYarkon St 167, Tel Aviv-Yafo",
        "Gordon Gym - Tel Aviv" to "Eliezer Peri St 14, Tel Aviv-Yafo",
        "Profit - Tel Aviv" to "Carlebach St 18, Tel Aviv-Yafo",
        "Profit - Bat Yam" to "HaHaroshet St 11, Bat Yam",
        "SEA+ - Bat Yam" to "Derech Ben Gurion 136, Bat Yam",
        "CrossFit - Bat Yam" to "Ehud Kinnamon St 23, Bat Yam",
        "GO Active - Bat Yam" to "Yoseftal St 92, Bat Yam")


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

    fun updateItem(item: Item) {
        viewModelScope.launch {
            repository.updateItem(item)
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

    fun updateGyms(cities: List<String>) {
        val allGyms = mutableListOf<String>()

        if (cities.contains("holon")) {
            allGyms.addAll(gymsHolon)
        }
        if (cities.contains("tel_aviv")) {
            allGyms.addAll(gymsTelAviv)
        }
        if (cities.contains("bat_yam")) {
            allGyms.addAll(gymsBatYam)
        }

        _gyms.value = allGyms
    }


}