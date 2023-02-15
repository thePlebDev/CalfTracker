package com.elliottsoftware.calftracker.presentation.components.newCalf

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                    viewModel.submitCalf()
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

        ) {

        MainBodyView(viewModel,onNavigate,scaffoldState,scope)

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainBodyView(
    viewModel: NewCalfViewModel,
    onNavigate: (Int) -> Unit,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
){
    val snackbarHostState = remember { SnackbarHostState() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())


    ) {

        TextInput(
            viewModel.state.value.calfTag,
            "Calf tag number",
            updateValue = { value -> viewModel.updateCalfTag(value) },
            errorMessage = viewModel.state.value.calfTagError
        )
        TextInput(
            viewModel.state.value.cowTagNumber,
            "Cow tag number",
            updateValue = { value -> viewModel.updateCowTagNumber(value) }
        )
        TextInput(
            viewModel.state.value.cciaNumber,
            "Ccia number",
            updateValue = { value -> viewModel.updateCciaNumber(value) }
        )
        TextInput(
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
        Checkboxes(viewModel.state.value.sex, updateSex = {value -> viewModel.updateSex(value)})








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

@Composable
fun TextInput(
    state: String,
    placeHolderText: String,
    updateValue: (String) -> Unit,
    errorMessage:String? = null

    ){
 val icon = painterResource(id = R.drawable.ic_error_24)
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
        OutlinedTextField(
            value = state,
            isError = errorMessage != null,
            trailingIcon = {
                if (errorMessage != null)
                    Icon(painter = icon, contentDescription = "Error")
            },
            onValueChange = { updateValue(it) },
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


            )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
@Composable
fun NumberInput(
    placeHolderText:String,
    state:String,
    updateValue: (String) -> Unit
){


    OutlinedTextField(value = state,

        onValueChange = { updateValue(it)},
        singleLine = true,
        placeholder = {
            Text(text = placeHolderText,fontSize = 20.sp)
        },

        modifier = Modifier
            .padding(start = 10.dp, 10.dp, 10.dp, 0.dp)
            .fillMaxWidth()

        ,
        textStyle = TextStyle(fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),


        )

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
            text = "Bull",
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
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).clickable { calendarState.show() }
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