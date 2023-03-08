package com.elliottsoftware.calftracker.presentation.components.subscription

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.presentation.components.register.RegisterView
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.RegisterViewModel

@Composable
fun SubscriptionView(){
    AppTheme(false){
        SubscriptionViews()
    }
}

@Composable
fun SubscriptionViews(subscriptionViewModel: SubscriptionViewModel = viewModel()){
    Column(horizontalAlignment = Alignment.CenterHorizontally,modifier= Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        BannerCard("Calf Tracker", "Powered by Elliott Software")
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){

            FreeCard(
                subscriptionViewModel.state.value.isPremium,
                setIsClicked = {value ->subscriptionViewModel.setIsPremium(value)},
                changeTextData= { subscriptionViewModel.setTextDataFree() }
            )
            PremiumCard(
                subscriptionViewModel.state.value.isPremium,
                setIsClicked = {value ->subscriptionViewModel.setIsPremium(value)},
                changeTextData= { subscriptionViewModel.setTextDataPremium() }
            )
        }
        DetailTextBox(textData = subscriptionViewModel.state.value.textData)
        SubmitButton(submit={})
    }

}

@Composable
fun FreeCard(clicked:Boolean,setIsClicked:(Boolean)-> Unit,changeTextData:()->Unit){
    val borderColor = if(!clicked) MaterialTheme.colors.primary else Color.White

        Card(
            modifier = Modifier
                .height(180.dp)
                .width(170.dp)
                .clickable {
                    changeTextData()
                    setIsClicked(false)
                           },
            elevation = 5.dp,
            border = BorderStroke(4.dp, borderColor)
        ) {
            Column( modifier = Modifier
                .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Free", style = MaterialTheme.typography.h4, modifier = Modifier.padding(bottom = 20.dp))
                Text("$0.00/month",style = MaterialTheme.typography.subtitle1)
            }

        }



}
@Composable
fun PremiumCard(clicked:Boolean,setIsClicked:(Boolean)-> Unit,changeTextData:()->Unit){
    val borderColor = if(clicked) MaterialTheme.colors.primary else Color.White

        Card(
            modifier = Modifier
                .height(180.dp)
                .width(170.dp)
                .clickable {
                    changeTextData()
                    setIsClicked(true)
                           },
            elevation = 5.dp,
            border = BorderStroke(4.dp, borderColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Premium" ,style = MaterialTheme.typography.h4, modifier = Modifier.padding(bottom = 20.dp))
                Text("$10.00/month",style = MaterialTheme.typography.subtitle1)
            }

        }
}

@Composable
fun DetailTextBox(textData:List<String>){
    Column(modifier = Modifier.padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("What do I get ?",style = MaterialTheme.typography.h4)
        Box( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), contentAlignment = Alignment.Center){
            Column() {
                for (text in textData){
                    Text(text,Modifier.padding(top = 5.dp))
                }
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

        Text(text="Register",fontSize = 26.sp)
    }
}
