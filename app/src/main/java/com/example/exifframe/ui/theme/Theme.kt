package com.example.exifframe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = MainText,
    onPrimary = Color.White,
    background = AppBackground,
    onBackground = MainText,
    surface = CanvasBackground,
    onSurface = MainText
)

@Composable
fun FotoNoteTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
