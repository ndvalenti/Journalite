package com.nvalenti.journalite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvalenti.journalite.controller.Journal
import com.nvalenti.journalite.controller.JournalTask

class MainViewModel: ViewModel() {
    var loggedIn: Boolean = true

    /*
    private val _journal: MutableLiveData<Journal> by lazy {
        MutableLiveData<Journal>()
    }
    val journal: LiveData<Journal> = _journal

     */
    val journal = Journal()
    /*
    private val _journalTasks: MutableLiveData<List<JournalTask>> by lazy {
        MutableLiveData<List<JournalTask>>(journal.journalTasks)
    }
    val journalTasks: LiveData<List<JournalTask>> = _journalTasks
    */
    val journalTasks: MutableList<JournalTask> = journal.journalTasks

    enum class LockState {
        LOCKED, UNLOCKED, UNREGISTERED
    }

    private val _lockState: MutableLiveData<LockState> by lazy {
        MutableLiveData<LockState>(LockState.LOCKED)
    }
    val lockState: LiveData<LockState> = _lockState

    fun unlock() {
        _lockState.value = LockState.UNLOCKED
    }

    fun lock() {
        _lockState.value = LockState.LOCKED
    }
}