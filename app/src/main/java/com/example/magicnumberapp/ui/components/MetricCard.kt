package com.example.magicnumberapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.BrutalCardBorderWidth
import com.example.magicnumberapp.ui.theme.CardWhite

@Composable
fun MetricCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = CardWhite
) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        modifier = modifier.brutalShadow(cornerRadius = 16.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(BrutalCardBorderWidth, BrutalBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = BrutalBlack
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = BrutalBlack,
                textAlign = TextAlign.Center
            )
        }
    }
}
