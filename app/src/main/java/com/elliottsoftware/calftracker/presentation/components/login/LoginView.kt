package com.elliottsoftware.calftracker.presentation.components.login

import android.content.pm.ActivityInfo
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import com.elliottsoftware.calftracker.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.sharedViews.PasswordInput
import com.elliottsoftware.calftracker.presentation.sharedViews.RegisterInput
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel
import com.elliottsoftware.calftracker.util.findActivity
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginViews(viewModel: LoginViewModel = viewModel(),onNavigate: (Int) -> Unit = {}){
    val context = LocalContext.current
    val activity = context.findActivity()
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    var visible by remember { mutableStateOf(false) }




    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        skipHalfExpanded = true
    )
    val sheetState = rememberModalSideSheetState(initialValue = ModalSideSheetValue.Hidden)


    AppTheme(false){



        ModalSideSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            isVisible = visible,
            sheetState = sheetState,
            sheetContent = {
                ModalSideSheetLayoutSheetContent(
                    sheetState = sheetState
                )
            }
        ){
            ModalBottomSheetLayout(
                sheetElevation = 0.dp,
                sheetState = bottomModalState,
                sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                sheetContent = {
                    ModalContentView(
                        bottomModalState = bottomModalState,
                        onClick={ visible = !visible},
                        sheetState= sheetState
                    )
                }
            ) {
                LoginView(
                    viewModel = viewModel,
                    onNavigate = onNavigate,
                    bottomModalState = bottomModalState
                )

            }

        }



    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalSideSheetLayoutSheetContent(
    sheetState: ModalSideSheetState
){
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .height(300.dp)
            .padding(10.dp)
            .fillMaxWidth()

    ){
        Column(
            modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            ){
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Close the modal",
                    modifier = Modifier.clickable {
                        scope.launch {
                            sheetState.hide()
                        }

                    }
                        .size(30.dp)
                )
            }
            Text("Log in to Calf Tracker",
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


        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalContentView(
    bottomModalState: ModalBottomSheetState,
    onClick:()-> Unit,
    sheetState: ModalSideSheetState,

) {
    val scope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val almostFullHeight = (screenHeight * 0.8).dp
    Box(
        modifier = Modifier
            .height(almostFullHeight)
            .padding(10.dp)
            .fillMaxWidth()

    ){
        Column(
            modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            ){
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close the modal",
                    modifier = Modifier.clickable {
                        scope.launch{
                            bottomModalState.hide()
                        }
                    }
                )
            }
            Text("Log in to Calf Tracker",
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
            LoginBoxes(
                onClick ={ onClick()},
                sheetState = sheetState
            )


        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginBoxes(
    onClick:()-> Unit,
    sheetState: ModalSideSheetState,
){
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier
        .alpha(0.8f)
        .padding(top = 15.dp)){
        Row(
            modifier = Modifier
                .border(1.dp, Color.Gray)
                .clickable {

                    scope.launch {
                        sheetState.show()
                    }
                }
                .padding(10.dp)
                .fillMaxWidth()

            ,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                Icons.Filled.Person,
                contentDescription = "Close the modal",
                Modifier.padding(end=40.dp)
            )
            Text(
                " Use email / username",
                fontWeight = FontWeight.Bold
            )

        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginView(
    viewModel: LoginViewModel = viewModel(),
    onNavigate: (Int) -> Unit,
    bottomModalState: ModalBottomSheetState,

    ) {
    val scope = rememberCoroutineScope()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
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
                submit = {
                    //viewModel.submitButton()
                    scope.launch {
                        bottomModalState.show()
                    }
                }
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
        modifier = Modifier
            .padding(start = 0.dp, 25.dp, 0.dp, 0.dp)
            .fillMaxWidth()) {
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




