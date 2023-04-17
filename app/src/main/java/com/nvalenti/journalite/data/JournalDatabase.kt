package com.nvalenti.journalite.data

import android.content.Context
import androidx.room.*
import com.nvalenti.journalite.controller.JournalMetaData
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.controller.JournalItem
import com.nvalenti.journalite.controller.JournalTask
import kotlinx.coroutines.flow.Flow

//@Database(entities = [JournalDB::class, JournalEntryDB::class, JournalItemDB::class, JournalTaskDB::class], version = 1)
@Database(entities = [JournalMetaData::class, JournalEntry::class, JournalItem::class, JournalTask::class], version = 1)
@TypeConverters(Converters::class)
abstract class JournalDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile private var INSTANCE: JournalDatabase? = null

        fun getDatabase(context: Context): JournalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, JournalDatabase::class.java,"journalite_database")
                    //.createFromAsset("database/journalite.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}