package com.elliottsoftware.calftracker.presentation.components.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import com.elliottsoftware.calftracker.presentation.viewModels.WeatherViewModel
import com.elliottsoftware.calftracker.util.*
import com.google.accompanist.permissions.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.*


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherView(){
    ScaffoldView()
    val context = LocalContext.current
    val activity = context.findActivity()


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
//            Button(onClick = { viewModel.getWeatherData() }) {
//                Text("GET WEATHER DATA")
//            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherStuff(viewModel: WeatherViewModel){



      //  HorizontalScrollScreen()


    HorizontalScrollScreen()


}


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HorizontalScrollScreen(viewModel: WeatherViewModel = viewModel()) {
    // replace with your items...
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    Column(modifier = Modifier.background(Color(0xFF102840))) {
        // a wrapper to fill the entire screen
        // BowWithConstraints will provide the maxWidth used below

            Card(modifier = Modifier
                .weight(2f)
                .fillMaxSize()
                .padding(10.dp)
                .background(Color(0xFF1B3B5A))) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1B3B5A)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = viewModel.uiState.value.focusedWeatherData.time ,
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
                    //goes here

                    if (locationPermissionState.status.isGranted) {


                        PermissionText("",locationPermissionState,modifier = Modifier.align(Alignment.CenterHorizontally)) //show if the permission is granted
                    } else {
                        Column {
                            val textToShow = if (locationPermissionState.status.shouldShowRationale) {
                                // If the user has denied the permission but the rationale can be shown,
                                // then gently explain why the app requires this permission
                                "Location is required in this app. Please request permission."
                            } else {
                                // If it's the first time the user lands on this feature, or the user
                                // doesn't want to be asked again for this permission, explain that the
                                // permission is required
                                "This weather application requires your location to function properly. " +
                                        "Please grant the permission in settings"
                            }

                            PermissionText(textToShow,locationPermissionState, modifier = Modifier.align(Alignment.CenterHorizontally))

                        }
                    }
                }


            }
            

        Box(modifier = Modifier
            .weight(1f)
            .background(Color(0xFF102840)),
            contentAlignment = Alignment.TopEnd

        ) {
            // LazyRow to display your items horizontally

            LazyRowComposable(locationPermissionState)

            //end of row
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LazyRowComposable(locationPermissionState:PermissionState,viewModel: WeatherViewModel = viewModel()){
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        state = rememberLazyListState()
    ) {

        if(locationPermissionState.status.isGranted){
            /********* GET THE ACTUAL DATA************/

            when(val response = viewModel.uiState.value.weatherData){
                is Response.Loading -> {
                    viewModel.setFocusedData(WeatherViewData("Loading...",0.00))

                    val list = listOf<String>("","")
                    itemsIndexed(list) { index, item ->
                        CardShownShimmer()
                    }
                }
                is Response.Success -> {
                    Log.d("SUCCESSS",response.data.size.toString())

                    itemsIndexed(response.data) { index, item ->
                        CardShown(item.time.substring(11),item.temperature)
                    }

                }
                is Response.Failure -> {
                    viewModel.setFocusedData(WeatherViewData("Error, please try again",0.00))

                    val list = listOf<String>("","")
                    itemsIndexed(list) { index, item ->
                        CardShownShimmer()
                    }
                }
            }

        }else{


            val list = listOf<String>("","")
            itemsIndexed(list) { index, item ->
                CardShownShimmer()
            }

        }


    }

}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionText(text:String,permission: PermissionState,modifier: Modifier,viewModel: WeatherViewModel = viewModel()){
    Spacer(modifier=Modifier.height(16.dp))
    if (permission.status.isGranted){
        Text(
            text= viewModel.uiState.value.focusedWeatherData.temperature.toString() +"°C",
            fontSize=50.sp,
            color = Color.White,
            modifier = modifier
        )
        Spacer(modifier=Modifier.height(5.dp))
    }else{
        Text(
            text=text,
            color = Color.White,
            modifier = modifier
        )
        Spacer(modifier=Modifier.height(5.dp))
        Button(
            onClick = { permission.launchPermissionRequest()},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF102840),
                contentColor = Color.White),
            modifier = modifier
        ) {
            Text("Request permission")
        }
    }




}

@Composable
fun CardShown(time: String, temperature: Double,viewModel: WeatherViewModel = viewModel()){


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
            .clickable {
                focusRequester.requestFocus()
                viewModel.setFocusedData(WeatherViewData(time, temperature))
            } // makes it all work




    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(time, color = Color.White,textAlign = TextAlign.Center,style = MaterialTheme.typography.h6) // card's content
        }
    }

}

/****************************SHIMMER ANIMATION*************************************/
@Composable
fun CardShownShimmer(){


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
            .clickable {
                focusRequester.requestFocus()
            } // makes it all work





    ) {
        Box(contentAlignment = Alignment.Center) {
             // card's content
            GradientShimmer()
        }
    }

}

@Composable
fun GradientShimmer(){
    val colors = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.9f)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val translateAnim by infiniteTransition.animateFloat( //register the transition
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = linearGradient(
        colors,
        start = Offset.Zero,
        end = Offset(x=translateAnim,y=translateAnim)
    )
    Surface(shape = MaterialTheme.shapes.small) {
        Spacer(modifier = Modifier
            .fillMaxSize()
            .background(brush = brush))
    }
}




