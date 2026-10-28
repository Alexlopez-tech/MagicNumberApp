package com.example.magicnumberapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.ui.components.BrutalButton
import com.example.magicnumberapp.ui.components.brutalShadow
import com.example.magicnumberapp.ui.components.difficultyColor
import com.example.magicnumberapp.ui.theme.AppYellow
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.BrutalCardBorderWidth
import com.example.magicnumberapp.ui.theme.CardWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onStartGame: (Difficulty) -> Unit,
    onAddRiddle: () -> Unit,
    onShowHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Adivina ",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
            Box(
                modifier = Modifier
                    .background(BrutalBlack, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "lo",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppYellow
                )
            }
        }

        Text(
            text = "Resuelve adivinanzas y pon a prueba tu ingenio.",
            style = MaterialTheme.typography.bodyLarge,
            color = BrutalBlack
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "ELIGE LA DIFICULTAD",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = BrutalBlack
        )

        Difficulty.entries.forEach { difficulty ->
            DifficultyCard(
                difficulty = difficulty,
                onClick = { onStartGame(difficulty) }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BrutalButton(
                text = "Agregar adivinanza",
                onClick = onAddRiddle,
                modifier = Modifier.weight(1f),
                containerColor = CardWhite
            )
            BrutalButton(
                text = "Historial",
                onClick = onShowHistory,
                modifier = Modifier.weight(1f),
                containerColor = CardWhite
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultyCard(
    difficulty: Difficulty,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 14.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = difficultyColor(difficulty)),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = difficulty.displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
            Text(
                text = "5 adivinanzas",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = BrutalBlack
            )
        }
    }
}
