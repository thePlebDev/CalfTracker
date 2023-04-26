package com.elliottsoftware.calftracker.presentation.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Propane
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.elliottsoftware.calftracker.R
data class NavigationItems(
    val title:String,
    val contentDescription:String,
    val navigationDestination:Int,
    val icon:ImageVector,
    val key:Int,

    )
val modalItem = listOf(
    NavigationItems(
        title ="Settings",
        contentDescription = "navigate to settings",
        navigationDestination = R.id.action_subscriptionFragment_to_settingsFragment,
        icon = Icons.Default.Settings,
        key =0
    ),
    NavigationItems(
        title ="Calves",
        contentDescription = "navigate to calves screen",
        navigationDestination = R.id.action_subscriptionFragment_to_mainFragment22,
        icon = Icons.Default.Home,
        key =1
    ),
)

sealed class Navigation(val navList:List<NavigationItems>){
    object Modal:Navigation(modalItem)
}