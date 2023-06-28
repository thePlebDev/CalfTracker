package com.elliottsoftware.calftracker.presentation.components.login

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.register.Register
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.sharedViews.PasswordInput
import com.elliottsoftware.calftracker.presentation.sharedViews.RegisterInput
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.LoginViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.RegisterViewModel
import com.elliottsoftware.calftracker.util.Actions
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
    var sheetContentUI by remember { mutableStateOf(true) }




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
                if(sheetContentUI){
                    ModalSideSheetLayoutSheetContent(
                        sheetState = sheetState,
                        emailText= viewModel.state.value.email,
                        updateEmail = {text -> viewModel.updateEmail(text)},
                        emailError = viewModel.state.value.emailError,
                        passwordIconPressed = viewModel.state.value.passwordIconChecked,
                        passwordText = viewModel.state.value.password,
                        passwordErrorMessage= viewModel.state.value.passwordError,
                        updatePassword = {password -> viewModel.updatePassword(password) },
                        updatePasswordIconPressed = {pressed -> viewModel.passwordIconChecked(pressed)},
                        submit={viewModel.submitButton()},
                        enabled = viewModel.state.value.buttonEnabled,
                        loginStatus = viewModel.state.value.loginStatus,
                        onNavigate = onNavigate,

                    )
                }else{
                    Register(
                        sheetState = sheetState,
                        onNavigate = onNavigate
                    )

                }

            }
        ){
            ModalBottomSheetLayout(
                sheetElevation = 0.dp,
                sheetState = bottomModalState,
                sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                sheetContent = {
                    ModalContentView(
                        bottomModalState = bottomModalState,
                        sheetState= sheetState,
                        changeSheetContent = { booleanItem -> sheetContentUI = booleanItem }

                    )
                }
            ) {
                LoginView(
                    onNavigate = onNavigate,
                    bottomModalState = bottomModalState
                )

            }

        }



    }
}



