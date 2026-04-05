package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MoodViewModel : ViewModel() {
    private val _selectedMood = MutableStateFlow<String?>(null)
    val selectedMood: StateFlow<String?> = _selectedMood

    fun selectMood(moodTag: String) {
        _selectedMood.value = moodTag
    }
}
