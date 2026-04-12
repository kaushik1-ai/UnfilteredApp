package com.example.unfilteredapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryIndigo,
    secondary = SecondaryPink,
    tertiary = AccentTeal,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = OnBackgroundDark,
    onSecondary = OnBackgroundDark,
    onTertiary = OnBackgroundDark,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark,
    error = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    secondary = SecondaryPink,
    tertiary = AccentTeal,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = OnBackgroundLight,
    onSecondary = OnBackgroundLight,
    onTertiary = OnBackgroundLight,
    onBackground = OnBackgroundLight,
    onSurface = OnSurfaceLight,
    error = ErrorRed
)

@Composable
fun UnfilteredAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}