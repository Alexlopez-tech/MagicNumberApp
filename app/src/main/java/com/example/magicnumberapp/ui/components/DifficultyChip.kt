package com.example.magicnumberapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.CardWhite
import com.example.magicnumberapp.ui.theme.EasyGreen
import com.example.magicnumberapp.ui.theme.HardRed
import com.example.magicnumberapp.ui.theme.MediumBlue

fun difficultyColor(difficulty: Difficulty): Color = when (difficulty) {
    Difficulty.EASY -> EasyGreen
    Difficulty.MEDIUM -> MediumBlue
    Difficulty.HARD -> HardRed
}

@Composable
fun DifficultyChip(
    difficulty: Difficulty,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = difficultyColor(difficulty)
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = difficulty.displayName,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = CardWhite,
            labelColor = BrutalBlack,
            selectedContainerColor = accent,
            selectedLabelColor = BrutalBlack
        ),
        border = BorderStroke(2.dp, BrutalBlack)
    )
}
