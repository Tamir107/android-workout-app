package com.hit.gymtime.ui.all_items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hit.gymtime.data.models.Item
import com.hit.gymtime.databinding.ItemLayoutBinding

class ItemAdapter(val items:List<Item>, val callBack : ItemListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(index: Int)
    }

    inner class ItemViewHolder(private val binding : ItemLayoutBinding):RecyclerView.ViewHolder(binding.root),View.OnClickListener,View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return false
        }

        fun bind(item: Item){
            binding.itemDate.text = "${item.date} ${item.hour}"
            binding.itemHour.text = item.partner
            binding.itemType.text = item.type
            binding.itemLocation.text = item.location
            Glide.with(binding.root).load(item.photo).circleCrop().into(binding.itemImg)
        }

    }

    fun itemAt(position: Int) = items[position]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() =
        items.size
}