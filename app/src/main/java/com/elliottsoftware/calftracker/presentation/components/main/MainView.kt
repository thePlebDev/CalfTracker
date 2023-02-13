package com.elliottsoftware.calftracker.presentation.components.main

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainView(
    viewModel: MainViewModel = viewModel(),
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,


){
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

    Scaffold(

        backgroundColor = MaterialTheme.colors.primary,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = { FloatingButton(onNavigate) },
        topBar = {
            CustomTopBar(viewModel.state.value.chipText,{tagNumber -> viewModel.searchCalfListByTag(tagNumber)},scope,scaffoldState)
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
                                viewModel.signUserOut()
                                onNavigate(R.id.action_mainFragment2_to_loginFragment)
                                scaffoldState.drawerState.close()


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
                  

                )
            )
            //END OF THE DRAWER BODY
           // ThemeToggle()
        },

        ) {


        HomeView(viewModel,onNavigate,sharedViewModel,viewModel.state.value.data,
            {chipText -> viewModel.setChipText(chipText)},
            {viewModel.getCalves()}
        )

    }
}




@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeView(
    viewModel: MainViewModel,
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    state:Response<List<FireBaseCalf>>,
    setChipTextMethod:(data:List<FireBaseCalf>) -> Unit,
    errorRefreshMethod:()->Unit
){



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)


    ){
        when(state){
            is Response.Loading -> CircularProgressIndicator( color =MaterialTheme.colors.onPrimary)
            is Response.Success -> {

                setChipTextMethod(state.data)
                if(state.data.isEmpty()){
                    Column(){
                        Text(text = "NO CALVES",color =MaterialTheme.colors.onPrimary)

                        //NO CALVES
                    }
                }
                else{



                        MessageList(state.data, viewModel, onNavigate, sharedViewModel)

                }

            }
            is Response.Failure -> ErrorResponse(refreshMethod = { errorRefreshMethod() })

        }


    }

}




@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MessageList(
    calfList: List<FireBaseCalf>,
    viewModel: MainViewModel,
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    //deleteCalfMethod:(String) -> Unit

    ) {

    LazyColumn(modifier=Modifier.background(MaterialTheme.colors.primary)) {


        items(calfList,key = { it.id!! }) { calf ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if(it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart){
                       viewModel.deleteCalf(calf.id!!)
                        //deleteCalfMethod(calf.id!!)

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

                        Text(calf.calftag!!,style=typography.h6,color=MaterialTheme.colors.onSecondary, textAlign = TextAlign.Start,maxLines = 1, overflow = TextOverflow.Ellipsis)
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

@Composable
fun CustomTopBar(chipTextList:List<String>,searchMethod:(String)->Unit, scope: CoroutineScope, scaffoldState: ScaffoldState){
    //TODO: MOVE THIS TO THE MAIN FRAGMENT
//    val viewSize = rememberWindowSize()
//    val value = viewSize.width



    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            elevation = 8.dp
        ) {
            Column() {


                 SearchText(searchMethod= { tagNumber -> searchMethod(tagNumber) },scope,scaffoldState)
                    //CHIPS GO BELOW HERE
                    LazyRow(
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(chipTextList){
                            Chip(it)
                        }
                    }






            }


        }
        
    }
}

@Composable
fun SearchText(searchMethod:(String)->Unit,scope: CoroutineScope, scaffoldState: ScaffoldState){
    var tagNumber by remember { mutableStateOf("") }
    var clicked by remember { mutableStateOf(false)}
    val source = remember {
        MutableInteractionSource()
    }
    val focusManager = LocalFocusManager.current

    if ( source.collectIsPressedAsState().value){
        clicked = true
    }
    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            scope.launch { scaffoldState.drawerState.open() }
        }) {
            Icon(Icons.Filled.Menu, contentDescription = "Toggle navigation drawer")
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = {Text("Search by tag number")},
            value = tagNumber, onValueChange = {tagNumber = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            trailingIcon = {
                if(clicked){
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Clear search icon",
                        modifier = Modifier.clickable {
                            tagNumber = ""
                            searchMethod("")
                            focusManager.clearFocus()
                        }
                    )
                }else{
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                }

            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchMethod(tagNumber)
                    focusManager.clearFocus()
                }),
            interactionSource = source,
        )
    }
}


@Composable
fun Chip(value:String){
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colors.secondary
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(8.dp)
        )

    }
}


/**************ERRORS*****************/
@Composable
fun ErrorResponse(refreshMethod:()->Unit) {
    Card(backgroundColor = MaterialTheme.colors.secondary,modifier = Modifier.padding(vertical = 20.dp)) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text("User Notice", style = MaterialTheme.typography.h4)
            Text("A Error has occurred and the development team has been notified. Thank you for your continued support ",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center
            )
            ErrorButton(refreshMethod)
        }
    }

}

@Composable
fun ErrorButton(refreshMethod:()->Unit) {
    Button(onClick = {
        refreshMethod()
    }) {
        Text(text = "Click to reload",
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center)
    }
}

/**************SCAFFOLD*****************/
@Composable
fun ScaffoldView(){

}





