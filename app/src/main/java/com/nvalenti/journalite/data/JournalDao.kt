package com.nvalenti.journalite.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.controller.JournalItem
import com.nvalenti.journalite.controller.JournalMetaData
import com.nvalenti.journalite.controller.JournalTask
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entry")
    fun getEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_item")
    fun getItems(): Flow<List<JournalItem>>

    @Query("SELECT * FROM journal_task")
    fun getTasks(): Flow<List<JournalTask>>

    @Query("SELECT * FROM journal_entry")
    fun getEntrySnapshot(): List<JournalEntry>

    @Query("SELECT * FROM journal_item")
    fun getItemSnapshot(): List<JournalItem>

    @Query("SELECT * FROM journal_task")
    fun getTaskSnapshot(): List<JournalTask>

    @Query("SELECT * FROM journal_entry WHERE id=:id")
    fun journalEntry(id: UUID): Flow<JournalEntry>

    @Query("SELECT * FROM journal_item WHERE id=:id")
    fun journalItem(id: UUID): LiveData<JournalItem>

    @Query("SELECT * FROM journal_task WHERE id=:id")
    fun journalTask(id: UUID): Flow<JournalTask>

    @Query("SELECT * FROM journal_entry WHERE id=:id")
    fun getEntryById(id: UUID): List<JournalEntry>

    @Query("SELECT * FROM journal_task WHERE entry_id=:entryId")
    fun getTasksByEntryId(entryId: UUID): List<JournalTask>

    @Query("SELECT * FROM journal_metadata")
    fun getMetaData(): List<JournalMetaData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateEntry(entry: JournalEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateItem(item: JournalItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateTask(task: JournalTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMetaData(metaData: JournalMetaData)

    @Update
    fun updateEntries(entries: List<JournalEntry>)

    @Update
    fun updateTasks(tasks: List<JournalTask>)

    @Delete
    fun deleteEntry(entry: JournalEntry)

    @Delete
    fun deleteItem(item: JournalItem)

    @Query("DELETE FROM journal_task WHERE item_id=:itemId")
    fun deleteTasksByItemId(itemId: UUID)

    @Delete
    fun deleteTask(task: JournalTask)

    @Delete
    fun deleteTasks(tasks: List<JournalTask>)

    @Query("UPDATE journal_item SET notify=:state WHERE id=:id")
    fun setItemNotifyState(id: UUID, state: Boolean)
}