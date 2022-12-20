package com.elliottsoftware.calftracker.presentation.components.editCalf

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf

import com.elliottsoftware.calftracker.presentation.components.main.FloatingButton
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme

import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch

@Composable
fun EditCalfView(viewModel: EditCalfViewModel, onNavigate:(Int)->Unit){
    AppTheme(false){
        ScaffoldView(viewModel=viewModel,onNavigate = onNavigate)
    }
}

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
            FloatingButton(viewModel)
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
                        contentDescription = "logout of account",
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
        EditCalfView(viewModel,padding, onNavigate)
    }
}

@Composable
fun EditCalfView(viewModel: EditCalfViewModel,paddingValues: PaddingValues,onNavigate: (Int) -> Unit){


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

        Checkboxes(state = viewModel.uiState.value.sex, {value -> viewModel.updateSex(value) })
        
        when(val response = viewModel.uiState.value.calfUpdated){
            is Response.Loading -> LinearProgressIndicator()
            is Response.Success ->{
                if(response.data){
                    onNavigate(R.id.action_editCalfFragment_to_mainFragment2)

                }
            }
            is Response.Failure -> {Text("FAILURE")}
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


@Composable
fun FloatingButton(viewModel:EditCalfViewModel){
    FloatingActionButton(
        onClick = { viewModel.validateText() },
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









