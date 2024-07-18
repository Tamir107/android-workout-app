package com.hit.gymtime.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(

    @ColumnInfo(name = "item_name")
    val title: String,

    @ColumnInfo(name = "content_des")
    val description: String,

    @ColumnInfo(name = "image")
    val photo: String?

) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    ) {
        id = parcel.readInt()
    }
    override fun describeContents(): Int {
        return 0
    }
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(photo)
        dest.writeInt(id)
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}



//object ItemManager{
//    val items: MutableList<Item> = mutableListOf()
//
//    fun add(item: Item){
//        items.add(item)
//    }
//
//    fun remove(index:Int){
//        items.removeAt(index)
//    }
//}