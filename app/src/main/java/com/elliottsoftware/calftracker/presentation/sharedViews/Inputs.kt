package com.elliottsoftware.calftracker.presentation.sharedViews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.presentation.viewModels.RegisterViewModel


/**
 * Text input meant for user input and optional error message
 * @param state The state of the text being typed.
 * @param placeHolderText The placeholder displayed to the user if there is no input.
 * @param updateValue Callback used to update the state parameter
 * @param modifier  a optional [Modifier][androidx.compose.ui.Modifier] for this text field
 * @param errorMessage a optional errorMessage to be shown to the user
 */
@Composable
fun SimpleTextInput(
    state: String,
    placeHolderText: String,
    updateValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage:String? = null

){
    val icon = painterResource(id = R.drawable.ic_error_24)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = state,
            isError = errorMessage != null,
            trailingIcon = {
                if (errorMessage != null)
                    Icon(painter = icon, contentDescription = "Error")
            },
            onValueChange = { updateValue(it) },
            singleLine = true,
            placeholder = {
                Text(text = placeHolderText, fontSize = 20.sp)
            },

            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),


            )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

/**
 * A number only text input
 * @param placeHolderText The placeholder displayed to the user if there is no input.
 * @param state The state of the text being typed.
 * @param updateValue Callback used to update the state parameter
 * @param modifier  a optional [Modifier][androidx.compose.ui.Modifier] for this text field
 */
@Composable
fun NumberInput(
    placeHolderText:String,
    state:String,
    updateValue: (String) -> Unit,
    modifier: Modifier = Modifier
){


    OutlinedTextField(value = state,

        onValueChange = { updateValue(it)},
        singleLine = true,
        placeholder = {
            Text(text = placeHolderText,fontSize = 20.sp)
        },

        modifier = modifier
            .padding(start = 10.dp, 10.dp, 10.dp, 0.dp)
            .fillMaxWidth()

        ,
        textStyle = TextStyle(fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),


        )

}


/**
 * Two radio buttons in a [Row][androidx.compose.foundation.layout.Row] with values of Bull and Heifer
 * @param state The state of the text being typed.
 * @param updateSex Callback used to update the state parameter
 * @param modifier  a optional [Modifier][androidx.compose.ui.Modifier] for this text field
 */
@Composable
fun BullHeiferRadioInput(
    state:String,
    updateSex: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        RadioButton(selected = state=="Bull", onClick = {updateSex("Bull") })
        Text(
            text = "Bull",
            modifier = Modifier
                .clickable(onClick = { })
                .padding(start = 4.dp)
        )

        RadioButton(selected = state=="Heifer", onClick = { updateSex("Heifer")})
        Text(
            text = "Heifer",
            modifier = Modifier
                .clickable(onClick = { })
                .padding(start = 4.dp)
        )
    }

}

/**
 * Two radio buttons in a [Row][androidx.compose.foundation.layout.Row] with values of Bull and Heifer
 * @param passwordIconPressed Determines which Icon to show to the user
 * @param password The value of the password
 * @param passwordErrorMessage  The message to be shown to the user if the password fails the predetermined values
 * @param updatePassword callback method used to update the password value entered by the user
 * @param updatePasswordIconPressed callback method used to update if the user has clicked the icon
 */
@Composable
fun PasswordInput(
                  passwordIconPressed:Boolean,
                  password:String,
                  passwordErrorMessage:String?,
                  updatePassword:(String)-> Unit,
                  updatePasswordIconPressed:(Boolean)->Unit
){
    val icon = if(passwordIconPressed)
        painterResource(id = R.drawable.design_ic_visibility)
    else
        painterResource(id = R.drawable.design_ic_visibility_off)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(value = password,
            onValueChange = {updatePassword(it)},
            placeholder = { Text(text ="Password", fontSize = 26.sp) },
            modifier = Modifier.padding(start = 0.dp, 10.dp, 0.dp, 0.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = passwordErrorMessage != null,
            trailingIcon = {
                IconButton(onClick = {
                    updatePasswordIconPressed(!passwordIconPressed)
                }) {
                    Icon(painter = icon, contentDescription = "Visibility Icon")
                }
            },
            visualTransformation = if (passwordIconPressed) VisualTransformation.None
            else PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 26.sp)
        )
        if (passwordErrorMessage != null) {
            Text(
                text = passwordErrorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

}

/**
 * Basic text input needed for gathering text input during a register. Also shows an error message
 * @param textState The text input by the user
 * @param updateTextState callback method called to update the textState
 * @param textStateError  Error message shown to the user if error occurs with textState
 * @param keyboardType The type of [Keyboard][androidx.compose.ui.text.input.KeyboardType] shown to the user
 * @param placeHolderText Text shown to the user when the function is first composed
 * @param modifier a optional [Modifier][androidx.compose.ui.Modifier] that determines the space between the inputs
 */
@Composable
fun RegisterInput(
    textState:String,
    updateTextState:(String)->Unit,
    textStateError:String?,
    keyboardType: KeyboardType,
    placeHolderText: String,
    modifier: Modifier = Modifier

){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        OutlinedTextField(value = textState,
            onValueChange = { updateTextState(it)},
            singleLine = true,
            placeholder = {
                Text(text = placeHolderText,fontSize = 26.sp)
            }
            ,
            textStyle = TextStyle(fontSize = 26.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            )


        )
        if(textStateError != null){
            Text(text = textStateError,color = MaterialTheme.colors.error, modifier = Modifier.align(
                Alignment.End))
        }

    }
}