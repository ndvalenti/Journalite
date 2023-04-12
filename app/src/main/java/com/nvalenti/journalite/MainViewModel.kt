package com.nvalenti.journalite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nvalenti.journalite.controller.*
import com.nvalenti.journalite.data.JournalDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class MainViewModel(private val dao: JournalDao): ViewModel() {
    private var journalMetaData = JournalMetaData()

    fun journalTasks(): Flow<List<JournalTask>> = dao.getTasks()
    fun journalEntries(): Flow<List<JournalEntry>> = dao.getEntries()
    fun journalItems(): Flow<List<JournalItem>> = dao.getItems()
    fun journalItem(id: UUID): LiveData<JournalItem> = dao.journalItem(id)
    fun getEntrySnapshotById(id: UUID): JournalEntry? = dao.getEntryById(id).firstOrNull()
    fun getTaskByEntryId(entryId: UUID): JournalTask? = dao.getTasksByEntryId(entryId).firstOrNull()


    fun addOrUpdateItem(item: JournalItem) {
        val items = dao.getItemSnapshot()
        val itemToUpdate = items.firstOrNull { it.id == item.id }
        if (itemToUpdate == null) {
            flagCheckRequireTaskUpdate()
            CoroutineScope(Dispatchers.IO).launch {
                dao.insertOrUpdateItem(item)
            }
        } else {
            /* If we are updating an existing item we need to ensure the following is completed:
             * 1) Remove matching tasks that have not been interacted with and are not stale (older than 24 hours), they will be regenerated
             * 2) Update the titles of all remaining (old or stale) tasks
             * 3) Update the titles of all matching entries
             * 4) Insert or update the item
             */
            val tasks = dao.getTaskSnapshot()

            val dirtyTasks = mutableListOf<JournalTask>()
            val regenTasks = mutableListOf<JournalTask>()

            tasks.forEach { task ->
                if (task.itemId == item.id) {
                    if (!task.hasEntry && !task.isStale) {
                        dirtyTasks.add(task)
                    } else {
                        regenTasks.add(task)
                    }
                }
            }
            val entries = dao.getEntrySnapshot()
            val regenEntries = entries.filter { it.itemId == item.id }

            regenTasks.forEach { task ->
                task.title = item.title
            }

            flagCheckRequireTaskUpdate()

            CoroutineScope(Dispatchers.IO).launch {
                dao.deleteTasks(dirtyTasks)
                dao.updateTasks(regenTasks)
                dao.updateEntries(regenEntries)
                dao.insertOrUpdateItem(item)
            }
        }
    }

    fun addOrUpdateEntry(entry: JournalEntry) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertOrUpdateEntry(entry)
        }
    }

    private fun addUniqueTaskFromItem(item: JournalItem, localDate: LocalDate) {
        val tasks = dao.getTaskSnapshot()
        if (tasks.firstOrNull { it.itemId == item.id && it.dueDate.toLocalTime() == item.timeDue } == null) {
            val task = JournalTask(item.id, LocalDateTime.of(localDate, item.timeDue), item.title)
            CoroutineScope(Dispatchers.IO).launch {
                dao.insertOrUpdateTask(task)
            }
        }
    }

    fun addOrUpdateTask(task: JournalTask) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertOrUpdateTask(task)
        }
    }

    private fun flagCheckRequireTaskUpdate() {
        val timeSinceLastUpdate = Duration.between(
            journalMetaData.lastUpdated ?: LocalDateTime.now(),
            LocalDateTime.now()
        )
        if (timeSinceLastUpdate.toHours() < 24) {
            setLastUpdated(null)
        }
    }

    private fun setLastUpdated(inLastUpdated: LocalDateTime?) {
        journalMetaData.lastUpdated = inLastUpdated
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertOrUpdateMetaData(journalMetaData)
        }
    }

    /**
     * Does item lie within specified TimeBlock
     * @Return true if item is not archived and falls within the specified TimeBlock
     */
    private fun isInInterval(item: JournalItem, interval: TimeBlock): Boolean {
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
        val staleTime = LocalDateTime.now().minusHours(20)

        // TODO: Changing time zones may break this, lastupdated will need to be standardized with a time zone to avoid edge case
        if (journalMetaData.lastUpdated == null) {
            setLastUpdated(staleTime)
        }

        val endTime = LocalDateTime.now().plusHours(2)
        var startTime = journalMetaData.lastUpdated ?: endTime

        // Populate TimeBlocks from start and end times
        while (startTime < endTime) {
            var endTimeAdjusted = endTime

            if (startTime.toLocalDate() != endTimeAdjusted.toLocalDate()) {
                endTimeAdjusted =
                    LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(23, 59, 59))
            }

            val currentBlock = TimeBlock(
                startTime.toLocalTime(),
                endTimeAdjusted.toLocalTime(),
                startTime.dayOfWeek,
                startTime.toLocalDate()
            )
            blocks.add(currentBlock)

            // Move start time forward to midnight of the next day
            startTime = LocalDateTime.of(startTime.toLocalDate().plusDays(1), LocalTime.of(0, 0, 0))
        }
        setLastUpdated(LocalDateTime.now())

        val items = dao.getItemSnapshot()
        blocks.forEach { block ->
            items.forEach { item ->
                if (isInInterval(item, block)) {
                    // TODO: make sure that we are adding unique tasks on unique dates
                    addUniqueTaskFromItem(item, block.date)
                }
            }
        }

        val removeList = mutableListOf<JournalTask>()
        val updateList = mutableListOf<JournalTask>()
        val tasks = dao.getTaskSnapshot()

        //_journalTasks.filter { it.dueDate < staleTime }.forEach { task ->
        tasks.filter { it.dueDate < staleTime }.forEach { task ->
            if (task.hasEntry) {
                removeList.add(task)
            } else {
                task.isStale = true
                updateList.add(task)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteTasks(removeList)
            dao.updateTasks(updateList)
        }
    }

    companion object {
        val navBarVisible: MutableLiveData<Boolean> by lazy {
            MutableLiveData<Boolean>()
        }
    }
}

class MainViewModelFactory(
    private val dao: JournalDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}