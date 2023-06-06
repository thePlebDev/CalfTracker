package com.elliottsoftware.calftracker.presentation.components.subscription

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
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
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.navigation.Navigation
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.sharedViews.CalfCreation
import com.elliottsoftware.calftracker.presentation.sharedViews.CreateCalfLoading
import com.elliottsoftware.calftracker.presentation.sharedViews.NavigationItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import com.elliottsoftware.calftracker.util.findActivity
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import com.elliottsoftware.calftracker.presentation.sharedViews.BottomNavigation
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubscriptionView(
    onNavigate: (Int) -> Unit = {},
    viewModel:BillingViewModel,
    newCalfViewModel:NewCalfViewModel,
    mainViewModel: MainViewModel
){
    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scaffoldState = rememberScaffoldState()
    AppTheme(false){

        ModalBottomSheetLayout(
            sheetState = bottomModalState,
            sheetContent = {
                ModalContent(
                    bottomModalState,
                    newCalfViewModel,
                    scaffoldState
                )
            }
        ){
            SubscriptionViews(
                onNavigate = {location -> onNavigate(location)},
                billingViewModel = viewModel,
                bottomModalState = bottomModalState,
                mainViewModel = mainViewModel
            )
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SubscriptionViews(
   // subscriptionViewModel: SubscriptionViewModel = viewModel(),
    onNavigate: (Int) -> Unit = {},
    billingViewModel:BillingViewModel = viewModel(),
    bottomModalState: ModalBottomSheetState,
    mainViewModel: MainViewModel
){

    val scope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            TopBar(
                onNavigate = onNavigate
            )
        },
        bottomBar = {
            BottomNavigation(
                navItemList = listOf(
                    NavigationItem(
                        title = "Logout",
                        contentDescription = "Logout Button",
                        icon = Icons.Outlined.Logout,
                        onClick = {
                            mainViewModel.signUserOut()
                            onNavigate(R.id.action_subscriptionFragment_to_loginFragment)

                        },
                        color = Color.Black,


                        ),
                    NavigationItem(
                        title = "Home",
                        contentDescription = "Navigate back to home page",
                        icon = Icons.Outlined.Home,
                        onClick = {
                            onNavigate(R.id.action_subscriptionFragment_to_mainFragment22)

                        },
                        color = Color.Black

                    ),
                    NavigationItem(
                        title = "Create",
                        contentDescription = "Launch the create Calf widget",
                        icon = Icons.Default.AddCircle,
                        onClick = {
                            scope.launch {
                                bottomModalState.show()
                            }
                        },
                        color = Color.Black,
                        weight = 1.3f,
                        modifier = Modifier.size(45.dp)

                    ),
                    NavigationItem(
                        title = "Settings",
                        contentDescription = "Navigate to the settings page",
                        icon = Icons.Outlined.Settings,
                        onClick = {
                            onNavigate(R.id.action_subscriptionFragment_to_settingsFragment)
                        },
                        color = Color.Black

                    ),
                    NavigationItem(
                        title = "Features",
                        contentDescription = "navigate to the subscriptions page. You can view and modifier your subscriptions",
                        icon = Icons.Default.MonetizationOn,
                        onClick = {

                        },
                        color = Color.Black

                    )

                )
            )
        }
    ){paddingValues ->
        SubscriptionBody(
            paddingValues,
            billingViewModel
        )


    }



}

@Composable
fun SubscriptionBody(
    paddingValues:PaddingValues,
    billingViewModel: BillingViewModel
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)


    ){
        Column( modifier = Modifier
            .matchParentSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())){
            Spacer(modifier = Modifier.padding(20.dp))
            CurrentSubscriptionInfo(
                title = "Current",
                icon = Icons.Outlined.CurrencyExchange,
                iconDescription = "Current Active Subscription"
            ){

                Subscribed(billingViewModel)

            }
            Spacer(modifier = Modifier.padding(20.dp))
            CurrentSubscriptionInfo(
                title = "Premium",
                icon = Icons.Outlined.Paid,
                iconDescription = "Premium Subscription information"
            ){
               // Premium()
                BuyingText(
                    productDetailsResponse = billingViewModel.state.value.productDetails,
                    billingViewModel = billingViewModel
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            CurrentSubscriptionInfo(
                title = "Details",
                icon = Icons.Outlined.Info,
                iconDescription = "Subscription plan info"
            ){
                Text(text = "DETAILS",fontSize =30.sp)
                Text(text = "DETAILS",fontSize =30.sp)
                Text(text = "DETAILS",fontSize =30.sp)
            }
        }



    }

}
@Composable
fun CurrentSubscriptionInfo(
    title:String,
    icon:ImageVector,
    iconDescription:String,
    content: @Composable() (() -> Unit)
){
    var expanded by remember { mutableStateOf(false) }
    // initialize focus reference to be able to request focus programmatically
    val focusRequester = remember { FocusRequester() }
    // MutableInteractionSource to track changes of the component's interactions (like "focused")
    val interactionSource = remember { MutableInteractionSource() }
    // text below will change when we focus it via button click
    val isFocused = interactionSource.collectIsFocusedAsState().value

    Card(
        modifier = Modifier
            .focusRequester(focusRequester) //register focus changes
            .focusable(interactionSource = interactionSource) //emit focus events
            .clickable {
                focusRequester.requestFocus()
            } // makes it all work


    ){
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Box(){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Icon(
                        modifier = Modifier.size(34.dp),
                        imageVector = icon,
                        contentDescription = iconDescription)
                    Text(
                        text =title,
                        modifier = Modifier.padding(start = 40.dp),
                        fontSize = 30.sp
                    )
                }
                Icon(
                    imageVector =  if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore ,
                    contentDescription = "expand",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)

                )
            }
            if(isFocused){
                content()
            }
        }
    }

}

