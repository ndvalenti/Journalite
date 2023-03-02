package com.nvalenti.journalite.controller

import java.util.Date
import java.util.UUID

class JournalEntry {
    var id = UUID.randomUUID()
    var itemId = UUID.randomUUID()

    lateinit var title: String
    lateinit var date: Date
    lateinit var entry: String
    var rating: Int? = null
}