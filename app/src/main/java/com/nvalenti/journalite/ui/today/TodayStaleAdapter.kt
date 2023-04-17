package com.nvalenti.journalite.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.controller.JournalTask
import com.nvalenti.journalite.databinding.StaleRowBinding
import com.nvalenti.journalite.databinding.TodayRowBinding

class TodayStaleAdapter(private val onDeleteClicked: (task: JournalTask) -> Unit, private val onItemClicked: (task: JournalTask) -> Unit) : ListAdapter<JournalTask, TodayStaleAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private var binding: StaleRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(task: JournalTask) {
            binding.taskTitleTV.text = task.title
            binding.taskTimeTV.text = task.dueDate.toLocalDate().toString()
            binding.deleteStaleRowButton.setOnClickListener {
                onDeleteClicked(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            StaleRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<JournalTask>() {
            override fun areItemsTheSame(oldItem: JournalTask, newItem: JournalTask): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JournalTask, newItem: JournalTask): Boolean {
                return oldItem == newItem
            }
        }
    }
}