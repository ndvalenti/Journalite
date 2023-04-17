package com.nvalenti.journalite.controller

import androidx.room.*
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "journal_metadata")
class JournalMetaData(@PrimaryKey val id: UUID = UUID.randomUUID()) {
    /**
     * Tracks the last time that tasks were generated
     * Will be null if tasks have never generated or have been invalidated
     */
    @ColumnInfo(name="last_updated") var lastUpdated: LocalDateTime? = null
}
