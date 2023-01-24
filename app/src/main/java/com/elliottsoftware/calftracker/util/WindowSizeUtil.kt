package com.elliottsoftware.calftracker.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration


data class WindowSizeUtil(
    val width: WindowType,
    val height: WindowType
)

enum class WindowType { Compact, Medium, Expanded }

@Composable
fun rememberWindowSize(): WindowSizeUtil {
    val configuration = LocalConfiguration.current
    val screenWidth by remember(key1 = configuration) { //if the configuration changes, compute a new mutableStateOf value
        mutableStateOf(configuration.screenWidthDp)
    }
    val screenHeight by remember(key1 = configuration) {
        mutableStateOf(configuration.screenHeightDp)
    }

    return WindowSizeUtil(
        width = getScreenWidth(screenWidth),
        height = getScreenHeight(screenHeight)
    )
}

fun getScreenWidth(width: Int): WindowType = when {
    width < 600 -> WindowType.Compact
    width < 840 -> WindowType.Medium
    else -> WindowType.Expanded
}

fun getScreenHeight(height: Int): WindowType = when {
    height < 480 -> WindowType.Compact
    height < 900 -> WindowType.Medium
    else -> WindowType.Expanded
}