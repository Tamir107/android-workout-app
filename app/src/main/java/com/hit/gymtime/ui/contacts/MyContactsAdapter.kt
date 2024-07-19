package com.hit.gymtime.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hit.gymtime.databinding.ContactItemBinding

class MyContactsAdapter(
    private var contacts: List<Contact>,
    private val listener: RecyclerContactClickListener
) : RecyclerView.Adapter<MyContactsAdapter.MyViewHolder>() {

    fun interface RecyclerContactClickListener {
        fun onItemClicked(contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = contacts.size

    fun setList(newList: List<Contact>) {
        contacts = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { listener.onItemClicked(contacts[adapterPosition]) }
        }

        fun bind() {

            binding.contactName.text = contacts[adapterPosition].name

            if(contacts[adapterPosition].phoneNumber.isNotEmpty()) {
                binding.contactPhone.text = contacts[adapterPosition].phoneNumber
                binding.contactPhone.isVisible = true
            }
            else binding.contactPhone.isVisible = false

            if(contacts[adapterPosition].email.isNotEmpty()) {
                binding.contactEmail.text = contacts[adapterPosition].email
                binding.contactEmail.isVisible = true
            }
            else binding.contactEmail.isVisible = false
        }

    }
}
