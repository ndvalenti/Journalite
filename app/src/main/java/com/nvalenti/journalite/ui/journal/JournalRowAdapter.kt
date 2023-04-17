package com.nvalenti.journalite.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.R
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.databinding.JournalRowBinding

class JournalRowAdapter(private val journalEntry: List<JournalEntry>): RecyclerView.Adapter<JournalRowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journal_row, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = JournalRowBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val entry = journalEntry[position]
            journalTitleTV.text = entry.title
            journalBodyTV.text = entry.entry
        }
    }

    override fun getItemCount() = journalEntry.size
}