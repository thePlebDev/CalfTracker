package com.elliottsoftware.calftracker.presentation.components.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel

@Composable
fun LoginView(viewModel: LoginViewModel = viewModel()) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BannerCard("Calf Tracker", "powered by Elliott Software")
        EmailInput(viewModel)
//        PasswordInput(viewModel,state)
        SubmitButton(viewModel)


    }
}

@Composable
fun BannerCard(banner: String,bannerDescription:String) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(banner,fontSize = 40.sp,
            fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 0.dp,16.dp,0.dp,0.dp)
        )
        Text(bannerDescription,fontSize = 18.sp, fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,)

    }
}

@Composable
fun EmailInput(loginViewModel: LoginViewModel){
    val state = loginViewModel.state.value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(value = state.email,
            onValueChange = {loginViewModel.updateEmail(it)},
            singleLine = true,
            placeholder = {
                Text(text = "Email",fontSize = 26.sp)
            },
            modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
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
fun SubmitButton(loginViewModel: LoginViewModel){
    Button(onClick = {loginViewModel.submitButton() },
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text = "Login",fontSize = 26.sp)
    }
}