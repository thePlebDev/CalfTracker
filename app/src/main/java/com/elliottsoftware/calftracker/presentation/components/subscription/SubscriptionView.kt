package com.elliottsoftware.calftracker.presentation.components.subscription

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.util.findActivity
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@Composable
fun SubscriptionView(
    onNavigate: (Int) -> Unit = {},
    viewModel:BillingViewModel
){
    AppTheme(false){
        SubscriptionViews(
            onNavigate = {location -> onNavigate(location)},
            billingViewModel = viewModel
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SubscriptionViews(

    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    onNavigate: (Int) -> Unit = {},
    billingViewModel:BillingViewModel = viewModel()
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.primary,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        topBar = {
            TopAppBar(
                title = { Text("Calf Tracker") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()

                            }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Toggle navigation drawer")
                    }
                }
            )
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id= "logout",
                        title="Logout",
                        contentDescription = "Logout",
                        icon = Icons.Default.Logout,
                        onClick = {
                            scope.launch {
                               // viewModel.signUserOut()
                                scaffoldState.drawerState.close()


                            }
                        }
                    )
                )
            )
        }
    ){
        //WE NEED A BOTTOM TEXT THAT WE TOUCH AND IT CHANGES VALUES

        TabScreen(billingViewModel,billingViewModel.state.value)



    }



}

@Composable
fun TabScreen(viewModel: BillingViewModel,UIState:BillingUiState) {
    var tabIndex by remember { mutableStateOf(0) }


    val tabs = listOf("Home", "Premium", "Settings")
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    // If `lifecycleOwner` changes, dispose and reset the effect
//    DisposableEffect(lifecycleOwner) {
//        // Create an observer that triggers our remembered callbacks
//        // for sending analytics events
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//
//                Timber.tag("LIFEEVENTCYCLE").d("WE ARE RESUMING")
//                viewModel.refreshPurchases()
//            }
//        }
//
//        // Add the observer to the lifecycle
//        lifecycleOwner.lifecycle.addObserver(observer)
//
//        // When the effect leaves the Composition, remove the observer
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = tabIndex,modifier=Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title)},
                    modifier= Modifier.align(Alignment.CenterHorizontally),
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.CurrencyExchange, contentDescription = null)
                            2 -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)

                        }
                    }
                )
            }
        }
        when (tabIndex) {
            //BuyingText(UIState,viewModel)
            0 -> ActiveSubscription(viewModel.state.value.subscribedInfo)
            1 -> PremiumPage(billingUiState = viewModel.state.value, billingViewModel = viewModel)
            2 -> Text("Settings")

        }
    }
}



@Composable
fun Settings(billingUiState: BillingUiState,billingViewModel: BillingViewModel){
    val context = LocalContext.current
   val packageName =  context.applicationContext.packageName
     val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL = "https://play.google.com/store/account/subscriptions?product=%s&package=%s"
    val url = String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL,
        "calf_tracker_premium_10", packageName);

    Column(modifier = Modifier.padding(15.dp)) {
        Text("Premium ", style = MaterialTheme.typography.h5)
        Text("subscription:",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SubscriptionCardInfo(
            subscriptionInfo = SubscriptionValues(
                description = "Premium subscription",
                title= "Premium subscription",
                items= "Unlimited Calf",
                price = "$10.00",
                icon = Icons.Default.MonetizationOn
            )
        )

        BuyingText(billingUiState,billingViewModel)
    }

}

@Composable
fun PremiumPage(billingUiState: BillingUiState,billingViewModel: BillingViewModel){
    Column(modifier = Modifier.padding(15.dp)) {
        Text("Premium ", style = MaterialTheme.typography.h5)
        Text("subscription:",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SubscriptionCardInfo(
            subscriptionInfo = SubscriptionValues(
                description = "Premium subscription",
                title= "Premium subscription",
                items= "Unlimited Calf",
                price = "$10.00",
                icon = Icons.Default.MonetizationOn
            )
        )

        BuyingText(billingUiState,billingViewModel)
    }
}

@Composable
fun MyButton(url:String) {//THIS SHOULD BE IN THE SETTINGS
    val uriHandler = LocalUriHandler.current
    Button(onClick = { uriHandler.openUri(url) }){
        Text("Subscriptions")
    }
}

@Composable
fun ActiveSubscription(subscriptionInfo: SubscriptionValues){
    Column(modifier = Modifier.padding(15.dp)) {
        Text("My active ", style = MaterialTheme.typography.h5)
        Text(
            "subscription:",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SubscriptionCardInfo(subscriptionInfo)

    }
}

@Composable
fun SubscriptionCardInfo(subscriptionInfo: SubscriptionValues) {

        SubscriptionCard(
            SubscriptionValues(
                description = subscriptionInfo.description,
                title=subscriptionInfo.title,
                items=subscriptionInfo.items,
                price = subscriptionInfo.price,
                icon = subscriptionInfo.icon
            )
        )
}


@Composable
fun SubscriptionCard(subscriptionData:SubscriptionValues){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.secondary
    ){
        Row(
            horizontalArrangement= Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(subscriptionData.icon, contentDescription = subscriptionData.description,modifier = Modifier.size(60.dp))
            Column(modifier = Modifier.padding(15.dp)){
                Text(subscriptionData.title)
                Text(subscriptionData.items,color = Color.Black.copy(alpha = 0.6f))
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            fontSize =  32.sp
                        )
                        ) {
                            append(subscriptionData.price)
                        }
                        append("/month")
                    }
                )
            }
        }
    }
}







/******BELOW IS ALL THE BUYING THINGS*******/
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

        when(val response = value.purchasedSubscriptions){
            is Response.Loading -> {
                Button(onClick = {}){
                    Text("Loading purchasedSubscriptions")
                }
            }
            is Response.Success -> {
                Button(onClick = {}
                ){
                    if(response.data.isNotEmpty()){
                        Column(){
                            Text((response.data[0].purchaseState == Purchase.PurchaseState.PURCHASED).toString())
                            Text((response.data[0].purchaseState == Purchase.PurchaseState.PENDING).toString())
                            Text((response.data[0].purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE).toString())
                        }

                    }else{
                        Text("Purchase Empty")
                    }

                }
            }
            is Response.Failure ->{
                Button(onClick = {}){
                    Text("Fail purchasedSubscriptions")
                }
            }


        }



    }
}









