package com.elliottsoftware.calftracker.presentation.components.newCalf

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.main.*
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
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: NewCalfViewModel = viewModel(),onNavigate:(Int)->Unit){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
    val vaccineList = remember { mutableStateListOf<String>() }
    if(state.loggedUserOut){
        onNavigate(R.id.action_newCalfFragment_to_loginFragment)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = {

            if(viewModel.state.value.calfSaved == Response.Loading){
                LoadingFloatingButton()
            }else{
                FloatingButton(navigate = {
                    viewModel.submitCalf(vaccineList)
                })
            }
                               },
        topBar = {
            TopAppBar(
                title = { Text("Calf Tracker") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()

                            }
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


            MainBodyView(viewModel,onNavigate,scaffoldState,padding,vaccineList)





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

){
    val snackbarHostState = remember { SnackbarHostState() }

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
            is Response.Loading -> {}
            is Response.Success ->{
                if(response.data){
                    //THIS IS WHERE WE DO THE NAVIGATION
                    onNavigate(R.id.action_newCalfFragment_to_mainFragment2)
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