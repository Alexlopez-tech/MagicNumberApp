package com.example.magicnumberapp.data.model

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class GameResult(
    val id: String,
    val timestamp: Long,
    val difficulty: Difficulty,
    val correctAnswers: Int,
    val totalRiddles: Int,
    val durationMillis: Long
) {
    val accuracy: Float
        get() = if (totalRiddles == 0) 0f else correctAnswers.toFloat() / totalRiddles

    val scorePercent: Int
        get() = (accuracy * 100).roundToInt()
}

data class GameStats(
    val gamesPlayed: Int = 0,
    val totalCorrect: Int = 0,
    val totalWrong: Int = 0
) {
    val accuracyPercent: Int
        get() {
            val total = totalCorrect + totalWrong
            return if (total == 0) 0 else (totalCorrect * 100) / total
        }
}
