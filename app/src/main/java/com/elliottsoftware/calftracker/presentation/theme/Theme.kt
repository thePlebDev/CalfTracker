package com.elliottsoftware.calftracker.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = primaryLight,
    primaryVariant = primaryLightVariant,
    onPrimary = Black2,
    secondary = lightSecondary,
    secondaryVariant = lightSecondaryVariant,
    onSecondary = Black2,
    error = RedErrorDark,
    onError = RedErrorLight,

)

private val DarkThemeColors = darkColors(
    primary = primaryDark,
    primaryVariant = primaryDarkVariant,
    onPrimary = White2,
    secondary = darkSecondary,
    secondaryVariant = darkSecondaryVariant,
    onSecondary = White2,
    error = RedErrorLight,
    onError = RedErrorLight,
    //surface = Color(0xFF3c506b),


)

@Composable
fun AppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        content= content
    )
}