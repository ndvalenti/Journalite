package com.nvalenti.journalite.controller

import java.time.LocalDate

data class JournalEntryBlock (
    val date: LocalDate?,
    val entry: List<JournalEntry>
)