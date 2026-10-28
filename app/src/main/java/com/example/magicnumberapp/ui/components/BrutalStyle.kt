package com.example.magicnumberapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.magicnumberapp.ui.theme.AppYellow
import com.example.magicnumberapp.ui.theme.BrutalBlack
import com.example.magicnumberapp.ui.theme.BrutalBorderWidth
import com.example.magicnumberapp.ui.theme.CardWhite

fun Modifier.brutalShadow(
    cornerRadius: Dp = 16.dp,
    offset: Dp = 4.dp
): Modifier = this
    .padding(end = offset, bottom = offset)
    .drawBehind {
        translate(left = offset.toPx(), top = offset.toPx()) {
            drawRoundRect(
                color = BrutalBlack,
                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
            )
        }
    }

@Composable
fun BrutalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = CardWhite,
    contentColor: Color = BrutalBlack,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.brutalShadow(cornerRadius = 12.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.45f),
            disabledContentColor = contentColor.copy(alpha = 0.45f)
        ),
        border = BorderStroke(BrutalBorderWidth, BrutalBlack),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = contentPadding
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BrutalTag(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = BrutalBlack,
    contentColor: Color = AppYellow,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        shape = shape,
        border = BorderStroke(BrutalBorderWidth, BrutalBlack)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
