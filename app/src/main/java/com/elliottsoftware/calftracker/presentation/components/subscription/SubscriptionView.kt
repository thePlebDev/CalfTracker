package com.elliottsoftware.calftracker.presentation.components.subscription

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.navigation.Navigation
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.util.findActivity
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SubscriptionView(
    onNavigate: (Int) -> Unit = {},
    viewModel:BillingViewModel
){
    AppTheme(false){
        SubscriptionViews(
            onNavigate = {location -> onNavigate(location)},
            billingViewModel = viewModel,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SubscriptionViews(
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    onNavigate: (Int) -> Unit = {},
    billingViewModel:BillingViewModel = viewModel()
){
    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            TopAppBar(
                title = { Text("Subscriptions") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                bottomModalState.show()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Toggle modal drawer")
                    }
                }
            )
        },
    ){

        ModalBottomSheetLayout(
            sheetState = bottomModalState,
            sheetContent = {
                ModalContents(
                    onNavigate = onNavigate,
                    bottomModalState = bottomModalState
                )
            }

        ){
            TabScreen(billingViewModel,billingViewModel.state.value)
        }
    }



}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalContents(
    onNavigate: (Int) -> Unit = {},
    bottomModalState:ModalBottomSheetState

){
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center


    ){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {

            items(Navigation.Modal.navList) { navItem ->
                Card(
                    modifier = Modifier
                        .padding(8.dp).clickable {
                            scope.launch {
                                bottomModalState.hide()
                                onNavigate(navItem.navigationDestination)
                            }
                        },
                    backgroundColor = MaterialTheme.colors.secondary,
                    elevation = 8.dp,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = 12.dp, horizontal = 4.dp),

                        ){
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.contentDescription,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(navItem.title)
                    }
                }
            }
        }
    }
}

@Composable
fun TabScreen(viewModel: BillingViewModel,UIState:BillingUiState) {
    var tabIndex by remember { mutableStateOf(0) }


    val tabs = listOf("Home", "Premium", "Details")


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
            0 -> MainSubscription(
                viewModel.state.value.subscribedInfo,
                changeIndex = {tabIndex = 1},
                subscribed = viewModel.state.value.subscribed,
                viewModel.state.value.nextBillingPeriod
            )

            1 -> PremiumPage(billingUiState = viewModel.state.value, billingViewModel = viewModel)

            2 -> Settings(billingUiState=viewModel.state.value, billingViewModel = viewModel)

        }
    }
}



@Composable
fun Settings(billingUiState: BillingUiState,billingViewModel: BillingViewModel){

    Column(modifier = Modifier.padding(15.dp)) {
        ActiveSubscription(subscriptionInfo = billingUiState.subscribedInfo)
        Text("When will I be charged ?", style = MaterialTheme.typography.h5)
        Text("Immediately upon purchase. This purchase is recurring and you will be charged once a month",
            color = Color.Black.copy(alpha = 0.6f)
        )
        Text("Subscription Benefits", style = MaterialTheme.typography.h5)
        Text("Unlimited calf data storage and digital backups of all your data",
            color = Color.Black.copy(alpha = 0.6f)
        )


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
                items= "Unlimited calf storage",
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
    Column() {
        Text("My active ", style = MaterialTheme.typography.h5)
        Text(
            "subscription:",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        SubscriptionCardInfo(subscriptionInfo)

    }
}
//todo: make the button conditional
@Composable
fun MainSubscription(
    subscriptionInfo: SubscriptionValues,
    changeIndex:() ->Unit,
    subscribed: Boolean,
    billingPeriod: String
){
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val packageName =  context.applicationContext.packageName
    val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL = "https://play.google.com/store/account/subscriptions?product=%s&package=%s"
    val url = String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL,
        "calf_tracker_premium_10", packageName);

    Column(modifier = Modifier.padding(15.dp)) {
        ActiveSubscription(subscriptionInfo)
        if(subscribed){
            Button(
                onClick = {
                    uriHandler.openUri(url)
                }
            ){
                Text("Manage Subscription")
            }
        }else{
            Button(
                onClick = {
                    changeIndex()
                }
            ){
                Text("Upgrade")
            }
        }
        Text("Next billing period: $billingPeriod")

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
                    Text("Purchase")
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









