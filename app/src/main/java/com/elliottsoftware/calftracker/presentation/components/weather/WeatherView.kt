package com.elliottsoftware.calftracker.presentation.components.weather

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.main.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.main.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.main.FloatingButton
import com.elliottsoftware.calftracker.presentation.components.main.MenuItem
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.WeatherViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun WeatherView(){
    ScaffoldView()
}





@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: WeatherViewModel = viewModel()) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

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

        }

    ) {
        Column() {
            WeatherStuff(viewModel)
            Button(onClick = { viewModel.getWeatherData() }) {
                Text("GET WEATHER DATA")
            }
        }

    }
}

@Composable
fun WeatherStuff(viewModel: WeatherViewModel){
    when(val response = viewModel.uiState.value.weatherData){
        is Response.Loading -> Text("LOADING")
        is Response.Success -> {
            val elevation = response.data.elevation
            val lat = response.data.latitude
            val long = response.data.longitude
            Text(elevation.toString())
            Text(lat.toString())
            Text(long.toString())
        }
        is Response.Failure -> Text("FAIL")
    }

}