@Composable
fun Subscribed(billingViewModel: BillingViewModel){
    when(val response = billingViewModel.state.value.isUserSubscribed){
        is Response.Loading->{
            Text(text = "LOADING",fontSize =30.sp)
        }
        is Response.Success->{

            Text(text = "Subscribed: ${response.data}",fontSize =30.sp)
        }
        is Response.Failure->{
            Text(text = "Failed to load details",fontSize =30.sp)
        }
    }

}




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalContent(
    bottomModalState: ModalBottomSheetState,
    newCalfViewModel: NewCalfViewModel,
    scaffoldState: ScaffoldState
){
    //TODO: MAKE A CHECK TO SEE IF THERE IS THE PROPER SUBSCRIPTION
    var vaccineList = remember { mutableStateListOf<String>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.TopCenter
    ){

        CalfCreation(
            bottomModalState,
            newCalfViewModel,
            vaccineList = vaccineList,
            addItem = {item -> vaccineList.add(item)},
            removeItem = {item -> vaccineList.remove(item)}
        )
        CreateCalfLoading(
            modifier = Modifier.matchParentSize(),
            loadingIconAlignmentModifier = Modifier.align(Alignment.Center),
            newCalfViewModel = newCalfViewModel,
            bottomModalState = bottomModalState,
            scaffoldState = scaffoldState,
            clearVaccineList = {vaccineList.clear()}
        )


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

            2 -> Settings(billingUiState=viewModel.state.value, billingViewModel = viewModel)

        }
    }
}



@Composable
fun Settings(billingUiState: BillingUiState,billingViewModel: BillingViewModel){

    Column(modifier = Modifier.padding(15.dp)) {
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
fun MyButton(url:String) {//THIS SHOULD BE IN THE SETTINGS
    val uriHandler = LocalUriHandler.current
    Button(onClick = { uriHandler.openUri(url) }){
        Text("Subscriptions")
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












/******BELOW IS ALL THE BUYING THINGS*******/
@Composable
fun BuyingText(productDetailsResponse: Response<ProductDetails>, billingViewModel: BillingViewModel) {
    val context = LocalContext.current

    val activity = context.findActivity()



    Column() {


        when(val response = productDetailsResponse){
            is Response.Loading -> {
                Button(onClick = {}){
                    Text("Loading buy Text")
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


@Composable
fun TopBar(onNavigate: (Int) -> Unit){
    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                Icon(
                    modifier = Modifier
                        .size(38.dp)
                        .clickable { onNavigate(R.id.action_subscriptionFragment_to_mainFragment22) }
                        .weight(1f),
                    imageVector = Icons.Default.KeyboardBackspace,
                    contentDescription ="Return to home screen",
                )
                Text("Subscriptions", fontSize = 30.sp,modifier = Modifier.weight(2f))
            }
        }
    }
}






