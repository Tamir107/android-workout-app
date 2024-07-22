package com.hit.gymtime.data.repository

import android.app.Application
import com.hit.gymtime.data.local_db.ItemDao
import com.hit.gymtime.data.local_db.ItemDataBase
import com.hit.gymtime.data.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class itemRepository(application: Application) {

    private var itemDao:ItemDao?

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db?.itemsDao()
    }

    fun getItems() = itemDao?.getItems()

    suspend fun addItem(item: Item){
        itemDao?.addItem(item)
    }

    suspend fun updateItem(item: Item){
        itemDao?.updateItem(item)
    }

    suspend fun deleteItem(item: Item) {
        itemDao?.deleteItem(item)
    }

    fun getItem(id:Int) = itemDao?.getItem(id)

    suspend fun deleteAll(){
        itemDao?.deleteAll()
    }
}