package com.dosbots.flixme.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Teal100,
    secondary = Pink100,
    surface = DarkGray100,
    surfaceVariant = DarkGray100,
    onSurfaceVariant = White100,
    background = Black100,
    onPrimary = DarkGray100,
    onSecondary = DarkGray100,
    onBackground = White100,
    onSurface = White100,
    error = Red100
)

private val LightColorScheme = lightColorScheme(
    primary = Teal100,
    secondary = Pink100,
    surface = White100,
    surfaceVariant = Teal25.copy(alpha = 0.4f),
    onSurfaceVariant = DarkGray100,
    background = Teal5,
    onPrimary = DarkGray100,
    onSecondary = DarkGray100,
    onBackground = DarkGray100,
    onSurface = DarkGray100,
    error = Red100
)

@Composable
fun FlixmeUi(
    dimensions: Dimensions = DefaultDimensions,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    CompositionLocalProvider(
        LocalDimens provides dimensions
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}