package com.elliottsoftware.calftracker.presentation.components.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.presentation.components.main.SearchText
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.presentation.sharedViews.NavigationItem
import com.elliottsoftware.calftracker.presentation.sharedViews.BottomNavigation
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import timber.log.Timber

@Composable
fun SettingsView(
    onNavigate: (Int) -> Unit,
    viewModel: MainViewModel = viewModel()
){
    AppTheme(false){
        Settings(
            onNavigate,
            viewModel
        )
    }
}

@Composable
fun Settings(
    onNavigate: (Int) -> Unit,
    viewModel: MainViewModel
){
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = { TopBar(onNavigate) },
        bottomBar = {
            BottomNavigation(
                navItemList = listOf(
                    NavigationItem(
                        title = "Logout",
                        contentDescription = "Logout Button",
                        icon = Icons.Outlined.Logout,
                        onClick = {
                           // viewModel.signUserOut()
                            Timber.tag("clickers").d("LOGOUT NAVIGATE")
                            onNavigate(R.id.action_settingsFragment_to_loginFragment2)

                        },
                        color = Color.Black,


                        ),
                    NavigationItem(
                        title = "Home",
                        contentDescription = "Navigate back to home page",
                        icon = Icons.Outlined.Home,
                        onClick = {
                            onNavigate(R.id.action_settingsFragment_to_mainFragment2)

                        },
                        color = Color.Black

                        ),
                    NavigationItem(
                        title = "Create",
                        contentDescription = "Launch the create Calf widget",
                        icon = Icons.Default.AddCircle,
                        onClick = {
                           // onNavigate(R.id.action_mainFragment2_to_settingsFragment)
                        },
                        color = Color.Black,
                        weight = 1.3f,
                        modifier = Modifier.size(45.dp)

                        ),
                    NavigationItem(
                        title = "Settings",
                        contentDescription = "Navigate to the settings page",
                        icon = Icons.Default.Settings,
                        onClick = {},
                        color = Color.Black

                    ),
                    NavigationItem(
                        title = "Features",
                        contentDescription = "navigate to the subscriptions page. You can view and modifier your subscriptions",
                        icon = Icons.Outlined.MonetizationOn,
                        onClick = {
                            // onNavigate(R.id.action_mainFragment2_to_settingsFragment)
                        },
                        color = Color.Black

                    )

                )
            )
        },
    ){ paddingValues ->
        MainView(paddingValues,onNavigate)
    }
}

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
                    .padding(8.dp)
                    .clickable { onNavigate(R.id.action_settingsFragment_to_subscriptionFragment) },
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
                        .weight(1f),
                        //.clickable { onNavigate(R.id.action_settingsFragment_to_mainFragment2) },
                    imageVector = Icons.Default.KeyboardBackspace,
                    contentDescription ="Return to home screen",
                )
                Text("Settings", fontSize = 30.sp,modifier = Modifier.weight(2f))
            }
        }
    }
}



