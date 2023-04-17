package com.nvalenti.journalite.controller

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * Helper class representing a period of time in a single day with explicit date
 * For use in translating more generalized date patterns into more localized calendar patterns
 */
data class TimeBlock(val start: LocalTime, val end: LocalTime, val day: DayOfWeek, val date: LocalDate)