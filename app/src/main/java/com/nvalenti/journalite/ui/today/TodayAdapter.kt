package com.nvalenti.journalite.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.R
import com.nvalenti.journalite.controller.JournalTask

class TodayAdapter(journalTasks: List<JournalTask>) : RecyclerView.Adapter<TodayAdapter.ViewHolder>() {
//class TodayAdapter(journalTasks: LiveData<List<JournalTask>>) : RecyclerView.Adapter<TodayAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val todayItemTitle: TextView
        val todayItemTime: TextView

        init {
            todayItemTitle = view.findViewById(R.id.taskTitleTV)
            todayItemTime = view.findViewById(R.id.taskTimeTV)
        }
    }

    private val tasks: List<JournalTask>
    init {
        tasks = journalTasks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.todayItemTitle.text = tasks.value?.get(position)?.title
        //holder.todayItemTime.text = tasks.value?.get(position)?.date
        holder.todayItemTitle.text = tasks[position].title
        holder.todayItemTime.text = tasks[position].date
    }

    override fun getItemCount(): Int {
        /*
        tasks.value?.let {
            return it.count()
        }
        return 0
         */
        return tasks.count()
    }
}