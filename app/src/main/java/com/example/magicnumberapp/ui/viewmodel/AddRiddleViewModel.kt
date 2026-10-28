package com.example.magicnumberapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.data.model.Riddle
import com.example.magicnumberapp.data.repository.RiddleRepository
import com.example.magicnumberapp.ui.state.AddRiddleErrors
import com.example.magicnumberapp.ui.state.AddRiddleUiState
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRiddleViewModel(private val repository: RiddleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddRiddleUiState())
    val uiState: StateFlow<AddRiddleUiState> = _uiState.asStateFlow()

    fun updateQuestion(value: String) {
        _uiState.update { it.copy(question = value, errors = it.errors.copy(question = null)) }
    }

    fun updateHint(value: String) {
        _uiState.update { it.copy(hint = value, errors = it.errors.copy(hint = null)) }
    }

    fun updateOption(index: Int, value: String) {
        _uiState.update { state ->
            if (index !in state.options.indices) return@update state
            val options = state.options.toMutableList().apply { this[index] = value }
            state.copy(options = options, errors = state.errors.copy(options = null))
        }
    }

    fun selectCorrectOption(index: Int) {
        _uiState.update { state ->
            if (index !in state.options.indices) state
            else state.copy(correctOptionIndex = index)
        }
    }

    fun updateDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun save() {
        val state = _uiState.value
        if (state.isSaving) return

        val question = state.question.trim()
        val hint = state.hint.trim()
        val options = state.options.map { it.trim() }

        val questionError = when {
            question.isBlank() -> "Escribe la pregunta"
            question.length < 5 -> "Debe tener al menos 5 caracteres"
            else -> null
        }
        val hintError = when {
            hint.isBlank() -> "Escribe una pista"
            hint.length < 5 -> "Debe tener al menos 5 caracteres"
            else -> null
        }
        val optionsError = when {
            options.any { it.isBlank() } -> "Completa las 4 opciones"
            options.map { it.lowercase() }.distinct().size != options.size ->
                "Las opciones deben ser diferentes"
            else -> null
        }

        if (questionError != null || hintError != null || optionsError != null) {
            _uiState.update {
                it.copy(
                    errors = AddRiddleErrors(
                        question = questionError,
                        hint = hintError,
                        options = optionsError
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isSaving = true, errors = AddRiddleErrors()) }

        viewModelScope.launch {
            repository.addRiddle(
                Riddle(
                    id = UUID.randomUUID().toString(),
                    question = question,
                    options = options,
                    correctOptionIndex = state.correctOptionIndex,
                    difficulty = state.difficulty,
                    hint = hint
                )
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }

    companion object {
        fun factory(repository: RiddleRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    AddRiddleViewModel(repository) as T
            }
    }
}
