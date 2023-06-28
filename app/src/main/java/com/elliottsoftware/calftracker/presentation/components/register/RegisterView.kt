package com.elliottsoftware.calftracker.presentation.components.register

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.login.ErrorMessage
import com.elliottsoftware.calftracker.presentation.components.login.ModalSideSheetState
import com.elliottsoftware.calftracker.presentation.components.login.SubmitButton
import com.elliottsoftware.calftracker.presentation.sharedViews.PasswordInput
import com.elliottsoftware.calftracker.presentation.sharedViews.RegisterInput
import com.elliottsoftware.calftracker.presentation.viewModels.RegisterViewModel
import kotlinx.coroutines.launch
import timber.log.Timber



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Register(
    sheetState: ModalSideSheetState,
    registerViewModel: RegisterViewModel = viewModel(),
    onNavigate: (Int) -> Unit

){
    val scope = rememberCoroutineScope()

    when(val response = registerViewModel.state.value.signInWithFirebaseResponse){
        is Response.Loading -> {
            //NOTHING NEEDS TO BE DONE HERE

        }
        is Response.Success -> {
            if(response.data){
                Timber.tag("testingLogin").d("signInWithFirebaseResponse -> SUCCESS SECOND")
                onNavigate(R.id.action_loginFragment_to_mainFragment2)
            }
        }
        is Response.Failure -> {
            Timber.tag("testingLogin").d("signInWithFirebaseResponse -> FAILED")

        }
    }
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()

    ){


        ErrorMessage(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = registerViewModel.state.value.registerError
        )



        Column(
            modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Move back to login screen",
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }

                        }
                        .size(30.dp)
                )
            }
            Text("Signup for Calf Tracker",
                style = MaterialTheme.typography.h5,
                modifier =Modifier.padding(bottom =5.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier =Modifier.alpha(0.8f),
                text = "Manage your calves, herds and much more coming",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1
            )
            //THIS IS THE USERNAME INPUT
            RegisterInput(
                textState = registerViewModel.state.value.username,
                updateTextState = {text -> registerViewModel.updateUsername(text)},
                textStateError = registerViewModel.state.value.usernameError,
                keyboardType = KeyboardType.Text,
                placeHolderText= "Username",
                modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
            )

            //THIS IS THE EMAIL INPUT
            RegisterInput(
                textState = registerViewModel.state.value.email,
                updateTextState = {text -> registerViewModel.updateEmail(text)},
                textStateError = registerViewModel.state.value.emailError,
                keyboardType = KeyboardType.Email,
                placeHolderText= "Email",
                modifier = Modifier.padding(start = 0.dp,10.dp,0.dp,0.dp)
            )
            //THIS IS THE PASSWORD INPUT
            PasswordInput(
                passwordIconPressed = registerViewModel.state.value.passwordIconChecked,
                password = registerViewModel.state.value.password,
                passwordErrorMessage = registerViewModel.state.value.passwordError,
                updatePassword = {password -> registerViewModel.updatePassword(password) },
                updatePasswordIconPressed = {pressed -> registerViewModel.passwordIconChecked(pressed)}

            )

            SubmitButton(
                submit={registerViewModel.submitButton()},
                title = "Register",
                enabled= registerViewModel.state.value.buttonEnabled
            )
        }

    }

}