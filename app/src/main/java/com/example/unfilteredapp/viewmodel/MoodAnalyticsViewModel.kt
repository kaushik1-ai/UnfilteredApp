package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfilteredapp.data.model.*
import com.example.unfilteredapp.data.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AnalyticsState {
    object Idle : AnalyticsState()
    object Loading : AnalyticsState()
    data class Success(val data: AnalyticsResponse) : AnalyticsState()
    data class Error(val message: String) : AnalyticsState()
}

class MoodAnalyticsViewModel(private val repository: MoodRepository) : ViewModel() {
    private val _analyticsState = MutableStateFlow<AnalyticsState>(AnalyticsState.Idle)
    val analyticsState: StateFlow<AnalyticsState> = _analyticsState

    fun fetchAnalytics(days: Int = 7) {
        viewModelScope.launch {
            _analyticsState.value = AnalyticsState.Loading
            try {
                val response = repository.getAnalytics(days)
                if (response.isSuccessful && response.body() != null) {
                    _analyticsState.value = AnalyticsState.Success(response.body()!!)
                } else {
                    _analyticsState.value = AnalyticsState.Error("Failed to fetch analytics: ${response.message()}")
                }
            } catch (e: Exception) {
                _analyticsState.value = AnalyticsState.Error("Network error: ${e.localizedMessage}")
            }
        }
    }

    fun logMood(modeType: String, modeSubType: String) {
        viewModelScope.launch {
            try {
                repository.logMood(MoodLogRequest(modeType, modeSubType))
                // Refresh analytics after logging
                fetchAnalytics()
            } catch (e: Exception) {
                // Silently fail or log
            }
        }
    }
}
