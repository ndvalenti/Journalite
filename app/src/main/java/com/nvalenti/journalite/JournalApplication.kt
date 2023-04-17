package com.nvalenti.journalite

import android.app.Application
import com.nvalenti.journalite.data.JournalDatabase

class JournalApplication : Application() {
    val database: JournalDatabase by lazy { JournalDatabase.getDatabase(this) }
}