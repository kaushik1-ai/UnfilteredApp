package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfilteredapp.data.model.PlaceResult
import com.example.unfilteredapp.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(private val repository: PlacesRepository = PlacesRepository()) : ViewModel() {
    private val _places = MutableStateFlow<List<PlaceResult>>(emptyList())
    val places: StateFlow<List<PlaceResult>> = _places

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchNearbyPlaces(lat: Double, lng: Double, type: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getNearbyPlaces(lat, lng, type)
                if (response.isSuccessful && response.body()?.status == "OK") {
                    _places.value = response.body()?.results ?: emptyList()
                } else {
                    _error.value = response.body()?.error_message ?: "Failed to fetch places"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
