package com.nvalenti.journalite.ui.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.R
import com.nvalenti.journalite.controller.JournalItem

class ItemsAdapter(journalItem: List<JournalItem>): RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val itemTitle: TextView
        val itemNotify: SwitchCompat
        init {
            itemTitle = view.findViewById(R.id.itemTitleTV)
            itemNotify = view.findViewById(R.id.itemNotificationSwitch)
        }
    }

    private val items: List<JournalItem>
    init {
        items = journalItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = items[position].title
        holder.itemNotify.isChecked = items[position].notification
    }

    override fun getItemCount(): Int = items.count()
}