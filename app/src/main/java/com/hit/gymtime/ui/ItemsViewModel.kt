package com.hit.gymtime.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hit.gymtime.R
import com.hit.gymtime.data.LocationUpdatesLiveData
import com.hit.gymtime.data.models.Item
import com.hit.gymtime.data.repository.itemRepository
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = itemRepository(application)

    val items : LiveData<List<Item>>? = repository.getItems()

    val location : LiveData<String> = LocationUpdatesLiveData(application.applicationContext)

    val workoutTypes = arrayOf(application.applicationContext.getString(R.string.cardio),
        application.applicationContext.getString(R.string.strength),
        application.applicationContext.getString(R.string.flexibility),
        application.applicationContext.getString(R.string.balance))


    val gymsHolon = arrayOf(application.applicationContext.getString(R.string.private_gym_holon) ,
        application.applicationContext.getString(R.string.crossfit_holon),
        application.applicationContext.getString(R.string.icon_holon), application.applicationContext.getString(R.string.space_holon))
    val gymsTelAviv = arrayOf(application.applicationContext.getString(R.string.icon_tel_aviv) ,
        application.applicationContext.getString(R.string.crossfit_tel_aviv),
        application.applicationContext.getString(R.string.gordon_gym_tel_aviv), application.applicationContext.getString(R.string.profit_tel_aviv))
    val gymsBatYam = arrayOf(application.applicationContext.getString(R.string.profit_bat_yam) ,
        application.applicationContext.getString(R.string.sea_bat_yam),
        application.applicationContext.getString(R.string.crossfit_bat_yam), application.applicationContext.getString(R.string.go_active_bat_yam))

    private val _gyms = MutableLiveData<List<String>>()
    val gyms : LiveData<List<String>> get() = _gyms

    val addresses = mapOf(application.applicationContext.getString(R.string.private_gym_holon) to application.applicationContext.getString(R.string.giv_at_hatahmoshet_st_27_holon),
        application.applicationContext.getString(R.string.crossfit_holon) to application.applicationContext.getString(R.string.ha_melakha_st_18_holon),
        application.applicationContext.getString(R.string.icon_holon) to application.applicationContext.getString(R.string.rehov_hamerkava_38_holon),
        application.applicationContext.getString(R.string.space_holon) to application.applicationContext.getString(R.string.harokmim_st_26_holon),
        application.applicationContext.getString(R.string.icon_tel_aviv) to application.applicationContext.getString(R.string.ahad_ha_am_st_9_tel_aviv_yafo),
        application.applicationContext.getString(R.string.crossfit_tel_aviv) to application.applicationContext.getString(R.string.hayarkon_st_167_tel_aviv_yafo),
        application.applicationContext.getString(R.string.gordon_gym_tel_aviv) to application.applicationContext.getString(R.string.eliezer_peri_st_14_tel_aviv_yafo),
        application.applicationContext.getString(R.string.profit_tel_aviv) to application.applicationContext.getString(R.string.carlebach_st_18_tel_aviv_yafo),
        application.applicationContext.getString(R.string.profit_bat_yam) to application.applicationContext.getString(R.string.haharoshet_st_11_bat_yam),
        application.applicationContext.getString(R.string.sea_bat_yam) to application.applicationContext.getString(R.string.derech_ben_gurion_136_bat_yam),
        application.applicationContext.getString(R.string.crossfit_bat_yam) to application.applicationContext.getString(R.string.ehud_kinnamon_st_23_bat_yam),
        application.applicationContext.getString(R.string.go_active_bat_yam) to application.applicationContext.getString(R.string.yoseftal_st_92_bat_yam))


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