package com.elliottsoftware.calftracker.presentation.components.forgotPassword

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
import com.elliottsoftware.calftracker.presentation.viewModels.ForgotPasswordViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.login.LinearLoadingBar
import com.elliottsoftware.calftracker.presentation.components.register.RegisterView
import com.elliottsoftware.calftracker.presentation.theme.AppTheme


@Composable
fun ForgotPasswordViews(viewModel: ForgotPasswordViewModel = viewModel()){
    AppTheme(false){
        ForgotPasswordView( viewModel = viewModel)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPasswordView(viewModel: ForgotPasswordViewModel = viewModel()){

    Scaffold(){
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()){
            BannerCard("Calf Tracker", "Powered by Elliott Software")
            EmailInput(viewModel)
            SubmitButton({viewModel.validateEmailText()})
            when(val response = viewModel.state.value.resetPassword){
                is Response.Loading -> LinearLoadingBar()
                is Response.Success ->{
                    if(response.data){
                        Text("Email sent",fontSize = 20.sp,modifier = Modifier.padding(start = 0.dp,16.dp,0.dp,0.dp))
                    }
                }
                is Response.Failure -> {
                    Text("Email not found",fontSize = 20.sp,modifier = Modifier.padding(start = 0.dp,16.dp,0.dp,0.dp))
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
fun EmailInput(viewModel: ForgotPasswordViewModel = viewModel()){

    val email = viewModel.state.value.email
    val emailError = viewModel.state.value.emailError

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            //state
            value = email,
            onValueChange = {viewModel.updateEmail(it)},
            //style
            singleLine = true,
            placeholder = {
                Text(text = "Recovery Email",fontSize = 26.sp)
            },
            modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
            ,
            textStyle = TextStyle(fontSize = 26.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            //error
            isError = emailError != null,
            trailingIcon = {
                if(emailError != null){
                    Icon(Icons.Filled.Error, "error has occurred")
                }

            }
        )
        if(emailError != null){
            Text(text = emailError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(
                Alignment.End))
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
















