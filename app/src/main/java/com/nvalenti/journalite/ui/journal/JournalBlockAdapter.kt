package com.nvalenti.journalite.ui.journal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.controller.JournalEntryBlock
import com.nvalenti.journalite.databinding.JournalBlockRowBinding

//class JournalBlockAdapter : ListAdapter<JournalEntry, JournalBlockAdapter.ViewHolder>(DiffCallback) {
class JournalBlockAdapter : ListAdapter<JournalEntryBlock, JournalBlockAdapter.ViewHolder>(DiffCallback) {
//class JournalBlockAdapter(private val blocks: List<JournalEntryBlock>) : RecyclerView.Adapter<JournalBlockAdapter.ViewHolder>() {
//class JournalBlockAdapter: RecyclerView.Adapter<JournalBlockAdapter.ViewHolder>() {
    //var blocks = mutableListOf<JournalEntryBlock>()

    /*
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = JournalBlockRowBinding.bind(view)
    }
     */
    //private lateinit var recyclerView: RecyclerView

    inner class ViewHolder(private var binding: JournalBlockRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: JournalEntryBlock) {
            binding.journalBlockDayTV.text = entry.date?.dayOfWeek.toString()
            binding.journalBlockDateTV.text = entry.date?.toString()

            val recyclerView = binding.journalDetailsRV
            recyclerView.adapter = JournalRowAdapter(entry.entry)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.journal_block_row, parent, false)
        //return ViewHolder(view)
        val viewHolder = ViewHolder(
            JournalBlockRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
        }
        return viewHolder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        /*
        holder.binding.apply {
            val block = blocks[position]
            journalBlockDateTV.text = block.date.toString()
            journalBlockDayTV.text = block.date?.dayOfWeek.toString()

            val journalRowAdapter = JournalRowAdapter(block.entry)
            journalDetailsRV.adapter = journalRowAdapter
        }
         */
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