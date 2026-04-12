package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfilteredapp.data.model.JournalEntryResponse
import com.example.unfilteredapp.data.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class JournalState {
    object Idle : JournalState()
    object Loading : JournalState()
    data class Success(val entries: List<JournalEntryResponse>) : JournalState()
    data class Error(val message: String) : JournalState()
}

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {
    private val _state = MutableStateFlow<JournalState>(JournalState.Idle)
    val state: StateFlow<JournalState> = _state

    fun fetchEntries() {
        viewModelScope.launch {
            _state.value = JournalState.Loading
            try {
                val response = repository.getEntries()
                if (response.isSuccessful && response.body() != null) {
                    _state.value = JournalState.Success(response.body()!!)
                } else {
                    _state.value = JournalState.Error("Failed to fetch entries")
                }
            } catch (e: Exception) {
                _state.value = JournalState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun addEntry(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            try {
                val response = repository.saveEntry(content)
                if (response.isSuccessful) {
                    fetchEntries() // Refresh the list
                }
            } catch (e: Exception) {
                // Handle failure
            }
        }
    }
}
