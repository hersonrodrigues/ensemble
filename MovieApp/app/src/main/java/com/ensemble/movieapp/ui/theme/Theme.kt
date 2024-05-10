package com.ensemble.movieapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.ensemble.movie.ui.theme.Purple200
import com.ensemble.movie.ui.theme.Purple500
import com.ensemble.movie.ui.theme.Purple700
import com.ensemble.movie.ui.theme.Teal200

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    onSurfaceVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    onSurfaceVariant = Purple700,
    secondary = Teal200
)

@Composable
fun EnsembleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}