package com.example.magicnumberapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.magicnumberapp.data.repository.RiddleRepository
import com.example.magicnumberapp.ui.state.HistoryUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(repository: RiddleRepository) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> =
        combine(repository.history, repository.stats) { results, stats ->
            HistoryUiState(results = results, stats = stats, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState()
        )

    companion object {
        fun factory(repository: RiddleRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    HistoryViewModel(repository) as T
            }
    }
}
