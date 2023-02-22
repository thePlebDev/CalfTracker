package com.elliottsoftware.calftracker.presentation.components.editCalf

import android.annotation.SuppressLint
import android.os.Build
import android.util.Range
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response

import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.sharedViews.BullHeiferRadioInput
import com.elliottsoftware.calftracker.presentation.sharedViews.NumberInput
import com.elliottsoftware.calftracker.presentation.sharedViews.SimpleTextInput
import com.elliottsoftware.calftracker.presentation.sharedViews.VaccinationView
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle


import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
//modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).clickable { calendarState.show() }
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCalfView(viewModel: EditCalfViewModel, onNavigate:(Int)->Unit){
    AppTheme(false){
        ScaffoldView(viewModel=viewModel,onNavigate = onNavigate)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScaffoldView(viewModel: EditCalfViewModel, onNavigate:(Int)->Unit) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState.value




    if (state.loggedUserOut) {
        onNavigate(R.id.action_editCalfFragment_to_loginFragment)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = {
            if(viewModel.uiState.value.calfUpdated == Response.Loading){
                LoadingFloatingButton()
            }else{
                FloatingButton(viewModel)
            }
        },
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

        EditCalfView(viewModel,padding, onNavigate,scaffoldState)



    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCalfView(
    viewModel: EditCalfViewModel,
    paddingValues: PaddingValues,
    onNavigate: (Int) -> Unit,
    scaffoldState: ScaffoldState,

){



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        SimpleTextInput(
            state = viewModel.uiState.value.calfTagNumber,
            placeHolderText = "Calf Tag",
            errorMessage = viewModel.uiState.value.testTextError,
            updateValue =  { value -> viewModel.updateCalfTag(value) }
        )

        SimpleTextInput(
            state = viewModel.uiState.value.cowTagNumber,
            placeHolderText = "Cow Tag",
            errorMessage = null,
            updateValue = { value -> viewModel.updateCowTag(value) }
        )

        SimpleTextInput(
            state = viewModel.uiState.value.cCIANUmber,
            placeHolderText = "CCIA number",
            errorMessage = null,
            updateValue = { value -> viewModel.updateCCIANumber(value) }
        )

        SimpleTextInput(
            state = viewModel.uiState.value.description,
            placeHolderText="Description",
            errorMessage = null,
            updateValue = { value -> viewModel.updateDescription(value) }
        )



        NumberInput(
            state = viewModel.uiState.value.birthWeight,
            placeHolderText = "Birth Weight",
            updateValue = { value -> viewModel.updateBirthWeight(value) }
        )


        CalendarDock(viewModel.uiState.value.birthDate!!,
            selectedAction = { date -> viewModel.updateDate(date)},
            modifier = Modifier.fillMaxWidth()
        )


        BullHeiferRadioInput(
            state = viewModel.uiState.value.sex,
            updateSex = {value -> viewModel.updateSex(value) }
        )




        
        when(val response = viewModel.uiState.value.calfUpdated){
            is Response.Loading ->{}
            is Response.Success ->{
                if(response.data){
                    viewModel.updateCalfUpdatedStateToFalse()
                    onNavigate(R.id.action_editCalfFragment_to_mainFragment2)

                }
            }
            is Response.Failure -> {
                LaunchedEffect(scaffoldState.snackbarHostState) {

                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Error! Please try again",
                        actionLabel = "Close"
                    )
                }
            }
        }

        //this will only run if vaccineList is not null
        /******************THIS RUNNING ON RECOMPOSITION IS CAUSING A PROBLEM***************************/
//        // I think this needs to happen inside the viewModel
//        viewModel.uiState.value.vaccineList?.let{ calfVaccineList ->
//            for(vaccine in calfVaccineList){
//                vaccineList.add(vaccine)
//            }
//        }
        VaccinationView(
            vaccineText = viewModel.uiState.value.vaccineText,
            updateVaccineText = {text -> viewModel.updateVaccineText(text) },
            dateText1 = viewModel.uiState.value.vaccineDate,
            updateDateText = {date -> viewModel.updateDateText(date)},
            vaccineList = viewModel.uiState.value.vaccineList,
            addItemToVaccineList = {item -> viewModel.addItemToVaccineList(item)},
            removeItemFromVaccineList = {item -> viewModel.removeItemToVaccineList(item)}
        )

    }


}








@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDock(dateBorn: Date,selectedAction:(LocalDate)->Unit,modifier: Modifier = Modifier){

    val convertedDate = dateBorn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    val selectedDate = remember { mutableStateOf<LocalDate?>(convertedDate) }
    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date(selectedDate = selectedDate.value){ newDate ->

            selectedDate.value = newDate
            selectedAction(newDate)

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
            modifier = modifier,
            textStyle = TextStyle(fontSize = 20.sp),


            )
    }


}

/********BUTTONS*********/
@Composable
fun FloatingButton(viewModel:EditCalfViewModel){
    FloatingActionButton(
        onClick = {
            viewModel.validateText()

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

@Composable
fun LoadingFloatingButton(){
    FloatingActionButton(
        onClick = {


        },
        backgroundColor = MaterialTheme.colors.secondary,
        content = {

            CircularProgressIndicator(
                color= MaterialTheme.colors.onSecondary
            )
        }
    )
}






