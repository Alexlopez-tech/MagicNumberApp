package com.example.magicnumberapp.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale as ComposeLocale
import androidx.compose.ui.unit.dp
import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.ui.components.BrutalButton
import com.example.magicnumberapp.ui.components.BrutalTag
import com.example.magicnumberapp.ui.components.MetricCard
import com.example.magicnumberapp.ui.components.brutalShadow
import com.example.magicnumberapp.ui.components.difficultyColor
import com.example.magicnumberapp.ui.state.HistoryUiState
import com.example.magicnumberapp.ui.theme.AppYellow
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.BrutalCardBorderWidth
import com.example.magicnumberapp.ui.theme.CardWhite
import com.example.magicnumberapp.ui.theme.EasyGreen
import com.example.magicnumberapp.ui.theme.MediumBlue
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun HistoryScreen(
    state: HistoryUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppYellow
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrutalBlack)
            }
            return@Scaffold
        }

        val averageScore = remember(state.results) {
            if (state.results.isEmpty()) {
                0f
            } else {
                state.results.sumOf { it.correctAnswers * 2 }.toFloat() / state.results.size
            }
        }
        val locale = ComposeLocale.current.platformLocale

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrutalButton(text = "Volver", onClick = onBack)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Historial",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrutalBlack
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        value = state.stats.gamesPlayed.toString(),
                        label = "Rondas jugadas",
                        modifier = Modifier.weight(1f),
                        containerColor = EasyGreen
                    )
                    MetricCard(
                        value = String.format(locale, "%.1f", averageScore),
                        label = "Promedio de puntaje",
                        modifier = Modifier.weight(1f),
                        containerColor = MediumBlue
                    )
                }
            }

            if (state.results.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Todavía no hay rondas",
                            style = MaterialTheme.typography.titleMedium,
                            color = BrutalBlack
                        )
                        Text(
                            text = "Juega tu primera ronda para ver tu progreso aquí.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = BrutalBlack
                        )
                    }
                }
            } else {
                item {
                    Text(
                        text = "RONDAS RECIENTES",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrutalBlack,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                items(items = state.results, key = { it.id }) { result ->
                    HistoryCard(result = result)
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(result: GameResult) {
    val locale = ComposeLocale.current.platformLocale
    val dateFormat = remember(locale) {
        SimpleDateFormat("dd MMM yyyy · HH:mm", locale)
    }
    val shape = RoundedCornerShape(14.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 14.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BrutalTag(
                    text = result.difficulty.displayName,
                    containerColor = difficultyColor(result.difficulty),
                    contentColor = BrutalBlack
                )
                Text(
                    text = dateFormat.format(Date(result.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = BrutalBlack
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "${result.correctAnswers * 2} pts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
        }
    }
}
