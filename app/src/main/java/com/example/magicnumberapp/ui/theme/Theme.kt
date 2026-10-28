package com.example.magicnumberapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BrutalBlack,
    onPrimary = CardWhite,
    primaryContainer = AppYellow,
    onPrimaryContainer = BrutalBlack,
    secondary = EasyGreen,
    onSecondary = BrutalBlack,
    tertiary = MediumBlue,
    onTertiary = BrutalBlack,
    background = AppYellow,
    onBackground = BrutalBlack,
    surface = CardWhite,
    onSurface = BrutalBlack,
    surfaceVariant = CardWhite,
    onSurfaceVariant = BrutalBlack,
    surfaceContainer = CardWhite,
    surfaceContainerHigh = CardWhite,
    surfaceContainerLow = AppYellow,
    outline = BrutalBlack,
    outlineVariant = BrutalBlack,
    error = HardRed,
    onError = BrutalBlack,
    errorContainer = HardRed,
    onErrorContainer = BrutalBlack,
    scrim = BrutalBlack
)

@Composable
fun MagicNumberAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
