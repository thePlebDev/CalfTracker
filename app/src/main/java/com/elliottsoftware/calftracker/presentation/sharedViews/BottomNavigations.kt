package com.elliottsoftware.calftracker.presentation.sharedViews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



data class NavigationItem(
    val title:String,
    val contentDescription:String,
    val icon: ImageVector,
    val onClick: () -> Unit ={},
    val color: Color,
    val weight:Float = 1f,
    val modifier: Modifier = Modifier

)

@Composable
fun BottomNavItem(
    modifier: Modifier = Modifier.size(34.dp),
    title:String,
    description:String,
    icon: ImageVector,
    onNavigate: () -> Unit,
    iconColor: Color = Color.Black,

    ){

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                onNavigate()
            }

    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = modifier,
            tint = iconColor
        )
        Text(title, fontSize = 8.sp,)

    }
}

@Composable
fun BottomNavigation(
    navItemList:List<NavigationItem>
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceAround

    ){

        navItemList.forEach {
            Box(
                modifier = Modifier.weight(it.weight),
                contentAlignment = Alignment.Center
            ){
                BottomNavItem(
                    title = it.title,
                    description = it.contentDescription,
                    icon = it.icon,
                    onNavigate = {
                        it.onClick()
                    },
                    iconColor = it.color,
                    modifier = it.modifier

                )
            }
        }
    }
}