package com.elliottsoftware.calftracker.presentation.components.editCalf

import android.annotation.SuppressLint
import android.os.Build
import android.util.Range
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf

import com.elliottsoftware.calftracker.presentation.components.main.FloatingButton
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
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
fun EditCalfView(viewModel: EditCalfViewModel,paddingValues: PaddingValues,onNavigate: (Int) -> Unit,scaffoldState: ScaffoldState,){



    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(paddingValues)) {
        SimpleText(state = viewModel.uiState.value.calfTagNumber,
            "Calf Tag",viewModel.uiState.value.testTextError,
            { value -> viewModel.updateCalfTag(value) })

        SimpleText(state = viewModel.uiState.value.cowTagNumber,
            "Cow Tag",errorMessage = null,
            { value -> viewModel.updateCowTag(value) })

        SimpleText(state = viewModel.uiState.value.cCIANUmber,
            "CCIA number",errorMessage = null,
            { value -> viewModel.updateCCIANumber(value) })

        SimpleText(state = viewModel.uiState.value.description,
            "Description",errorMessage = null,
            { value -> viewModel.updateDescription(value) })

        NumberTextInput(state = viewModel.uiState.value.birthWeight,
            "Birth Weight",
            { value -> viewModel.updateBirthWeight(value) })


        CalendarDock(viewModel.uiState.value.birthDate!!,
            selectedAction = { date -> viewModel.updateDate(date)},
            modifier = Modifier.fillMaxWidth()
        )


        Checkboxes(state = viewModel.uiState.value.sex, {value -> viewModel.updateSex(value) })



        
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

    }


}



@Composable
fun SimpleText(
    state: String,
    placeHolderText:String,
    errorMessage: String? = null,
    updateValue: (String) -> Unit

){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            //state
            value = state,
            onValueChange = { updateValue(it) },
            //style
            singleLine = true,
            placeholder = {
                Text(text = placeHolderText, fontSize = 20.sp)
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            //errors
            isError = errorMessage!=null,
            trailingIcon = {
                if(errorMessage!=null){
                    Icon(Icons.Filled.Error, "error has occurred", tint = MaterialTheme.colors.error)
                }

            }

        )
        if(errorMessage!=null){
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )

        }
    }

}

@Composable
fun NumberTextInput(
    state: String,
    placeHolderText:String,
    updateValue: (String) -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            //state
            value = state,
            onValueChange = { updateValue(it) },
            //style
            singleLine = true,
            placeholder = {
                Text(text = placeHolderText, fontSize = 20.sp)
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

        )
    }
}

@Composable
fun Checkboxes(
    state:String,
    updateSex: (String) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(selected = state=="Bull", onClick = {updateSex("Bull") })
        Text(
            text = "Bull" ,
            modifier = Modifier
                .clickable(onClick = { })
                .padding(start = 4.dp)
        )

        RadioButton(selected = state=="Heifer", onClick = { updateSex("Heifer")})
        Text(
            text = "Heifer",
            modifier = Modifier
                .clickable(onClick = { })
                .padding(start = 4.dp)
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






