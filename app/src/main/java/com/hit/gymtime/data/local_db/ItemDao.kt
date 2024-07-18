package com.hit.gymtime.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hit.gymtime.data.models.Item

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Delete
    suspend fun deleteItem(vararg items: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items ORDER BY item_date ASC")
    fun getItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE id LIKE :id")
    fun getItem(id: Int): Item

    @Query("DELETE FROM items")
    suspend fun deleteAll()
}