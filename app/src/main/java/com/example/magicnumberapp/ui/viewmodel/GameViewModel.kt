package com.example.magicnumberapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.data.repository.RiddleRepository
import com.example.magicnumberapp.ui.state.AnswerState
import com.example.magicnumberapp.ui.state.GameUiState
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(private val repository: RiddleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun startGame(difficulty: Difficulty) {
        _uiState.value = GameUiState(difficulty = difficulty, isLoading = true)
        viewModelScope.launch {
            val riddles = repository.startGame(difficulty)
            _uiState.value = GameUiState(
                difficulty = difficulty,
                riddles = riddles,
                startedAt = System.currentTimeMillis(),
                isLoading = false
            )
        }
    }

    fun selectOption(index: Int) {
        val state = _uiState.value
        val riddle = state.currentRiddle ?: return
        if (state.hasAnswered || index !in riddle.options.indices) return
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun confirmAnswer() {
        val state = _uiState.value
        val riddle = state.currentRiddle ?: return
        val selected = state.selectedOptionIndex ?: return
        if (state.hasAnswered) return

        val isCorrect = selected == riddle.correctOptionIndex
        _uiState.update {
            it.copy(
                answerState = if (isCorrect) AnswerState.Correct else AnswerState.Incorrect,
                correctAnswers = it.correctAnswers + if (isCorrect) 1 else 0
            )
        }
    }

    fun nextRiddle() {
        val state = _uiState.value
        if (!state.hasAnswered || state.isLastRiddle) return
        _uiState.update {
            it.copy(
                currentIndex = it.currentIndex + 1,
                selectedOptionIndex = null,
                answerState = AnswerState.Idle,
                isHintVisible = false
            )
        }
    }

    fun revealHint() {
        val state = _uiState.value
        if (state.isLoading || state.isHintVisible || state.currentRiddle == null) return
        _uiState.update {
            it.copy(
                isHintVisible = true,
                hintsUsed = it.hintsUsed + 1
            )
        }
    }

    fun finishGame() {
        val state = _uiState.value
        if (state.isFinished || state.riddles.isEmpty()) return

        val now = System.currentTimeMillis()
        val result = GameResult(
            id = UUID.randomUUID().toString(),
            timestamp = now,
            difficulty = state.difficulty,
            correctAnswers = state.correctAnswers,
            totalRiddles = state.totalRiddles,
            durationMillis = (now - state.startedAt).coerceAtLeast(0L)
        )

        _uiState.update { it.copy(isFinished = true, result = result) }
        viewModelScope.launch { repository.saveGameResult(result) }
    }

    companion object {
        fun factory(repository: RiddleRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    GameViewModel(repository) as T
            }
    }
}
