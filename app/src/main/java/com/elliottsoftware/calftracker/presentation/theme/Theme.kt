package com.elliottsoftware.calftracker.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = Color(0xFFbfd5ef),
    primaryVariant = Color(0xFFf2ffff),
    onPrimary = Black2,
    secondary = Color(0xFFefd8bf),
    secondaryVariant = Color(0xFFfffff2),
    onSecondary = Black2,
    error = RedErrorDark,
    onError = RedErrorLight,

)

private val DarkThemeColors = darkColors(
    primary = Color(0xFF102840),
    primaryVariant = Color(0xFF00001a),
    onPrimary = Color.White,
    secondary = Color(0xFF402810),
    secondaryVariant = Color(0xFF200000),
    onSecondary = Color.White,
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