package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val StorybookColorScheme = lightColorScheme(
    primary = CoralBerry,
    onPrimary = Color.White,
    primaryContainer = CardPeach,
    onPrimaryContainer = CocoaInk,
    secondary = MeadowMint,
    onSecondary = Color.White,
    secondaryContainer = CardMint,
    onSecondaryContainer = CocoaInk,
    tertiary = SunburstYellow,
    onTertiary = CocoaInk,
    tertiaryContainer = CardLemon,
    onTertiaryContainer = CocoaInk,
    background = CreamSkyTop,
    onBackground = CocoaInk,
    surface = WarmWhite,
    onSurface = CocoaInk,
    surfaceVariant = CardSky,
    onSurfaceVariant = CocoaInkMuted,
    outline = Color(0xFFE6D5B8)
)

val StorybookShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun TebakHewanTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StorybookColorScheme,
        typography = AppTypography,
        shapes = StorybookShapes,
        content = content
    )
}
