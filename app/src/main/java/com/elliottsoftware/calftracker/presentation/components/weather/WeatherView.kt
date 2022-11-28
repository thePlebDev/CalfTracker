package com.elliottsoftware.calftracker.presentation.components.weather

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import com.elliottsoftware.calftracker.presentation.viewModels.WeatherViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherView(){
    ScaffoldView()
}





@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherStuff(viewModel: WeatherViewModel){
    when(val response = viewModel.uiState.value.weatherData){
        is Response.Loading -> Text("LOADING")
        is Response.Success -> {

            HorizontalScrollScreen(response.data)
        }
        is Response.Failure -> Text("FAIL")
    }

      //  HorizontalScrollScreen()



}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HorizontalScrollScreen(data: MutableList<WeatherViewData>) {
    // replace with your items...

    Column(modifier = Modifier.background(Color(0xFF102840))) {
        val items = (1..168).map { "Item $it" }
        // a wrapper to fill the entire screen
        // BowWithConstraints will provide the maxWidth used below

            Card(modifier = Modifier
                .weight(2f)
                .fillMaxSize()
                .padding(10.dp)
                .background(Color(0xFF1B3B5A))) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(Color(0xFF1B3B5A)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Today 18:00",
                        modifier= Modifier
                            .padding(10.dp)
                            .align(Alignment.End),
                        color = Color.White)
                    Spacer(modifier=Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_cloudy ),
                        contentDescription = "cloudy icon",
                        modifier = Modifier.width(200.dp)
                    )
                    Spacer(modifier=Modifier.height(16.dp))
                    Text(
                        text="12.4 °C",
                        fontSize=50.sp,
                        color = Color.White
                    )
                }


            }
            


        Box(modifier = Modifier
            .weight(1f)
            .background(Color(0xFF102840)),
            contentAlignment = Alignment.TopEnd

        ) {
            // LazyRow to display your items horizontally


            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                state = rememberLazyListState()
            ) {

                itemsIndexed(data) { index, item ->
                    CardShown(item.time.substring(11))
                }

            }
            //end of row
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
    val isFocused = interactionSource.collectIsFocusedAsState().value //recomp cause


    val extraPadding by animateDpAsState(
        targetValue = if (isFocused ) 20.dp else 40.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Card(
        backgroundColor = Color(0xFF1B3B5A),
        modifier = Modifier
            .focusRequester(focusRequester) //register focus changes
            .focusable(interactionSource = interactionSource) //emit focus events
            .height(200.dp)
            .width(180.dp)
            .background(Color(0xFF102840), shape = RoundedCornerShape(4.dp))
            .padding(extraPadding.coerceAtLeast(0.dp))
            .clickable { focusRequester.requestFocus() } // makes it all work





    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(item, color = Color.White,textAlign = TextAlign.Center,style = MaterialTheme.typography.h6) // card's content
        }
    }

}