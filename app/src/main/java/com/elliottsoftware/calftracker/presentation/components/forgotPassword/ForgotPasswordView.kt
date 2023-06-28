package com.elliottsoftware.calftracker.presentation.components.forgotPassword

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.presentation.viewModels.ForgotPasswordViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.login.LinearLoadingBar
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.sharedViews.RegisterInput
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun ForgotPasswordViews(
    viewModel: ForgotPasswordViewModel = viewModel(),
    onNavigate: (Int) -> Unit
){
    AppTheme(false){
        ForgotPasswordView(
            viewModel = viewModel,
            onNavigate = onNavigate
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPasswordView(
    viewModel: ForgotPasswordViewModel = viewModel(),
    onNavigate: (Int) -> Unit
){

    Scaffold(){
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()){
           // BannerCard("Calf Tracker", "Powered by Elliott Software")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start=10.dp,top=10.dp,bottom = 30.dp)
            ){
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Move back to login screen",
                    modifier = Modifier
                        .clickable {
                            onNavigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                        }
                        .size(30.dp)
                )
            }
            Text("Forgot password",
                style = MaterialTheme.typography.h5,
                modifier =Modifier.padding(bottom =5.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier =Modifier.alpha(0.8f),
                text = "We'll email you a code to reset your password",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1
            )
            //THIS IS AN EMAIL INPUT
            RegisterInput(
                textState = viewModel.state.value.email,
                updateTextState = {text -> viewModel.updateEmail(text)},
                textStateError = viewModel.state.value.emailError,
                keyboardType = KeyboardType.Email,
                placeHolderText= "Recovery Email",
                modifier = Modifier.padding(start = 0.dp,10.dp,0.dp,0.dp)
            )
            SubmitButton { viewModel.validateEmailText() }
            when(val response = viewModel.state.value.resetPassword){
                is Response.Loading -> LinearLoadingBar()
                is Response.Success ->{
                    if(response.data){
                        Timber.tag("SENDINGEMAIL").d("email sent")
                        Text("Email sent",fontSize = 20.sp,modifier = Modifier.padding(start = 0.dp,16.dp,0.dp,0.dp))
                    }
                }
                is Response.Failure -> {
                    Text(
                        "Email not found",
                        fontSize = 20.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 0.dp,16.dp,0.dp,0.dp)
                    )
                }

            }

        }
    }


}


@Composable
fun SubmitButton(buttonFunc:()->Unit){
    Button(onClick = {buttonFunc()},
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text = "Reset Password",fontSize = 26.sp)
    }
}
















