package com.elliottsoftware.calftracker.presentation.components.newCalf

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.main.*
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionValues
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.sharedViews.*
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.CoroutineScope


import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewCalfView(onNavigate:(Int)->Unit) {
    AppTheme(false){
        ScaffoldView(onNavigate = onNavigate)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: NewCalfViewModel = viewModel(),onNavigate:(Int)->Unit){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
    val vaccineList = remember { mutableStateListOf<String>() }
    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    if(state.loggedUserOut){
        onNavigate(R.id.action_newCalfFragment_to_loginFragment)
    }
    ModalBottomSheetLayout(
        sheetState = bottomModalState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth().fillMaxHeight()

            ){

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size(60.dp)
                )
            }
        },
        sheetBackgroundColor = Color.Gray.copy(alpha = .7f)
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            floatingActionButton = {

                //todo: this will go inside the Create Calf Item
//            if(viewModel.state.value.calfSaved == Response.Loading){
//                LoadingFloatingButton()
//            }else{
//                FloatingButton(navigate = {
//                    viewModel.submitCalf(vaccineList)
//                })
//            }
            },
            bottomBar = {
                BottomNavigation(
                    onNavigate = { location -> onNavigate(location) },
                    navItemList = listOf(
                        NavigationItem(
                            title = "Logout",
                            contentDescription = "Logout Button",
                            icon = Icons.Default.Logout,
                            onClick = { viewModel.signUserOut() },
                            navigationLocation = R.id.action_newCalfFragment_to_loginFragment

                        ),
                        NavigationItem(
                            title = "Create",
                            contentDescription = "Create Calf",
                            icon = Icons.Default.AddCircle,
                            onClick = {
                                    viewModel.submitCalf(vaccineList)
                            },
                            navigationLocation = R.id.action_newCalfFragment_to_mainFragment2

                        ),
                        NavigationItem(
                            title = "Settings",
                            contentDescription = "Settings Button",
                            icon = Icons.Default.Settings,
                            onClick = {},
                            navigationLocation = R.id.action_newCalfFragment_to_loginFragment

                        )

                    )
                )
            },
            topBar = {
                TopBar(
                    onNavigate = { item -> onNavigate(item) }
                )
            },
            drawerContent = {
                DrawerHeader()
                DrawerBody(
                    items = listOf(
                        MenuItem(
                            id = "logout",
                            title = "Logout",
                            contentDescription = "Logout",
                            icon = Icons.Default.Logout,
                            onClick = {
                                scope.launch {
                                    viewModel.signUserOut()
                                    scaffoldState.drawerState.close()


                                }
                            }
                        )
                    )
                )
            },

            ) { padding ->

            //this should get danced to the MainBodyView
            MainBodyView(
                viewModel,
                onNavigate,
                scaffoldState,
                padding,
                vaccineList,
                showModal = {
                    scope.launch {
                        bottomModalState.show()
                    }
                },
                hideModal = {
                    scope.launch {
                        bottomModalState.hide()
                    }
                }

            )


        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainBodyView(
    viewModel: NewCalfViewModel,
    onNavigate: (Int) -> Unit,
    scaffoldState: ScaffoldState,
    padding: PaddingValues,
    vaccineList: MutableList<String>,
    showModal:()->Unit,
    hideModal:()->Unit

){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .verticalScroll(rememberScrollState())


    ) {

        SimpleTextInput(
            state= viewModel.state.value.calfTag,
            placeHolderText = "Calf tag number",
            updateValue = { value -> viewModel.updateCalfTag(value) },
            errorMessage = viewModel.state.value.calfTagError
        )
        SimpleTextInput(
            viewModel.state.value.cowTagNumber,
            "Cow tag number",
            updateValue = { value -> viewModel.updateCowTagNumber(value) }
        )
        SimpleTextInput(
            viewModel.state.value.cciaNumber,
            "Ccia number",
            updateValue = { value -> viewModel.updateCciaNumber(value) }
        )
        SimpleTextInput(
            viewModel.state.value.description,
            "Description",
            updateValue = { value -> viewModel.updateDescription(value) }
        )

        NumberInput(
            "Birth Weight",
            viewModel.state.value.birthWeight,
            updateValue = {value -> viewModel.updateBirthWeight(value)}
        )

        /**CALANDAR STUFF**/
        CalendarDock(viewModel)

        BullHeiferRadioInput(
            state = viewModel.state.value.sex,
            updateSex = {value -> viewModel.updateSex(value) },
            modifier = Modifier
        )

        VaccinationView(
            vaccineText = viewModel.state.value.vaccineText,
            updateVaccineText = {text -> viewModel.updateVaccineText(text) },
            dateText1 = viewModel.state.value.vaccineDate,
            updateDateText = {date -> viewModel.updateDateText(date)},
            vaccineList = vaccineList,
            addItemToVaccineList = {item -> vaccineList.add(item)},
            removeItemFromVaccineList = {item -> vaccineList.remove(item)}
        )


        when(val response = viewModel.state.value.calfSaved){
            is Response.Loading -> {
                showModal()
            }
            is Response.Success ->{
                if(response.data){
                    //THIS IS WHERE WE DO THE NAVIGATION
                    hideModal()

                    LaunchedEffect(scaffoldState.snackbarHostState) {

                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Calf Created",
                            actionLabel = "Close"
                        )
                    }
                }
            }
            is Response.Failure ->{

                LaunchedEffect(scaffoldState.snackbarHostState) {

                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Error! Please try again",
                        actionLabel = "Close"
                    )
                }

            }
        }



    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDock(viewModel: NewCalfViewModel, ){
    val selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date(selectedDate = selectedDate.value){ newDate ->

            selectedDate.value = newDate
            viewModel.updateDate(newDate)

        }
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { calendarState.show() }
    ) {
        OutlinedTextField(
            //state
            enabled = false,
            value = "Date born: "+ selectedDate.value.toString(),
            onValueChange = {  },
            //style
            singleLine = true,
            placeholder = {
                Text(text = "Date", fontSize = 20.sp)
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),


            )
    }

}

