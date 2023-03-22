package com.elliottsoftware.calftracker.presentation.components.subscription

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.main.FloatingButton
import com.elliottsoftware.calftracker.presentation.components.newCalf.LoadingFloatingButton
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.util.findActivity
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SubscriptionView( onNavigate: (Int) -> Unit = {}){
    AppTheme(false){
        SubscriptionViews(onNavigate = {location -> onNavigate(location)})
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
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
//                Timber.tag("anotherOne").d("WE ARE RESUMING")
                viewModel.refreshPurchases()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
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
            0 -> BuyingText(UIState,viewModel)
            1 -> Text("Premium")
            2 -> Text("Settings")

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

