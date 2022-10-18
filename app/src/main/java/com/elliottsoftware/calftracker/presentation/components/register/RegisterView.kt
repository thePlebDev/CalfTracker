package com.elliottsoftware.calftracker.presentation.components.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.presentation.components.login.BannerCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.RegisterViewModel


@Composable
fun RegisterView(viewModel: RegisterViewModel = viewModel()){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BannerCard("Calf Tracker", "Powered by Elliott Software")
        UsernameInput(viewModel)
        EmailInput(viewModel)
        PasswordInput(viewModel)
        SubmitButton(viewModel)
        //LinearLoadingBar()



    }
}

@Composable
fun UsernameInput(viewModel: RegisterViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val state = viewModel.state.value

        OutlinedTextField(value = state.username,
            onValueChange = { viewModel.updateUsername(it)},
            singleLine = true,
            placeholder = {
                Text(text = "Username",fontSize = 26.sp)
            },
            modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
            ,
            textStyle = TextStyle(fontSize = 26.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )


        )
        if(state.usernameError != null){
            Text(text = state.usernameError,color = MaterialTheme.colors.error, modifier = Modifier.align(
                Alignment.End))
        }

    }
}
@Composable
fun EmailInput(viewModel: RegisterViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val state = viewModel.state.value

        OutlinedTextField(value = state.email,
            onValueChange = { viewModel.updateEmail(it)},
            singleLine = true,
            placeholder = {
                Text(text = "Email",fontSize = 26.sp)
            },
            modifier = Modifier.padding(start = 0.dp,10.dp,0.dp,0.dp)
            ,
            textStyle = TextStyle(fontSize = 26.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )

        )
        if(state.emailError != null){
            Text(text = state.emailError,color = MaterialTheme.colors.error, modifier = Modifier.align(
                Alignment.End))
        }

    }
}

@Composable
fun PasswordInput(viewModel: RegisterViewModel){
    val state = viewModel.state.value

    val icon = if(state.passwordIconChecked)
        painterResource(id = R.drawable.design_ic_visibility)
    else
        painterResource(id = R.drawable.design_ic_visibility_off)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(value = state.password,
            onValueChange = {viewModel.updatePassword(it)},
            placeholder = { Text(text = "Password", fontSize = 26.sp) },
            modifier = Modifier.padding(start = 0.dp, 10.dp, 0.dp, 0.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = state.passwordError != null,
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.passwordIconChecked(!state.passwordIconChecked)
                }) {
                    Icon(painter = icon, contentDescription = "Visibility Icon")
                }
            },
            visualTransformation = if (state.passwordIconChecked) VisualTransformation.None
            else PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 26.sp)
        )
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

}

@Composable
fun SubmitButton(viewModel: RegisterViewModel){
    Button(onClick = {viewModel.submitButton()},
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text = "Register",fontSize = 26.sp)
    }
}