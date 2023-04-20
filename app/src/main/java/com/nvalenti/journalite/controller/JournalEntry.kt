package com.nvalenti.journalite.controller

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * JournalEntries are individual entries spawned by a Task but tied only to an Item
 * Once their task expires they are only modified when the Item title changes
 */

@Entity(tableName = "journal_entry")
data class JournalEntry(
    @PrimaryKey var id: UUID,
    @ColumnInfo(name="item_id") var itemId: UUID,
    @ColumnInfo(name="due_date") var dueDate: LocalDateTime,
    var title: String?
) {
    @ColumnInfo(name="entry_date") var entryDate: LocalDateTime? = null
    var entry: String? = null
    var rating: Int? = null
}