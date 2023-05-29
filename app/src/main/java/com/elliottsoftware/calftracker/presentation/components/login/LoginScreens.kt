package com.elliottsoftware.calftracker.presentation.components.login

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class LoginBoxData(
    val title:String,
    val icon: ImageVector,
    val iconDescription:String,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginHeader(
    sheetState: ModalSideSheetState,
    loginText: String,
    listLoginBoxData: List<LoginBoxData>,
    changeSheetContent: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(loginText,
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
            sheetState = sheetState,
            listLoginBoxData = listLoginBoxData,
            changeSheetContent = {changeSheetContent()}
        )


    } // END OF COLUMN
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginBoxes(
    sheetState: ModalSideSheetState,
    listLoginBoxData: List<LoginBoxData>,
    changeSheetContent: () -> Unit
){
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .alpha(0.8f)
        .padding(top = 15.dp)){
        for(item in listLoginBoxData){
            Row(
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .clickable {

                        scope.launch {
                            changeSheetContent()
                            sheetState.show()
                        }
                    }
                    .padding(10.dp)
                    .fillMaxWidth()

                ,
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    item.icon,
                    contentDescription = item.iconDescription,
                    Modifier.padding(end=40.dp)
                )
                Text(
                    item.title,
                    fontWeight = FontWeight.Bold
                )

            }
            Spacer(modifier = Modifier.padding(5.dp))
        }

    }

}