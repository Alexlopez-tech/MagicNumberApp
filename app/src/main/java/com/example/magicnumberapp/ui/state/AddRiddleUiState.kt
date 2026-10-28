package com.example.magicnumberapp.ui.state

import com.example.magicnumberapp.data.model.Difficulty

data class AddRiddleErrors(
    val question: String? = null,
    val hint: String? = null,
    val options: String? = null
)

data class AddRiddleUiState(
    val question: String = "",
    val hint: String = "",
    val options: List<String> = List(OPTION_COUNT) { "" },
    val correctOptionIndex: Int = 0,
    val difficulty: Difficulty = Difficulty.EASY,
    val errors: AddRiddleErrors = AddRiddleErrors(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
) {
    val canSave: Boolean
        get() = question.isNotBlank() &&
            hint.isNotBlank() &&
            options.all { it.isNotBlank() } &&
            options.map { it.trim().lowercase() }.distinct().size == options.size

    companion object {
        const val OPTION_COUNT = 4
    }
}
