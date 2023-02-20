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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.R


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