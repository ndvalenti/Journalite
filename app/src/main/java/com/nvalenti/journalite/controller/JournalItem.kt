package com.nvalenti.journalite.controller

import android.graphics.Color
import java.util.UUID

// Journal Items generate recurring tasks to fill out journal entries
class JournalItem {
    var id = UUID.randomUUID()

    lateinit var title: String
    lateinit var category: Color

    var timeDue: Int = -1
    var notification: Boolean = false

    // Archived items will not generate future tasks but exist as a reference point for journal entries
    var archived: Boolean = false
}