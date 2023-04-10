package com.nvalenti.journalite.controller

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

/**
 * JournalItems represent recurring prompts and are used to generate JournalTasks that then spawn JournalEntries
 */
@Entity(tableName = "journal_item")
data class JournalItem(
    @PrimaryKey var id: UUID
) {
    var title: String? = null
    var days: MutableSet<DayOfWeek> = mutableSetOf()
    @ColumnInfo(name="time_due") var timeDue: LocalTime? = null
    @ColumnInfo(name="archived") var isArchived: Boolean = false
    @ColumnInfo(name="notify") var isNotify: Boolean = false
}