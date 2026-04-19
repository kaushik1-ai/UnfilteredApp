package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfilteredapp.data.model.SpotifyTrack
import com.example.unfilteredapp.data.repository.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MusicState {
    object Idle : MusicState()
    object Loading : MusicState()
    data class Success(val tracks: List<SpotifyTrack>) : MusicState()
    data class Error(val message: String) : MusicState()
}

class MusicViewModel(private val repository: SpotifyRepository) : ViewModel() {
    private val _musicState = MutableStateFlow<MusicState>(MusicState.Idle)
    val musicState: StateFlow<MusicState> = _musicState

    fun loadMoodSuggestions(mood: String) {
        viewModelScope.launch {
            _musicState.value = MusicState.Loading
            try {
                val tracks = repository.getMoodSuggestions(mood)
                if (tracks.isNotEmpty()) {
                    _musicState.value = MusicState.Success(tracks)
                } else {
                    _musicState.value = MusicState.Error("No suggestions found for this mood")
                }
            } catch (e: Exception) {
                _musicState.value = MusicState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
