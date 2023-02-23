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
import androidx.compose.ui.res.stringResource
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
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.sharedViews.PasswordInput
import com.elliottsoftware.calftracker.presentation.sharedViews.RegisterInput
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel
import timber.log.Timber

@Composable
fun LoginViews(viewModel: LoginViewModel = viewModel(),onNavigate: (Int) -> Unit = {}){
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

            RegisterInput(
                textState = viewModel.state.value.email,
                updateTextState = {text -> viewModel.updateEmail(text)},
                textStateError = viewModel.state.value.emailError,
                keyboardType = KeyboardType.Email,
                placeHolderText= "Email",
                modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
            )
            PasswordInput(
                passwordIconPressed = viewModel.state.value.passwordIconChecked,
                password = viewModel.state.value.password,
                passwordErrorMessage = viewModel.state.value.passwordError,
                updatePassword = {password -> viewModel.updatePassword(password) },
                updatePasswordIconPressed = {pressed -> viewModel.passwordIconChecked(pressed)}

            )
            SubmitButton(
                submit = {viewModel.submitButton()}
            )
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
                    Text( "Username or Password incorrect", color = MaterialTheme.colors.error)
                    Timber.tag("LoginError").d(response.e.message.toString())
                }
            }

        }
    }

}





@Composable
fun SubmitButton(
    submit:()->Unit
){
    Button(onClick = {submit()},
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text="Login",fontSize = 26.sp)
    }
}
@Composable
fun SignUpForgotPassword(onNavigate: (Int) -> Unit){
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(start = 0.dp,25.dp,0.dp,0.dp).fillMaxWidth()) {
        Text("Forgot Password",
            fontSize = 16.sp,
            modifier = Modifier.clickable(enabled = true) {
                onNavigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        )
        Text( "Register",
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