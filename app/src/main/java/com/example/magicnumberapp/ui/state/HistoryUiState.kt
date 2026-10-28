package com.example.magicnumberapp.ui.state

import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.data.model.GameStats

data class HistoryUiState(
    val results: List<GameResult> = emptyList(),
    val stats: GameStats = GameStats(),
    val isLoading: Boolean = true
)