@Composable
fun FloatingButton(
    navigate:()-> Unit,showSheetState:()-> Unit = {},
){
    // val scope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = {
            navigate()
        },
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




/*********BUTTONS**************/
@Composable
fun LoadingFloatingButton(){
    FloatingActionButton(
        onClick = {},
        backgroundColor = MaterialTheme.colors.secondary,
        content = {

            CircularProgressIndicator(
                color= MaterialTheme.colors.onSecondary
            )
        }
    )
}


data class NavigationItem(
    val title:String,
    val contentDescription:String,
    val icon: ImageVector,
    val onClick: () -> Unit ={},
    val navigationLocation:Int,

    )

@Composable
fun BottomNavigation(onNavigate: (Int) -> Unit,navItemList:List<NavigationItem>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceAround

    ){
        navItemList.forEach {
            com.elliottsoftware.calftracker.presentation.components.main.BottomNavItem(
                title = it.title,
                description = it.contentDescription,
                icon = it.icon,
                onNavigate = {
                    it.onClick()
                    //onNavigate(it.navigationLocation)
                }

            )
        }
    }
}

@Composable
fun BottomNavItem(title:String, description:String, icon: ImageVector, onNavigate: () -> Unit,){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onNavigate() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(28.dp)
        )
        Text(title)

    }
}


@Composable
fun TopBar(
    onNavigate: (Int) -> Unit
){
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
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    modifier = Modifier
                        .size(38.dp)
                        .padding(start = 8.dp)
                        .clickable {
                            onNavigate(R.id.action_newCalfFragment_to_mainFragment2)
                        },
                    imageVector = Icons.Default.KeyboardBackspace,
                    contentDescription ="Return to home screen",
                )
            }
    }
}

