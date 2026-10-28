package com.example.magicnumberapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.ui.components.BrutalButton
import com.example.magicnumberapp.ui.components.BrutalTag
import com.example.magicnumberapp.ui.components.brutalShadow
import com.example.magicnumberapp.ui.theme.AppYellow
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.BrutalCardBorderWidth
import com.example.magicnumberapp.ui.theme.CardWhite
import com.example.magicnumberapp.ui.theme.EasyGreen
import com.example.magicnumberapp.ui.theme.HardRed

@Composable
fun ResultScreen(
    result: GameResult?,
    hintsUsed: Int,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppYellow
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (result == null) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "No hay resultados disponibles.",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrutalBlack
                )
                BrutalButton(text = "Volver al menú", onClick = onMenu)
                return@Column
            }

            BrutalTag(
                text = "RONDA ${result.difficulty.displayName.uppercase()} TERMINADA"
            )

            ScoreCard(score = result.correctAnswers * 2)

            HitsCard(
                correct = result.correctAnswers,
                total = result.totalRiddles
            )

            Text(
                text = hintMessage(hintsUsed),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = BrutalBlack,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            BrutalButton(
                text = "Repetir ronda",
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth(),
                containerColor = HardRed
            )
            BrutalButton(
                text = "Volver al menú",
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth(),
                containerColor = CardWhite
            )
        }
    }
}

@Composable
private fun ScoreCard(score: Int) {
    val shape = RoundedCornerShape(18.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 18.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = score.toString(),
                fontSize = 72.sp,
                lineHeight = 76.sp,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
            Text(
                text = "PUNTOS DE 10",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
        }
    }
}

@Composable
private fun HitsCard(correct: Int, total: Int) {
    val shape = RoundedCornerShape(14.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .brutalShadow(cornerRadius = 14.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = EasyGreen),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = "$correct de $total aciertos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BrutalBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        )
    }
}

private fun hintMessage(hintsUsed: Int): String = when (hintsUsed) {
    0 -> "No usaste la pista."
    1 -> "Usaste la pista en 1 adivinanza."
    else -> "Usaste la pista en $hintsUsed adivinanzas."
}
