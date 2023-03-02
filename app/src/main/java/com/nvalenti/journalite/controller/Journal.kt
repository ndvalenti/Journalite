package com.nvalenti.journalite.controller

import android.graphics.Color
import android.util.Log
import java.util.*

class Journal {
    // TODO: Items and Entries may need to become maps with <id:item> format
    val journalTasks = mutableListOf<JournalTask>()
    //val journalEntries = mutableMapOf<UUID, JournalEntry>()
    val journalEntries = mutableListOf<JournalEntry>()
    //val journalItems = mutableMapOf<UUID, JournalItem>()
    val journalItems = mutableListOf<JournalItem>()

    // Tracks the last time that tasks were generated
    var lastUpdated: Date? = null

    fun addTask() {
        val newTask = JournalTask()
        newTask.title = "NewTask"
        newTask.date = "Date"
        journalTasks.add(newTask)
    }

    fun addEntry() {
        val newEntry = JournalEntry()
        newEntry.title = "Title"
        newEntry.entry = "Sample Entry"
        newEntry.date = Date()
        newEntry.rating = 4
        journalEntries.add(newEntry)
    }

    fun addItem() {
        val newItem = JournalItem()
        newItem.title = "New Item"
        newItem.timeDue = 600
        newItem.category = Color()
        journalItems.add(newItem)
    }
}