@Composable
fun ErrorMessage(
    modifier: Modifier,
    visible: Boolean
){
    val density = LocalDensity.current
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Text("Error with username or password",
            color = Color.Red,
            modifier = modifier
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color(0xFFadadad))
                .padding( 5.dp)

        )
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalSideSheetLayoutSheetContent(
    sheetState: ModalSideSheetState,
    emailText:String,
    updateEmail:(String)->Unit,
    emailError:String?,
    passwordIconPressed:Boolean,
    passwordText:String,
    passwordErrorMessage:String?,
    updatePassword:(String)->Unit,
    updatePasswordIconPressed:(Boolean)->Unit,
    submit:()->Unit,
    enabled:Boolean,
    loginStatus:Response<Boolean>,
    onNavigate: (Int) -> Unit,
){
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()

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

                RegisterInput(
                    textState = emailText,
                    updateTextState = updateEmail,
                    textStateError = emailError,
                    keyboardType = KeyboardType.Email,
                    placeHolderText= "Email",
                    modifier = Modifier.padding(start = 0.dp,40.dp,0.dp,0.dp)
                )

            PasswordInput(
                passwordIconPressed = passwordIconPressed,
                password = passwordText,
                passwordErrorMessage = passwordErrorMessage,
                updatePassword = updatePassword,
                updatePasswordIconPressed = updatePasswordIconPressed
            )
            when(val response = loginStatus){
                is Response.Loading -> {}
                is Response.Success -> {
                    if(response.data){
                        //THIS IS WHERE WE WOULD DO THE NAVIGATION

                        onNavigate(R.id.action_loginFragment_to_mainFragment2)
                    }
                }
                is Response.Failure -> {
                    //should probably show a snackbar
                    Text( "Username or Password incorrect", color = MaterialTheme.colors.error)
                }

            }
            SubmitButton(
                submit = {
                    submit()
//
                },
                enabled = enabled,
                title = "Login"
            )
            SignUpForgotPassword(onNavigate)
            }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalContentView(
    bottomModalState: ModalBottomSheetState,
    sheetState: ModalSideSheetState,
    bottomTextBackgroundColor: Color = Color(0xFFededed),
    changeSheetContent: (Boolean) -> Unit
) {
    val animationTime = 100

    var visible  by remember { mutableStateOf(true) }

    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val almostFullHeight = (screenHeight * 0.8).dp
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
        .height(almostFullHeight)
        .fillMaxWidth()
    ){
        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .background(bottomTextBackgroundColor)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(40.dp))
        }

        Column(){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
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

            val highLightedTexst = buildAnnotatedString {
                append("Don't have an account?")
                withStyle(style= SpanStyle(color= Color(0xFFe63946))){
                    append(" Sign up")
                }
            }
            val listLoginBoxDatas = listOf(
                LoginBoxData(
                    "Use email / username",
                    icon = Icons.Filled.Person,
                    iconDescription = "Close the modal",
                ),
            )

            LoginAnimation(
                visible =visible,
                animationTime = animationTime,
                sheetState = sheetState,
                changeSheetContent = {  changeSheetContent(true) },
                changeVisibility = {visible = false},
                titleText = "Login to Calf Tracker",
                annotatedText = highLightedTexst,
                listLoginBoxData = listLoginBoxDatas,
                animationDirection = 1

            )
            val highlightedText = buildAnnotatedString {
                append("Already have an account?")
                withStyle(style= SpanStyle(color= Color(0xFFe63946))){
                    append(" Login")
                }
            }
            val listLoginBoxData = listOf(
                LoginBoxData(
                    "Use username and email",
                    icon = Icons.Filled.Person,
                    iconDescription = "Close the modal",
                ),
            )
            LoginAnimation(
                visible =!visible,
                animationTime = animationTime,
                sheetState = sheetState,
                changeSheetContent = {  changeSheetContent(false) },
                changeVisibility = {visible = true},
                titleText = "Sign up for Calf Tracker",
                annotatedText = highlightedText,
                listLoginBoxData = listLoginBoxData,
                animationDirection = -1

            )

        } // end of the column

    } // end of the box scope

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginAnimation(
    visible:Boolean,
    animationTime:Int,
    sheetState:ModalSideSheetState,
    changeSheetContent: (Boolean) -> Unit,
    changeVisibility: () -> Unit,
    titleText:String,
    annotatedText:AnnotatedString,
    listLoginBoxData:List<LoginBoxData>,
    animationDirection:Int,

){
    AnimatedVisibility(
        visible,
        modifier = Modifier.fillMaxSize(),
        enter = slideInHorizontally(
            initialOffsetX = { it * animationDirection}, // it == screen fullWidth
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it * animationDirection},
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing
            )
        )
    ) {
        BottomModalScreen(
            sheetState = sheetState,
            changeVisiblility = { changeVisibility() },
            changeSheetContent = { changeSheetContent(false) },
            titleText = titleText,
            annotatedText = annotatedText,
            boxDataList = listLoginBoxData
        )
    }
}




@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginView(
    onNavigate: (Int) -> Unit,
    bottomModalState: ModalBottomSheetState,

    ) {
     val scope = rememberCoroutineScope()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            BannerCard("Calf Tracker", "Powered by Elliott Software")



            Spacer(modifier = Modifier.padding(30.dp))
            Icon(Icons.Filled.Person,
                "contentDescription",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            SubmitButton(
                submit = {
                    scope.launch {
                        bottomModalState.show()
                    }
                },
                title = "Login"
            )
            SignUpForgotPassword(onNavigate)

        }


}





@Composable
fun SubmitButton(
    submit: () -> Unit,
    enabled: Boolean = true,
    title: String
){
    Button(
        onClick = {
            if(enabled){
                submit()
            }

                  },
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {
        Box(modifier = Modifier.fillMaxSize()){
            Text(text=title,fontSize = 26.sp,modifier = Modifier.align(Alignment.Center))
            if(!enabled){
                CircularProgressIndicator(
                    color=Color.Black,
                    modifier= Modifier
                        .size(26.dp)
                        .align(Alignment.CenterEnd)
                )
            }

        }



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

    }
}
@Composable
fun LinearLoadingBar(){
    LinearProgressIndicator(modifier = Modifier
        .width(276.dp)
        .padding(start = 0.dp, 16.dp, 0.dp, 0.dp))

}




