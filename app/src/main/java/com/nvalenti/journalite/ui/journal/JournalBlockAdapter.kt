package com.nvalenti.journalite.ui.journal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.controller.JournalEntryBlock
import com.nvalenti.journalite.databinding.JournalBlockRowBinding

class JournalBlockAdapter : ListAdapter<JournalEntryBlock, JournalBlockAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private var binding: JournalBlockRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: JournalEntryBlock) {
            binding.journalBlockDayTV.text = entry.date?.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercaseChar() }
            binding.journalBlockDateTV.text = entry.date?.toString()

            val recyclerView = binding.journalDetailsRV
            recyclerView.adapter = JournalRowAdapter(entry.entry)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            JournalBlockRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
        return viewHolder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<JournalEntryBlock>() {
            override fun areItemsTheSame(oldItem: JournalEntryBlock, newItem: JournalEntryBlock): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: JournalEntryBlock, newItem: JournalEntryBlock): Boolean {
                return oldItem == newItem
            }
        }
    }

}