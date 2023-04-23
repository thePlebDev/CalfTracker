package com.elliottsoftware.calftracker.presentation.components.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.presentation.components.main.SearchText
import com.elliottsoftware.calftracker.presentation.theme.AppTheme

@Composable
fun SettingsView(onNavigate: (Int) -> Unit){
    AppTheme(false){
        Settings(onNavigate)
    }
}

@Composable
fun Settings(onNavigate: (Int) -> Unit){
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = { TopBar(onNavigate) }
    ){ paddingValues ->
        MainView(paddingValues,onNavigate)
    }
}

val settingsList = listOf("Bulls","Subscriptions")
@Composable
fun MainView(paddingValues: PaddingValues,onNavigate: (Int) -> Unit){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ){
        LazyVerticalGridDemo(onNavigate)
    }
}


@Composable
fun LazyVerticalGridDemo(onNavigate: (Int) -> Unit){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        item{
            Card(
                modifier = Modifier
                    .padding(8.dp).clickable { onNavigate(R.id.action_settingsFragment_to_subscriptionFragment) },
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = 8.dp,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 4.dp),

                ){
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "click for subscriptions",
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Subscription")
                }

            }


        }
    }

}
@Composable
fun TopBar(onNavigate: (Int) -> Unit){
    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                Icon(
                    modifier = Modifier
                        .size(38.dp)
                        .weight(1f).clickable { onNavigate(R.id.action_settingsFragment_to_mainFragment2) },
                    imageVector = Icons.Default.KeyboardBackspace,
                    contentDescription ="Return to home screen",
                )
                Text("Settings", fontSize = 30.sp,modifier = Modifier.weight(2f))
            }
        }
    }
}


/***********************BELOW IS ALL THE EXIT AND ENTER ANIMATION STUFF I PLAYED AROUND WITH************************************************************/
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullScreenNotification() {
    val aspectRatio: Float by animateFloatAsState(6f)
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(), exit = fadeOut()
    ) {
        // Fade in/out the background and foreground
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x88000000))) {
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .animateEnterExit(
                        // Slide in/out the rounded rect
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .requiredHeight(100.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Content of the notification goes here
                Text("hellow",
                    fontSize=40.sp,
                    modifier = Modifier
                        .animateEnterExit(enter = scaleIn(), exit = scaleOut())
                        .aspectRatio(aspectRatio)
                )

            }
        }
    }
}



