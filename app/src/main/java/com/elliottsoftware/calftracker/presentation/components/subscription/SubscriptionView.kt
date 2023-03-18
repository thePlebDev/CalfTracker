package com.elliottsoftware.calftracker.presentation.components.subscription

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.util.findActivity

@Composable
fun SubscriptionView( onNavigate: (Int) -> Unit = {}){
    AppTheme(false){
        SubscriptionViews(onNavigate = {location -> onNavigate(location)})
    }
}

@Composable
fun SubscriptionViews(

    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    onNavigate: (Int) -> Unit = {},
    billingViewModel:BillingViewModel = viewModel()
){
    // State variable passed into Billing connection call and set to true when
    // connections is established.
    val isBillingConnected by billingViewModel.billingConnectionState.observeAsState()

//    val productsForSale by billingViewModel.productsForSaleFlows.collectAsState(
//        initial = MainState()
//    )
    val currentPurchases by billingViewModel.currentPurchasesFlow.collectAsState(
        initial = listOf()
    )
    val screen by billingViewModel.destinationScreen.observeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally,modifier= Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        BannerCard("Calf Tracker", "Powered by Elliott Software")
//        DetailTextBox(textData = subscriptionViewModel.state.value.textData)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceEvenly){

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

        DetailTextBox(isPremium = subscriptionViewModel.state.value.isPremium)
        SubmitButton(submit={ location -> onNavigate(location)})
        when(screen){
            BillingViewModel.DestinationScreen.SUBSCRIPTIONS_OPTIONS_SCREEN ->{

                BuyingText(billingViewModel.state.value,billingViewModel)

            }

            else -> {
                Text(screen.toString())
            }
        }


    }


}
@Composable
fun BuyingText(value: BillingUiState,billingViewModel: BillingViewModel) {
    val context = LocalContext.current
    val activity = context.findActivity()


    Column() {

        when(val response = value.subscriptionProduct){
            is Response.Loading -> {
                Button(onClick = {}){
                    Text("Loading")
                }
            }
            is Response.Success -> {
                Button(onClick = {
                    billingViewModel.buy(
                        productDetails = response.data,
                        currentPurchases = null,
                        activity = activity,
                        tag = "calf_tracker_premium"
                    )
                }
                ){
                    Text(response.data.name)
                }
            }
            is Response.Failure ->{
                Button(onClick = {}){
                    Text("Fail")
                }
            }

        }


    }
}


@Composable
fun FreeCard(clicked:Boolean,setIsClicked:(Boolean)-> Unit,changeTextData:()->Unit){
    val borderColor = if(!clicked) MaterialTheme.colors.primary else Color.White

        Card(
            modifier = Modifier
                .width(160.dp)
                .clickable {
                    changeTextData()
                    setIsClicked(false)
                },
            elevation = 5.dp,
            border = BorderStroke(4.dp, borderColor)
        ) {
            Column( modifier = Modifier
                .padding(15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("$00.00/month",modifier = Modifier.padding(bottom = 10.dp),style = MaterialTheme.typography.h6)
                Text("- 50 calf limit")
                Text("- Offline usage")
                Text("- Cloud backup")

            }

        }

}
@Composable
fun PremiumCard(clicked:Boolean,setIsClicked:(Boolean)-> Unit,changeTextData:()->Unit){
    val borderColor = if(clicked) MaterialTheme.colors.primary else Color.White

        Card(
            modifier = Modifier

                .width(160.dp)
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
                horizontalAlignment = Alignment.Start
            ) {
                Text("$10.00/month",modifier = Modifier.padding(bottom = 10.dp),style = MaterialTheme.typography.h6)
                Text("- No calf limit")
                Text("- Offline usage")
                Text("- Cloud backup")
            }

        }
}

@Composable
fun DetailTextBox(isPremium:Boolean){
    val billedText = if(isPremium) "Your Google account will be charged immediately after sign up" else "Your Google account will not be charged. You have selected the free tier"
    val subText = if(isPremium) "Yes. You can manage your subscription through" else "No. You have selected the free tier"
    Column(modifier = Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text("When will I be billed?",style = MaterialTheme.typography.h6)
        Column( horizontalAlignment = Alignment.Start){
            Text(billedText,color = Color.Gray.copy(alpha = .9f))
        }
        Text("Does my subscription auto renew?",style = MaterialTheme.typography.h6,modifier = Modifier.padding(top=15.dp))
        Column( horizontalAlignment = Alignment.CenterHorizontally){

            Text(subText,color = Color.Gray.copy(alpha = .9f))
            if(isPremium){
                HighlightedText()
            }

        }


    }

}


@Composable
fun SubmitButton(
    submit: (Int) -> Unit = {}
){
    Button(onClick = {},
        modifier = Modifier
            .height(80.dp)
            .width(280.dp)
            .padding(start = 0.dp, 20.dp, 0.dp, 0.dp)) {

        Text(text="Register",fontSize = 26.sp)
    }
}

@Composable
fun HighlightedText(
    modifier:Modifier = Modifier,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
){
    val uriHandler = LocalUriHandler.current
    Text(
        "Google Play’s Subscription Center",
        modifier = modifier.clickable{
            uriHandler.openUri("https://play.google.com/store/account/subscriptions")

        },
        color = linkTextColor,
        textDecoration = linkTextDecoration,
        fontWeight = linkTextFontWeight


    )

}

