package com.example.magicnumberapp.ui.state

import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.data.model.Riddle

enum class AnswerState {
    Idle,
    Correct,
    Incorrect
}

data class GameUiState(
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val riddles: List<Riddle> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val answerState: AnswerState = AnswerState.Idle,
    val correctAnswers: Int = 0,
    val hintsUsed: Int = 0,
    val isHintVisible: Boolean = false,
    val startedAt: Long = 0L,
    val isLoading: Boolean = true,
    val isFinished: Boolean = false,
    val result: GameResult? = null
) {
    val currentRiddle: Riddle? get() = riddles.getOrNull(currentIndex)

    val totalRiddles: Int get() = riddles.size

    val isLastRiddle: Boolean get() = currentIndex == riddles.lastIndex

    val progress: Float
        get() = if (riddles.isEmpty()) 0f else (currentIndex + 1f) / riddles.size

    val hasAnswered: Boolean get() = answerState != AnswerState.Idle
}
