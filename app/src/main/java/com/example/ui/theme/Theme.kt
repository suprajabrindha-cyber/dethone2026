package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BentoBluePrimary,
    secondary = BentoBlueLight,
    tertiary = BentoLavenderLight,
    error = AlertRed,
    background = BentoCanvasBG,
    surface = BentoCardWhite,
    onPrimary = BrightWhite,
    onSecondary = BentoTextNavy,
    onBackground = BentoTextDark,
    onSurface = BentoTextDark,
    surfaceVariant = BentoBorderLight
)

private val LightColorScheme = lightColorScheme(
    primary = BentoBluePrimary,
    secondary = BentoBlueLight,
    tertiary = BentoLavenderLight,
    error = AlertRed,
    background = BentoCanvasBG,
    surface = BentoCardWhite,
    onPrimary = BrightWhite,
    onSecondary = BentoTextNavy,
    onBackground = BentoTextDark,
    onSurface = BentoTextDark,
    surfaceVariant = BentoBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Set default to false to showcase the bright clean Bento Grid theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
