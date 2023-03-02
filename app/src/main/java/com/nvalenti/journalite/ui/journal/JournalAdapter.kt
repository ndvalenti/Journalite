package com.nvalenti.journalite.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.R
import com.nvalenti.journalite.controller.JournalEntry

class JournalAdapter(journalEntries: List<JournalEntry>): RecyclerView.Adapter<JournalAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val journalTitle: TextView
        val journalDate: TextView
        val journalRating: TextView
        val journalBody: TextView

        init {
            journalTitle = view.findViewById(R.id.journalTitleTV)
            journalDate = view.findViewById(R.id.journalDateTV)
            journalRating = view.findViewById(R.id.journalRatingTV)
            journalBody = view.findViewById(R.id.journalBodyTV)
        }
    }

    private val entries: List<JournalEntry>
    init {
        entries = journalEntries
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journal_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.journalTitle.text = entries[position].title
        holder.journalBody.text = entries[position].entry
        holder.journalRating.text = entries[position].rating.toString()
        holder.journalDate.text = entries[position].date.toString()
    }

    override fun getItemCount(): Int {
        return entries.count()
    }
}