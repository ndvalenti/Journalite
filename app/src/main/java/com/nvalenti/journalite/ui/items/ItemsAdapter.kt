package com.nvalenti.journalite.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.controller.JournalItem
import com.nvalenti.journalite.databinding.ItemsRowBinding

class ItemsAdapter : ListAdapter<JournalItem, ItemsAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(private var binding: ItemsRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JournalItem) {
            binding.itemTitleTV.text = item.title
            binding.itemNotificationSwitch.isChecked = item.isNotify
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val action = ItemsFragmentDirections.actionGlobalItemDetailFragment(getItem(position).id)
            it.findNavController().navigate(action)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<JournalItem>() {
            override fun areItemsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}