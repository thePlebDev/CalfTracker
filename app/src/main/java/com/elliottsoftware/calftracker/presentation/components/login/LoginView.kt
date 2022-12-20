package com.elliottsoftware.calftracker.presentation.components.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.elliottsoftware.calftracker.R.drawable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import com.elliottsoftware.calftracker.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.register.Fail
import com.elliottsoftware.calftracker.presentation.components.register.Success
import com.elliottsoftware.calftracker.presentation.components.weather.ScaffoldView
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel

@Composable
fun LoginViews(viewModel: LoginViewModel = viewModel(),onNavigate: (Int) -> Unit){
    AppTheme(false){
        LoginView( viewModel = viewModel,onNavigate= onNavigate)
    }
}

@Composable
fun LoginView(viewModel: LoginViewModel = viewModel(),onNavigate: (Int) -> Unit) {
    if(viewModel.state.value.isUserLoggedIn){
        onNavigate(R.id.action_loginFragment_to_mainFragment2)
    }else{
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BannerCard("Calf Tracker", "Powered by Elliott Software")
            EmailInput(viewModel)
            PasswordInput(viewModel)
            SubmitButton(viewModel,onNavigate)
            SignUpForgotPassword(onNavigate)
            when(val response = viewModel.state.value.loginStatus){
                is Response.Loading -> LinearLoadingBar()
                is Response.Success -> {
                    if(response.data){
                        //THIS IS WHERE WE WOULD DO THE NAVIGATION
                        onNavigate(R.id.action_loginFragment_to_mainFragment2)
                    }
                }
                is Response.Failure -> {
                    //should probably show a snackbar
                    Text("Username or Password incorrect", color = MaterialTheme.colors.error)
                    Log.d("Login Error",response.e.message.toString())
                }
            }

        }
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
fun PasswordInput(viewModel: LoginViewModel){
    val state = viewModel.state.value

    val icon = if(state.passwordIconChecked)
        painterResource(id = drawable.design_ic_visibility)
    else
        painterResource(id = drawable.design_ic_visibility_off)

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
fun SubmitButton(loginViewModel: LoginViewModel,onNavigate: (Int) -> Unit){
    Button(onClick = {loginViewModel.submitButton()},
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text = "Login",fontSize = 26.sp)
    }
}
@Composable
fun SignUpForgotPassword(onNavigate: (Int) -> Unit){
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(start = 0.dp,25.dp,0.dp,0.dp).fillMaxWidth()) {
        Text("Forgot Password?",
            fontSize = 16.sp,
            modifier = Modifier.clickable(enabled = true) {
                onNavigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        )
        Text("Register",
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(enabled = true) {
                    onNavigate(R.id.action_loginFragment_to_registerFragment2)
                })

    }
}
@Composable
fun LinearLoadingBar(){
    LinearProgressIndicator(modifier = Modifier
        .width(276.dp)
        .padding(start = 0.dp, 16.dp, 0.dp, 0.dp))

}