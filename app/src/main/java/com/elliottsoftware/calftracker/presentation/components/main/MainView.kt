package com.elliottsoftware.calftracker.presentation.components.main

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R


@Composable
fun MainView(){




}

@Composable
fun DrawerHeader(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Calf Tracker",fontSize=40.sp, textAlign = TextAlign.Center)
    }
}
@Composable
fun DrawerBody(
    items:List<MenuItem>,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick:(MenuItem) -> Unit,

    ){
    LazyColumn(){
        items(items){item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(16.dp)
            ){
                Icon(imageVector = item.icon, contentDescription = item.contentDescription )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title,style=itemTextStyle,modifier = Modifier.weight(1f))
            }
        }
    }

}

@Composable
fun AppBar (
    onNavigationIconClick: () -> Unit
){
    TopAppBar(
        title = {
            Text(text = "Calf Tracker")
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle navigation drawer" )
            }
        }
    )


}


data class MenuItem(
    val id:String,
    val title:String,
    val contentDescription:String,
    val icon: ImageVector
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: MainViewModel = viewModel(),onNavigate: (Int) -> Unit){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
    if(state.loggedUserOut){
        onNavigate(R.id.action_mainFragment2_to_loginFragment)
    }
    Scaffold(

        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
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
                }
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
                        icon = Icons.Default.Logout
                    )
                ),
                onItemClick = {
                    when(it.id){
                        "logout"->{
                            scope.launch {
                                scaffoldState.drawerState.close()
                                viewModel.signUserOut()

                            }
                        }
                    }
                }
            )
        },

        ) {}
}