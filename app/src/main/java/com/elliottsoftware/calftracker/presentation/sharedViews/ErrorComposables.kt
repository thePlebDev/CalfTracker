package com.elliottsoftware.calftracker.presentation.sharedViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**************ERRORS*****************/
@Composable
fun ErrorResponse(
    refreshMethod:()->Unit,
    errorMessageTitle: String,
    errorMessageBody:String,
    errorButtonMessage:String,
    modifier: Modifier = Modifier
) {
    Card(backgroundColor = MaterialTheme.colors.secondary,modifier = modifier.padding(8.dp)) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(errorMessageTitle, style = MaterialTheme.typography.h4)
            Text(errorMessageBody,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center
            )
            ErrorButton(refreshMethod,errorButtonMessage)
        }
    }

}

@Composable
fun ErrorButton(refreshMethod:()->Unit,text:String) {
    Button(onClick = {
        refreshMethod()
    }) {
        Text(text = text,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center)
    }
}