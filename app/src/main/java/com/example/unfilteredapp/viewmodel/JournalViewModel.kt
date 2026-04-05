package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JournalViewModel : ViewModel() {
    private val _entries = MutableStateFlow<List<String>>(emptyList())
    val entries: StateFlow<List<String>> = _entries

    fun addEntry(entry: String) {
        if (entry.isNotBlank()) {
            _entries.value = listOf(entry) + _entries.value
        }
    }
}
