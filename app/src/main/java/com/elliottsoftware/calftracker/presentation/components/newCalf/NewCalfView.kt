package com.elliottsoftware.calftracker.presentation.components.newCalf

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.presentation.components.main.*
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import kotlinx.coroutines.launch

@Composable
fun NewCalfView() {
    ScaffoldView()
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: NewCalfViewModel = viewModel()){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
//    if(state.loggedUserOut){
//       // onNavigate(R.id.action_mainFragment2_to_loginFragment)
//    }
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = { FloatingButton(navigate = {}) },
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
                        id= "logout",
                        title="Logout",
                        contentDescription = "logout of account",
                        icon = Icons.Default.Logout
                    )
                ),
                onItemClick = {
                    when(it.id){
                        "logout"->{
                            scope.launch {


                            }
                        }
                    }
                }
            )
        },

        ) {
        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())) {

            TextInput(
                viewModel.state.value.calfTag,
                "Calf tag number",
                updateValue = { value -> viewModel.updateCalfTag(value) }
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {

                Checkboxes("Bull")
                Checkboxes("Heifer")
            }


        }

    }
}

@Composable
fun TextInput(
    state: String,
    placeHolderText: String,
    updateValue: (String) -> Unit,

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
            keyboardType = KeyboardType.Text
        ),


        )
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
fun Checkboxes(sex:String){
    Column() {
        RadioButton(selected = true, onClick = { })
        Text(
            text = sex,
            modifier = Modifier
                .clickable(onClick = { })
                .padding(start = 4.dp)
        )
    }
}