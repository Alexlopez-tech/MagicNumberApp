package com.example.magicnumberapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.magicnumberapp.ui.components.BrutalButton
import com.example.magicnumberapp.ui.components.BrutalTag
import com.example.magicnumberapp.ui.components.brutalShadow
import com.example.magicnumberapp.ui.state.GameUiState
import com.example.magicnumberapp.ui.theme.AppYellow
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.BrutalCardBorderWidth
import com.example.magicnumberapp.ui.theme.CardWhite
import com.example.magicnumberapp.ui.theme.EasyGreen
import com.example.magicnumberapp.ui.theme.HardRed
import com.example.magicnumberapp.ui.theme.MediumBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameUiState,
    onSelectOption: (Int) -> Unit,
    onConfirm: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    onRevealHint: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppYellow
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrutalBlack)
                }
            }

            state.riddles.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No hay adivinanzas para esta dificultad.",
                        style = MaterialTheme.typography.titleMedium,
                        color = BrutalBlack
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BrutalButton(text = "Volver al menú", onClick = onExit)
                }
            }

            else -> {
                val riddle = state.currentRiddle
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BrutalButton(
                            text = "Salir",
                            onClick = onExit,
                            containerColor = CardWhite,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BrutalTag(
                            text = "${state.difficulty.displayName} • " +
                                "${state.currentIndex + 1} de ${state.totalRiddles}"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ProgressSegments(
                        total = state.totalRiddles,
                        filled = state.currentIndex + 1
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        QuestionCard(
                            question = riddle?.question.orEmpty()
                        )

                        HintCard(
                            hint = riddle?.hint.orEmpty(),
                            isVisible = state.isHintVisible
                        )

                        riddle?.options?.forEachIndexed { index, option ->
                            OptionCard(
                                letter = ('A' + index).toString(),
                                text = option,
                                isSelected = state.selectedOptionIndex == index,
                                isCorrectOption = index == riddle.correctOptionIndex,
                                answered = state.hasAnswered,
                                onClick = { onSelectOption(index) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val action = when {
                        !state.hasAnswered -> onConfirm
                        state.isLastRiddle -> onFinish
                        else -> onNext
                    }
                    val label = when {
                        !state.hasAnswered -> "Confirmar respuesta"
                        state.isLastRiddle -> "Ver resultado"
                        else -> "Siguiente"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BrutalButton(
                            text = if (state.isHintVisible) "Pista revelada" else "Ver pista",
                            onClick = onRevealHint,
                            modifier = Modifier.weight(1f),
                            containerColor = MediumBlue,
                            enabled = !state.isHintVisible
                        )
                        BrutalButton(
                            text = label,
                            onClick = action,
                            modifier = Modifier.weight(1f),
                            containerColor = HardRed,
                            enabled = state.hasAnswered || state.selectedOptionIndex != null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressSegments(total: Int, filled: Int) {
    val shape = RoundedCornerShape(4.dp)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp)
                    .clip(shape)
                    .background(if (index < filled) BrutalBlack else CardWhite)
                    .border(2.dp, BrutalBlack, shape)
            )
        }
    }
}

@Composable
private fun QuestionCard(question: String) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 16.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BrutalTag(
                text = "ADIVINANZA",
                containerColor = HardRed,
                contentColor = BrutalBlack
            )
            Text(
                text = question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
        }
    }
}

@Composable
private fun HintCard(hint: String, isVisible: Boolean) {
    val shape = RoundedCornerShape(14.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 14.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MediumBlue),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = if (isVisible) {
                "Pista: ${hint.trim()}"
            } else {
                "Pista: pulsa «Ver pista» para descubrirla."
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = BrutalBlack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptionCard(
    letter: String,
    text: String,
    isSelected: Boolean,
    isCorrectOption: Boolean,
    answered: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val isCorrect = answered && isCorrectOption
    val isWrongSelection = answered && isSelected && !isCorrectOption
    val containerColor = when {
        isCorrect -> EasyGreen
        isWrongSelection -> HardRed
        isSelected -> EasyGreen
        else -> CardWhite
    }
    val borderWidth = if (isSelected && !answered) 3.dp else BrutalCardBorderWidth

    Card(
        onClick = onClick,
        enabled = !answered,
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 14.dp),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor
        ),
        border = BorderStroke(borderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(BrutalBlack, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CardWhite
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected || isCorrect) FontWeight.Bold else FontWeight.Normal,
                color = BrutalBlack,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
