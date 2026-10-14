package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val PureGlassDarkColorScheme = darkColorScheme(
    primary = LiquidAccentBlue,
    onPrimary = StudioOnPrimary,
    primaryContainer = StudioPrimaryContainer,
    onPrimaryContainer = StudioOnPrimaryContainer,
    secondary = LiquidAccentCyan,
    onSecondary = Color.White,
    secondaryContainer = StudioSecondaryContainer,
    onSecondaryContainer = Color.White,
    tertiary = LiquidAccentGreen,
    onTertiary = Color.White,
    background = LiquidBgDark,
    onBackground = LiquidTextWhite,
    surface = LiquidSurfaceDark,
    onSurface = LiquidTextWhite,
    surfaceVariant = Color(0xFF161B26),
    onSurfaceVariant = LiquidTextGray,
    outline = LiquidBorderGlass,
    outlineVariant = LiquidBorderGlassSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PureGlassDarkColorScheme,
        typography = Typography,
        content = content
    )
}
