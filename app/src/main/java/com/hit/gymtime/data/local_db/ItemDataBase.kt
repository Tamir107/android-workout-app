package com.hit.gymtime.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hit.gymtime.data.models.Item

@Database(entities = arrayOf(Item::class), version = 2, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {
    abstract fun itemsDao() : ItemDao

    companion object{
        @Volatile
        private var instance: ItemDataBase? = null

        fun getDataBase(context: Context) = instance ?: synchronized(this){
            Room.databaseBuilder(context.applicationContext,ItemDataBase::class.java,"item_db").build()
        }
    }
}