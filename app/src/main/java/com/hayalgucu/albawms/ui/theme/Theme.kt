package com.hayalgucu.albawms.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/*private val LightColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.Gray,
    background = Color.DarkGray,
    secondary = Orange500,
    surface = Color.White,
    onSurface = Color.Black,
    onPrimary = Color.White
)*/

private val LightColorPalette = darkColors(
    primary = Color.Red,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface
)


@Composable
fun AlbaWMSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        content = content
    )
}