package com.elliottsoftware.calftracker.presentation.sharedViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingUiState
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingViewModel
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionValues
import com.elliottsoftware.calftracker.util.findActivity

@Composable
fun SubscriptionCardInfo(subscriptionInfo: SubscriptionValues) {


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActiveSubscription(
    subscriptionInfo: SubscriptionValues,
    billingViewModel: BillingViewModel,
    onNavigate: () -> Unit
){

    Column(
        modifier = Modifier.padding(15.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("Oops!", style = MaterialTheme.typography.h4)
        Text(
            "Looks like you hit the limit on your free tier and will need to upgrade for unlimited calf storage.",

            color = Color.Black.copy(alpha = 0.6f),

            )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick={
            onNavigate()
        }){
            Text("View subscription pricing",fontSize=20.sp)
        }

//        ClickText(
//            onNavigate = {location -> onNavigate(location)}
//        )

//        SubscriptionCardInfo(
//            subscriptionInfo
//        )
//        BuyingText(
//            value = billingViewModel.state.value,
//            billingViewModel = billingViewModel
//        )


    }
}
@Composable
fun ClickText(onNavigate: (Int) -> Unit){
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val packageName =  context.applicationContext.packageName
    val subscriptionId = "calf_tracker_premium_10"
    val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL = "https://play.google.com/store/account/subscriptions?product=%s&package=%s"
    val url = String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL,
        subscriptionId, packageName);

    ClickableText(
        onClick ={onNavigate(R.id.action_mainFragment2_to_subscriptionFragment)},
        text = AnnotatedString("Subscription settings"),
        style = TextStyle(
            fontSize =  20.sp,
            color= Color(R.color.linkColor)
        ),
        modifier = Modifier.padding(bottom = 20.dp)
    )

}


@Composable
fun BuyingText(value: BillingUiState, billingViewModel: BillingViewModel) {
    val context = LocalContext.current

    val activity = context.findActivity()

    Column() {


//        when(val response = value.productDetails){
//            is Response.Loading -> {
//                Button(onClick = {}){
//                    Text("Loading")
//                }
//            }
//            is Response.Success -> {
//                Button(onClick = {
//                    billingViewModel.buy(
//                        productDetails = response.data,
//                        currentPurchases = null,
//                        activity = activity,
//                        tag = "calf_tracker_premium"
//                    )
//                }
//                ){
//                    Text("Purchase")
//                }
//            }
//            is Response.Failure ->{
//                Button(onClick = {}){
//                    Text("Fail")
//                }
//            }
//            else -> {}
//        }



    }
}