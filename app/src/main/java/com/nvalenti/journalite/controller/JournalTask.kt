package com.nvalenti.journalite.controller

import java.util.*

// Valid tasks are kept until they are completed and more than 24 hours old, tasks are generated automatically by cross referencing with the Item list
class JournalTask {
    lateinit var itemId: UUID
    lateinit var entryId: UUID

    var title: String = ""
    var date: String = ""

    var completed: Boolean = false
}