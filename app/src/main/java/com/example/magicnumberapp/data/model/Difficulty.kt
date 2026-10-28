package com.example.magicnumberapp.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class Difficulty(val displayName: String) {
    EASY("Fácil"),
    MEDIUM("Media"),
    HARD("Difícil")
}
