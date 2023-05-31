package com.elliottsoftware.calftracker.presentation.components.register

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.presentation.components.login.LinearLoadingBar
import com.elliottsoftware.calftracker.presentation.components.login.LoginView
import com.elliottsoftware.calftracker.presentation.sharedViews.*
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.RegisterViewModel
import com.elliottsoftware.calftracker.util.Actions
import timber.log.Timber

@Composable
fun RegisterViews(viewModel: RegisterViewModel = viewModel(),onNavigate:(Int) -> Unit){
    AppTheme(false){
        RegisterView( viewModel = viewModel,onNavigate= onNavigate)
    }
}

@Composable
fun RegisterView(viewModel: RegisterViewModel = viewModel(),onNavigate:(Int) -> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BannerCard("Calf Tracker", "Powered by Elliott Software")

        //THIS IS THE USERNAME INPUT
        RegisterInput(
            textState = viewModel.state.value.username,
            updateTextState = {text -> viewModel.updateUsername(text)},
            textStateError = viewModel.state.value.usernameError,
            keyboardType = KeyboardType.Text,
            placeHolderText= "Username",
            modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
        )

        //THIS IS THE EMAIL INPUT
        RegisterInput(
            textState = viewModel.state.value.email,
            updateTextState = {text -> viewModel.updateEmail(text)},
            textStateError = viewModel.state.value.emailError,
            keyboardType = KeyboardType.Email,
            placeHolderText= "Email",
            modifier = Modifier.padding(start = 0.dp,10.dp,0.dp,0.dp)
        )
        //THIS IS THE PASSWORD INPUT
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

        when(val response = viewModel.state.value.signInWithFirebaseResponse){
            is Response.Loading -> LinearLoadingBar()

            is Response.Success -> {
                //todo: I think we can change the response data to a more explicit enum
//                if(response.data == Actions.FIRST){
//                    //THIS IS WHERE WE WOULD DO THE NAVIGATION
//                    LinearLoadingBar()
//                    viewModel.createUserDatabase(viewModel.state.value.email,viewModel.state.value.username)
//                }
//                if(response.data == Actions.SECOND){
//                    onNavigate(R.id.action_registerFragment2_to_mainFragment2)
//                }
//                if(response.data == Actions.RESTING){
//
//                }
            }
            is Response.Failure -> {
                //should probably show a snackbar
                Fail()
                Timber.tag("Login Error").d(response.e.message.toString())
            }
            // else -> {}
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

        Text(text ="Register",fontSize = 26.sp)
    }

}
@Composable
fun Fail() {
    Text("FAILURE")
}
@Composable
fun Success() {
    Text("SUCCESS")
}