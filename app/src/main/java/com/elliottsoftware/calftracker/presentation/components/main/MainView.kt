package com.elliottsoftware.calftracker.presentation.components.main

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.WeatherViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//TODO: NEED TO ADD THE SEARCH FUNCTIONALITY
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainView(viewModel: MainViewModel = viewModel(),onNavigate: (Int) -> Unit,sharedViewModel: EditCalfViewModel){
    AppTheme(false){
        ScaffoldView(viewModel,onNavigate,sharedViewModel)
    }


}


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: MainViewModel = viewModel(),onNavigate: (Int) -> Unit,sharedViewModel: EditCalfViewModel){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
    if(state.loggedUserOut){
        onNavigate(R.id.action_mainFragment2_to_loginFragment)
    }
    Scaffold(

        backgroundColor = MaterialTheme.colors.primary,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = { FloatingButton(onNavigate) },
        topBar = {
            TopAppBar(
                title = { Text("Calf Tracker") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Toggle navigation drawer")
                    }
                },
                actions = {}
            )
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id= "logout",
                        title="Logout",
                        contentDescription = "logout of account",
                        icon = Icons.Default.Logout,
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.close()
                                viewModel.signUserOut()

                            }
                        }
                    ),
                    MenuItem(
                        id= "weather",
                        title="Weather",
                        contentDescription = "Weather",
                        icon = Icons.Default.Satellite,
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.close()
                                onNavigate(R.id.action_mainFragment2_to_weatherFragment)

                            }
                        }
                    ),
                    MenuItem(
                        id= "stats",
                        title="Stats",
                        contentDescription = "Statistics page",
                        icon = Icons.Default.Info,
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.close()
                                onNavigate(R.id.action_mainFragment2_to_calfStatsFragment)

                            }
                        }
                    )

                )
            )
            //END OF THE DRAWER BODY
           // ThemeToggle()
        },

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)


        ){
            when(val response = state.data){
                is Response.Loading -> CircularProgressIndicator( color =MaterialTheme.colors.onPrimary)
                is Response.Success -> {
                    if(response.data.isEmpty()){
                        Text(text = "NO CALVES",color =MaterialTheme.colors.onPrimary)
                    }else{
                        MessageList(response.data,viewModel,onNavigate,sharedViewModel)
                    }


                }
                is Response.Failure -> Text("FAIL")
            }

        }


    }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MessageList(
    calfList:List<FireBaseCalf>,
    viewModel: MainViewModel,
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel
) {
    val dateFormat = SimpleDateFormat("yyyy-mm-dd")

    LazyColumn(modifier=Modifier.background(MaterialTheme.colors.primary)) {
        items(calfList,key = { it.id!! }) { calf ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if(it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart){
                        viewModel.deleteCalf(calf.id!!)

                    }

                    true
                }
            )
            SwipeToDismiss(
                state = dismissState,
                background = {
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> MaterialTheme.colors.primary
                            else -> Color.Red
                        }
                    )
                    val alignment = Alignment.CenterEnd
                    val icon = Icons.Default.Delete

                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = Dp(20f)),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Delete Icon",
                            modifier = Modifier.scale(scale)
                        )
                    }
                }

            //END OF BACKGROUND
            ){
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                //    .background(MaterialTheme.colors.primary) /*******THIS IS WHAT IS COLORING THE CORNERS********/
                    .clickable {
                        sharedViewModel.setCalf(calf)
                        onNavigate(R.id.action_mainFragment2_to_editCalfFragment)
                               }
                ,
                elevation = 2.dp,
                backgroundColor = MaterialTheme.colors.secondary, /******THIS IS WHAT I CHANGED*******/
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            ){
                Row(
                    modifier = Modifier.padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Column(modifier = Modifier.weight(2f)){

                        Text(calf.calfTag!!,style=typography.h6,color=MaterialTheme.colors.onSecondary, textAlign = TextAlign.Start,maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(calf.details!!,style=typography.subtitle1,color=MaterialTheme.colors.onSecondary,maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Column(modifier = Modifier.weight(1f)){


                        Text(DateFormat.getDateInstance().format(calf.date),style=typography.subtitle1,color=MaterialTheme.colors.onSecondary,)
                        Text(calf.sex!!,style=typography.subtitle1,color=MaterialTheme.colors.onSecondary,)
                    }
                    
                }


            }
        }
        }

    }
}

@Composable
fun FloatingButton(navigate:(Int)-> Unit){
    FloatingActionButton(
        onClick = { navigate(R.id.action_mainFragment2_to_newCalfFragment) },
        backgroundColor = MaterialTheme.colors.secondary,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary
            )
        }
    )
}
