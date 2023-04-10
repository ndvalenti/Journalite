package com.nvalenti.journalite.controller

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

/**
 * Valid tasks are kept until they are completed and more than 24 hours old, tasks are generated automatically by cross referencing with the Item list
 * They are a go-between for items and entries and should contain no original information
 */

@Entity(tableName = "journal_task")
data class JournalTask(
    @ColumnInfo(name="item_id") var itemId: UUID,
    @ColumnInfo(name="due_date") var dueDate: LocalDateTime,
    var title: String?
) {
    @PrimaryKey var id: UUID = UUID.randomUUID()
    @ColumnInfo(name="entry_id") var entryId: UUID = UUID.randomUUID()
    @ColumnInfo(name="stale") var isStale: Boolean = false
    @ColumnInfo(name="entry") var hasEntry: Boolean = false
}