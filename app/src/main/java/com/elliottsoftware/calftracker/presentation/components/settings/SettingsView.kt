package com.elliottsoftware.calftracker.presentation.components.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.newCalf.CalendarDock
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingViewModel
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionValues
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionViewExample
import com.elliottsoftware.calftracker.presentation.sharedViews.ActiveSubscription
import com.elliottsoftware.calftracker.presentation.sharedViews.NavigationItem
import com.elliottsoftware.calftracker.presentation.sharedViews.BottomNavigation
import com.elliottsoftware.calftracker.presentation.sharedViews.BullHeiferRadioInput
import com.elliottsoftware.calftracker.presentation.sharedViews.CalfCreation
import com.elliottsoftware.calftracker.presentation.sharedViews.CreateCalfLoading
import com.elliottsoftware.calftracker.presentation.sharedViews.VaccinationView
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsView(
    onNavigate: (Int) -> Unit,
    viewModel: MainViewModel,
    newCalfViewModel: NewCalfViewModel,
    billingViewModel: BillingViewModel
){
    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val scaffoldState = rememberScaffoldState()

    AppTheme(false) {

        ModalBottomSheetLayout(
            sheetState = bottomModalState,
            sheetContent = {
                ModalContent(
                    bottomModalState,
                    newCalfViewModel,
                    scaffoldState,
                    billingViewModel,
                    onNavigate

                )
            }
        ) {
            Settings(
                onNavigate,
                viewModel,
                bottomModalState,
                scaffoldState,

                )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalContent(
    bottomModalState: ModalBottomSheetState,
    newCalfViewModel: NewCalfViewModel,
    scaffoldState: ScaffoldState,
    billingViewModel:BillingViewModel,
    onNavigate: (Int) -> Unit
){
    //TODO: MAKE A CHECK TO SEE IF THERE IS THE PROPER SUBSCRIPTION
    var vaccineList = remember { mutableStateListOf<String>() }
    val isUserSubscribed = billingViewModel.state.value.isUserSubscribed
    val calfSize = billingViewModel.state.value.calfListSize
    val calfLimit = 1
    if(!isUserSubscribed && calfSize >= calfLimit){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .height(400.dp)
        ){
            ActiveSubscription(
                subscriptionInfo = SubscriptionValues(
                    description = "Premium subscription",
                    title= "Premium subscription",
                    items= "Unlimited calf storage",
                    price = "$10.00",
                    icon = Icons.Default.MonetizationOn
                ),
                billingViewModel = billingViewModel,
                onNavigate = { }
            )
            //onNavigate(R.id.action_settingsFragment_to_subscriptionFragment)
        }
    }else {
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
}



@Composable
fun Header(
    cancelFunction:() -> Unit,
    createCalf:() -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Button(onClick = {cancelFunction()},modifier = Modifier.padding(8.dp)){
            Text("Cancel",fontSize=17.sp)
        }
        Text("New Calf",fontSize=20.sp)
        Button(onClick = {createCalf()},modifier = Modifier.padding(8.dp)){
            Text("Create",fontSize=17.sp)

        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(
    onNavigate: (Int) -> Unit,
    viewModel: MainViewModel,
    bottomModalState: ModalBottomSheetState,
    scaffoldState: ScaffoldState,
){
    val scope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = { TopBar(onNavigate) },
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomNavigation(
                navItemList = listOf(
                    NavigationItem(
                        title = "Logout",
                        contentDescription = "Logout Button",
                        icon = Icons.Outlined.Logout,
                        onClick = {
                            viewModel.signUserOut()

                            onNavigate(R.id.action_settingsFragment_to_loginFragment2)

                        },
                        color = Color.Black,


                        ),
                    NavigationItem(
                        title = "Home",
                        contentDescription = "Navigate back to home page",
                        icon = Icons.Outlined.Home,
                        onClick = {
                            onNavigate(R.id.action_settingsFragment_to_mainFragment2)

                        },
                        color = Color.Black

                        ),
                    NavigationItem(
                        title = "Create",
                        contentDescription = "Launch the create Calf widget",
                        icon = Icons.Default.AddCircle,
                        onClick = {
                            onNavigate(R.id.action_settingsFragment_to_mainFragment2)
                        },
                        color = Color.Black,
                        weight = 1.3f,
                        modifier = Modifier.size(45.dp)

                        ),
                    NavigationItem(
                        title = "Settings",
                        contentDescription = "Navigate to the settings page",
                        icon = Icons.Default.Settings,
                        onClick = {},
                        color = Color.Black

                    ),
                    NavigationItem(
                        title = "Features",
                        contentDescription = "navigate to the subscriptions page. You can view and modifier your subscriptions",
                        icon = Icons.Outlined.MonetizationOn,
                        onClick = {
                             onNavigate(R.id.action_settingsFragment_to_subscriptionFragment)
                        },
                        color = Color.Black

                    )

                )
            )
        },
    ){ paddingValues ->
        MainView(
            paddingValues,
            onNavigate,

        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(
    paddingValues: PaddingValues,
    onNavigate: (Int) -> Unit,

){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ){
        LazyVerticalGridDemo(onNavigate)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LazyVerticalGridDemo(
    onNavigate: (Int) -> Unit,

){
    val uriHandler = LocalUriHandler.current

    val url = "https://www.reddit.com/r/CalfTrackerAndroid/";

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        item{
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onNavigate(R.id.action_settingsFragment_to_subscriptionFragment)
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
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "click for subscriptions",
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Subscription")
                    Text(
                        text ="View and update current subscriptions",
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally).alpha(0.8f).align(Alignment.CenterHorizontally)
                    )
                }

            }


        }
        item{
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        uriHandler.openUri(url)
                    },
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = 8.dp,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 4.dp).fillMaxWidth(),

                    ){
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "click to chat with the developers on reddit",
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Reddit", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(
                        text ="Chat with developers on reddit",
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally).alpha(0.8f).align(Alignment.CenterHorizontally)
                    )
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
                        .weight(1f)
                        .clickable { onNavigate(R.id.action_settingsFragment_to_mainFragment2) },
                    imageVector = Icons.Default.KeyboardBackspace,
                    contentDescription ="Return to home screen",
                )
                Text("Settings", fontSize = 30.sp,modifier = Modifier.weight(2f))
            }
        }
    }
}



