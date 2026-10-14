package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LiquidGallantColorScheme = darkColorScheme(
    primary = LiquidCyan,
    onPrimary = StudioOnPrimary,
    primaryContainer = StudioPrimaryContainer,
    onPrimaryContainer = StudioOnPrimaryContainer,
    secondary = LiquidViolet,
    onSecondary = Color.White,
    secondaryContainer = StudioSecondaryContainer,
    onSecondaryContainer = Color(0xFFF3E8FF),
    tertiary = LiquidEmerald,
    onTertiary = Color(0xFF003822),
    background = LiquidDarkBg,
    onBackground = LiquidTextPrimary,
    surface = LiquidGlassSurface,
    onSurface = LiquidTextPrimary,
    surfaceVariant = Color(0xFF141829),
    onSurfaceVariant = LiquidTextSecondary,
    outline = LiquidGlassBorder,
    outlineVariant = Color(0x26FFFFFF)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LiquidGallantColorScheme,
        typography = Typography,
        content = content
    )
}
