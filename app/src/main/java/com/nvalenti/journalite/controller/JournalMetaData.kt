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

    // TODO: Stretch - items and entries in sets makes more sense since we aren't using indicies but would need to be converted to lists for their respective fragments
    /*
    @Ignore private var _journalTasks = tasks
    @Ignore private var _journalEntries = entries
    @Ignore private var _journalItems = items


    val tasks: List<JournalTask> get() = journalTasks
    val entries: List<JournalEntry> get() = journalEntries
    val items: List<JournalItem> get() = journalItems
     */
    /*
    fun addOrUpdateItem(item: JournalItem) {
        if (journalItems.removeIf { it.id == item.id }) {
            /* After removing the old item we need to ensure the following is completed:
             * 1) Remove matching tasks that have not been interacted with and are not stale (older than 24 hours)
             * 2) Update the titles of all remaining (old or stale) tasks
             * 3) Update the titles of all matching entries
             */

            journalTasks.removeIf { task ->
                task.itemId == item.id &&
                        (task.entryId == null && !task.isStale)
            }

            journalTasks.forEach { task ->
                if (task.itemId == item.id) {
                    task.title = item.title
                }
            }

            journalEntries.forEach { entry ->
                if (entry.itemId == item.id) {
                    entry.title = item.title
                }
            }
        }

        journalItems.add(item)
        flagCheckRequireTaskUpdate()
    }

    fun addOrUpdateEntry(entry: JournalEntry) {
        journalEntries.removeIf { it.id == entry.id }
        journalEntries.add(entry)
    }

    private fun flagCheckRequireTaskUpdate() {
        val timeSinceLastUpdate = Duration.between(lastUpdated ?: LocalDateTime.now(), LocalDateTime.now())
        if (timeSinceLastUpdate.toHours() < 24) {
            lastUpdated = null
        }
    }

    private fun addUniqueTaskFromItem(item: JournalItem, localDate: LocalDate) {
        if (journalTasks.firstOrNull { it.itemId == item.id && it.dueDate.toLocalTime() == item.timeDue } == null) {
            val task = JournalTask(item.id, LocalDateTime.of(localDate, item.timeDue), item.title)
            journalTasks.add(task)
        }
    }

    /**
     * Does item lie within specified TimeBlock
     * @Return true if item is not archived and falls within the specified TimeBlock
     */
    fun isInInterval(item: JournalItem, interval: TimeBlock): Boolean {
        if (item.isArchived) return false

        item.timeDue?.let { time ->
            if (item.days.contains(interval.day)) {
                if (time >= interval.start && time <= interval.end) {
                    return true
                }
            }
        }
        return false
    }

    fun updateTasks() {
        val blocks = mutableListOf<TimeBlock>()
        val staleTime = LocalDateTime.now().minusDays(1)

        // TODO: Changing time zones may break this, lastupdated will need to be standardized with a time zone to avoid edge case
        if (lastUpdated == null) {
            lastUpdated = staleTime
        }

        val endTime = LocalDateTime.now().plusHours(1)
        var startTime = lastUpdated ?: endTime

        // Populate TimeBlocks from start and end times
        while (startTime < endTime) {
            var endTimeAdjusted = endTime

            if (startTime.toLocalDate() != endTimeAdjusted.toLocalDate()) {
                endTimeAdjusted = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(23,59,59))
            }

            val currentBlock = TimeBlock(startTime.toLocalTime(), endTimeAdjusted.toLocalTime(), startTime.dayOfWeek, startTime.toLocalDate())
            blocks.add(currentBlock)

            // Move start time forward to midnight of the next day
            startTime = LocalDateTime.of(startTime.toLocalDate().plusDays(1), LocalTime.of(0,0,0))
        }
        lastUpdated = LocalDateTime.now()

        blocks.forEach { block ->
            journalItems.forEach { item ->
                if (isInInterval(item, block)) {
                    addUniqueTaskFromItem(item, block.date)
                }
            }
        }

        val removeList = mutableListOf<JournalTask>()
        journalTasks.filter { it.dueDate < staleTime }.forEach { task ->
            if (task.entryId != null) {
                removeList.add(task)
            } else {
                task.isStale = true
            }
        }
        journalTasks.removeAll(removeList)
    }
     */
}
