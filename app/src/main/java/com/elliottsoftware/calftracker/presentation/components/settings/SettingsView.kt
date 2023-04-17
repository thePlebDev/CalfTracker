package com.elliottsoftware.calftracker.presentation.components.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.presentation.components.main.SearchText
import com.elliottsoftware.calftracker.presentation.theme.AppTheme

@Composable
fun SettingsView(){
    AppTheme(false){
        Settings()
    }
}

@Composable
fun Settings(){
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = { TopBar() }
    ){ paddingValues ->
        MainView(paddingValues)
    }
}

val settingsList = listOf("Bulls","Subscriptions")
@Composable
fun MainView(paddingValues: PaddingValues){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ){
        LazyVerticalGridDemo()
    }
}

@Composable
fun LazyVerticalGridDemo(){

}
@Composable
fun TopBar(){
    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                Icon(
                    modifier = Modifier.size(38.dp).weight(1f),
                    imageVector = Icons.Default.KeyboardBackspace,
                    contentDescription ="Return to home screen",
                )
                Text("Settings", fontSize = 30.sp,modifier = Modifier.weight(2f))
            }



        }
    }
}

