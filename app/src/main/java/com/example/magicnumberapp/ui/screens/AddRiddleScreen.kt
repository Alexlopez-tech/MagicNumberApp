package com.example.magicnumberapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.ui.components.BrutalButton
import com.example.magicnumberapp.ui.state.AddRiddleUiState
import com.example.magicnumberapp.ui.theme.AppYellow
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.CardWhite
import com.example.magicnumberapp.ui.theme.HardRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRiddleScreen(
    state: AddRiddleUiState,
    onQuestionChange: (String) -> Unit,
    onHintChange: (String) -> Unit,
    onOptionChange: (Int, String) -> Unit,
    onCorrectOptionChange: (Int) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    Scaffold(
        modifier = modifier,
        containerColor = AppYellow
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BrutalButton(text = "Volver", onClick = onBack)
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Nueva adivinanza",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrutalBlack
                )
            }

            val fieldShape = RoundedCornerShape(12.dp)

            OutlinedTextField(
                value = state.question,
                onValueChange = onQuestionChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text("ADIVINANZA") },
                placeholder = { Text("¿Qué tiene manos pero no aplaude?") },
                isError = state.errors.question != null,
                supportingText = {
                    state.errors.question?.let { error -> Text(error) }
                },
                minLines = 2,
                maxLines = 4,
                shape = fieldShape,
                colors = brutalTextFieldColors()
            )

            OutlinedTextField(
                value = state.hint,
                onValueChange = onHintChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text("PISTA") },
                placeholder = { Text("Tiene agujas y marca las horas.") },
                isError = state.errors.hint != null,
                supportingText = {
                    state.errors.hint?.let { error -> Text(error) }
                },
                minLines = 2,
                maxLines = 3,
                shape = fieldShape,
                colors = brutalTextFieldColors()
            )

            Text(
                text = "OPCIONES (MARCA LA CORRECTA)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = BrutalBlack
            )

            state.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.correctOptionIndex == index,
                        onClick = { onCorrectOptionChange(index) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BrutalBlack,
                            unselectedColor = BrutalBlack
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    OutlinedTextField(
                        value = option,
                        onValueChange = { onOptionChange(index, it) },
                        modifier = Modifier.weight(1f),
                        label = { Text("Opción ${('A' + index)}") },
                        singleLine = true,
                        isError = state.errors.options != null,
                        shape = fieldShape,
                        colors = brutalTextFieldColors()
                    )
                }
            }

            state.errors.options?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = HardRed
                )
            }

            DifficultyDropdown(
                difficulty = state.difficulty,
                onDifficultyChange = onDifficultyChange
            )

            Spacer(modifier = Modifier.height(4.dp))

            BrutalButton(
                text = if (state.isSaving) "Guardando..." else "Guardar adivinanza",
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                containerColor = HardRed,
                enabled = state.canSave && !state.isSaving,
                leadingIcon = if (state.isSaving) {
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = BrutalBlack,
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultyDropdown(
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = difficulty.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("DIFICULTAD") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            shape = shape,
            colors = brutalTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Difficulty.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onDifficultyChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun brutalTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BrutalBlack,
    unfocusedBorderColor = BrutalBlack,
    focusedLabelColor = BrutalBlack,
    unfocusedLabelColor = BrutalBlack,
    cursorColor = BrutalBlack,
    focusedTextColor = BrutalBlack,
    unfocusedTextColor = BrutalBlack,
    focusedContainerColor = CardWhite,
    unfocusedContainerColor = CardWhite
)
