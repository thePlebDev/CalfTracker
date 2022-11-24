package com.elliottsoftware.calftracker.presentation.components.weather

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
//    when(val response = viewModel.uiState.value.weatherData){
//        is Response.Loading -> Text("LOADING")
//        is Response.Success -> {
//
//            HorizontalScrollScreen()
//        }
//        is Response.Failure -> Text("FAIL")
//    }

        HorizontalScrollScreen()



}

@Composable
fun HorizontalScrollScreen() {
    // replace with your items...
    var bigTap by remember { mutableStateOf(false) }

    Column() {
        val items = (1..168).map { "Item $it" }
        // a wrapper to fill the entire screen
        // BowWithConstraints will provide the maxWidth used below
        Box(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .background(Color.Blue, shape = RoundedCornerShape(4.dp))
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(Color.Red, shape = RoundedCornerShape(4.dp))) {

            }
            
        }

        Box(modifier = Modifier
            .weight(1f)
            .background(Color.Blue, shape = RoundedCornerShape(4.dp))
        ) {
            // LazyRow to display your items horizontally
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                state = rememberLazyListState()
            ) {

                itemsIndexed(items) { index, item ->
                    CardShown(item)
                }

            }
        }
    }

}


@Composable
fun CardShown(item: String){

    // initialize focus reference to be able to request focus programmatically
    val focusRequester = remember { FocusRequester() }
// MutableInteractionSource to track changes of the component's interactions (like "focused")
    val interactionSource = remember { MutableInteractionSource() }
    // text below will change when we focus it via button click
    val isFocused = interactionSource.collectIsFocusedAsState().value



    val extraPadding by animateDpAsState(
        if (!isFocused ) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Card(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .height(300.dp)
            .width(160.dp)
            .padding(extraPadding.coerceAtLeast(0.dp))
            .clickable {focusRequester.requestFocus()}


    ) {
        Text(item) // card's content
    }
}