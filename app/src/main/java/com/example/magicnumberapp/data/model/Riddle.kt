package com.example.magicnumberapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Riddle(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val hint: String = ""
) {
    val correctOption: String get() = options.getOrElse(correctOptionIndex) { "" }
}
