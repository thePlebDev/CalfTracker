package com.elliottsoftware.calftracker.presentation.components.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomModalScreen(
    modifier: Modifier = Modifier,
    sheetState: ModalSideSheetState,
    changeVisiblility: () -> Unit,
    changeSheetContent: () -> Unit,
    titleText:String,
    annotatedText:AnnotatedString,
    boxDataList:List<LoginBoxData>

    ){

    Box(modifier = modifier
        .fillMaxSize()
    ){
        Box(
            modifier = modifier
                .matchParentSize()
                .padding(10.dp)

        ){
            LoginHeader(
                sheetState = sheetState,
                loginText = titleText,
                listLoginBoxData = boxDataList,
                changeSheetContent = {  changeSheetContent()}
            )

        }// end of the innerbox

        Column(modifier = modifier
            .align(Alignment.BottomCenter)

            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.padding(10.dp))
            ClickableText(
                modifier = Modifier.alpha(1f),
                onClick ={ offset ->

                    if(offset >=20){
                        changeVisiblility()
                    }
                },
                text = annotatedText,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.padding(15.dp))
        }

    } //END OF OUTER BOX
}