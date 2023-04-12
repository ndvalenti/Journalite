package com.nvalenti.journalite.ui.journal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.databinding.JournalRowBinding

class JournalAdapter : ListAdapter<JournalEntry, JournalAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private var binding: JournalRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: JournalEntry) {
            binding.journalTitleTV.text = entry.title
            binding.journalBodyTV.text = entry.entry
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            JournalRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<JournalEntry>() {
            override fun areItemsTheSame(oldItem: JournalEntry, newItem: JournalEntry): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JournalEntry, newItem: JournalEntry): Boolean {
                return oldItem == newItem
            }
        }
    }
}