package com.elliottsoftware.calftracker.presentation.components.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.presentation.components.login.BannerCard
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel


@Composable
fun RegisterView(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BannerCard("Calf Tracker", "Powered by Elliott Software")
        UsernameInput()
        EmailInput()
        PasswordInput()
        SubmitButton()
        //LinearLoadingBar()



    }
}

@Composable
fun UsernameInput(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        OutlinedTextField(value = "Username",
            onValueChange = { },
            singleLine = true,
            placeholder = {
                Text(text = "Email",fontSize = 26.sp)
            },
            modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
            ,
            textStyle = TextStyle(fontSize = 26.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )


        )
//        if(state.emailError != null){
//            Text(text = state.emailError,color = MaterialTheme.colors.error, modifier = Modifier.align(
//                Alignment.End))
//        }

    }
}
@Composable
fun EmailInput(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        OutlinedTextField(value = "Email",
            onValueChange = { },
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
//        if(state.emailError != null){
//            Text(text = state.emailError,color = MaterialTheme.colors.error, modifier = Modifier.align(
//                Alignment.End))
//        }

    }
}

@Composable
fun PasswordInput(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        OutlinedTextField(value = "Password",
            onValueChange = { },
            singleLine = true,
            placeholder = {
                Text(text = "Email",fontSize = 26.sp)
            },
            modifier = Modifier.padding(start = 0.dp,10.dp,0.dp,0.dp)
            ,
            textStyle = TextStyle(fontSize = 26.sp),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )


        )
//        if(state.emailError != null){
//            Text(text = state.emailError,color = MaterialTheme.colors.error, modifier = Modifier.align(
//                Alignment.End))
//        }

    }
}

@Composable
fun SubmitButton(){
    Button(onClick = {},
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text = "Register",fontSize = 26.sp)
    }
}