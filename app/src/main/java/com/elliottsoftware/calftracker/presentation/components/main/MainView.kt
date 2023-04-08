package com.elliottsoftware.calftracker.presentation.components.main

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.icu.text.DateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingViewModel
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionCard
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionValues
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import com.google.android.material.internal.ViewUtils.dpToPx
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainView(
    viewModel: MainViewModel = viewModel(),
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    billingViewModel: BillingViewModel
){
    AppTheme(false){
        ScaffoldView(viewModel,onNavigate,sharedViewModel)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(viewModel: MainViewModel = viewModel(),onNavigate: (Int) -> Unit,sharedViewModel: EditCalfViewModel){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    //BottomSheetScaffold states
    var skipHalfExpanded by remember { mutableStateOf(false) }
    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )

    Scaffold(

        backgroundColor = MaterialTheme.colors.primary,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = {
            FloatingButton(
                navigate = onNavigate,
                showSheetState = {scope.launch { bottomModalState.animateTo(ModalBottomSheetValue.Expanded) }}
            )
                               },
        topBar = {
            CustomTopBar(
                viewModel.state.value.chipText,
                {tagNumber -> viewModel.searchCalfListByTag(tagNumber)},
                scope,
                scaffoldState
            )
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id= "logout",
                        title="Logout",
                        contentDescription = "logout of account",
                        icon = Icons.Default.Logout,
                        onClick = {
                            scope.launch {
                                viewModel.signUserOut()
                                onNavigate(R.id.action_mainFragment2_to_loginFragment)
                                scaffoldState.drawerState.close()

                            }
                        }
                    ),
                    MenuItem(
                        id= "subscriptions",
                        title="Subscriptions",
                        contentDescription = "check your subscriptions",
                        icon = Icons.Default.CurrencyExchange,
                        onClick = {
                            scope.launch {
                                onNavigate(R.id.action_mainFragment2_to_subscriptionFragment)
                                scaffoldState.drawerState.close()

                            }
                        }
                    ),
                    MenuItem(
                        id= "weather",
                        title="Weather",
                        contentDescription = "Weather",
                        icon = Icons.Default.Satellite,
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.close()
                                onNavigate(R.id.action_mainFragment2_to_weatherFragment)

                            }
                        }
                    ),
                  

                )
            )
            //END OF THE DRAWER BODY
           // ThemeToggle()
        },

        ) {

        ModalBottomSheetLayout(
            sheetState = bottomModalState,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ){
                    ActiveSubscription(
                        subscriptionInfo = SubscriptionValues(
                            description = "Premium subscription",
                            title= "Premium subscription",
                            items= "Unlimited calf storage",
                            price = "$10.00",
                            icon = Icons.Default.MonetizationOn
                        )
                    )
                }
            },
            sheetBackgroundColor = MaterialTheme.colors.primary
        ){
            HomeView(
                viewModel,onNavigate,sharedViewModel,viewModel.state.value.data,
                {chipText -> viewModel.setChipText(chipText)},
                {viewModel.getCalves()}
            )
        }




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
fun ActiveSubscription(subscriptionInfo: SubscriptionValues){

    Column(modifier = Modifier.padding(15.dp)) {
        Text("Oops!", style = MaterialTheme.typography.h5)
        Text(
            "Looks like you hit the limit on your free tier and will need to upgrade for unlimited calf storage.",

            color = Color.Black.copy(alpha = 0.6f),

        )
        Text("This subscription will auto renew every 30 days. You can cancel any time in the ",color = Color.Black.copy(alpha = 0.6f))
        ClickText()

        SubscriptionCardInfo(
            subscriptionInfo
        )
        BuyButton()


    }
}
@Composable
fun ClickText(){
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val packageName =  context.applicationContext.packageName
    val subscriptionId = "calf_tracker_premium_10"
    val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL = "https://play.google.com/store/account/subscriptions?product=%s&package=%s"
    val url = String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL,
        subscriptionId, packageName);

    ClickableText(
        onClick ={uriHandler.openUri(url)},
        text = AnnotatedString("Google Play store"),
        style = TextStyle(
            fontSize =  20.sp,
            color= Color(R.color.linkColor)
        ),
        modifier = Modifier.padding(bottom = 20.dp)
    )

}

@Composable
fun BuyButton(){
    Button(onClick = {
//    billingViewModel.buy(
//        productDetails = response.data,
//        currentPurchases = null,
//        activity = activity,
//        tag = "calf_tracker_premium"
//    )
    }
    ){
        Text("Purchase")
    }
}




@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeView(
    viewModel: MainViewModel,
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    state:Response<List<FireBaseCalf>>,
    setChipTextMethod:(data:List<FireBaseCalf>) -> Unit,
    errorRefreshMethod:()->Unit
){



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
           // .padding(horizontal = 8.dp)


    ){
        when(state){
            is Response.Loading -> CircularProgressIndicator( color =MaterialTheme.colors.onPrimary)
            is Response.Success -> {

                setChipTextMethod(state.data)
                if(state.data.isEmpty()){
                    Column(){
                        Text(text = "NO CALVES",color =MaterialTheme.colors.onPrimary)

                        //NO CALVES
                    }
                }
                else{



                        MessageList(
                            calfList = state.data,
                            onNavigate,
                            deleteCalfMethod= {calfId -> viewModel.deleteCalf(calfId)},
                            setClickedCalf = {calf -> sharedViewModel.setCalf(calf)},
                            showDeleteModal ={value -> viewModel.setShowDeleteModal(value)},
                            showDeleteValue = viewModel.state.value.showDeleteModal,
                            setCalfDeleteTagNId = {value1,value2 -> viewModel.setCalfDeleteTagNId(value1,value2)},
                            TagNumberToBeDeleted = viewModel.state.value.calfToBeDeletedTagNumber,
                            calfId = viewModel.state.value.calfToBeDeletedId
                            )

                }

            }
            is Response.Failure -> ErrorResponse(refreshMethod = { errorRefreshMethod() })


        }


    }


}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MessageList(
    calfList: List<FireBaseCalf>,
    onNavigate: (Int) -> Unit,
    deleteCalfMethod:(String) -> Unit,
    setClickedCalf:(FireBaseCalf) -> Unit,
    showDeleteModal:(Boolean) -> Unit,
    showDeleteValue:Boolean,
    setCalfDeleteTagNId:(String,String) ->Unit,
    TagNumberToBeDeleted:String,
    calfId:String,

    ) {


    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.TopCenter){
        LazyColumn(modifier=Modifier.background(MaterialTheme.colors.primary)) {


            items(calfList,key = { it.id!! }) { calf ->

                SwipeableSample(
                    calf,
                    showDeleteModal = {value -> showDeleteModal(value)},
                    setCalfDeleteTagNId = {value1, value2 -> setCalfDeleteTagNId(value1,value2)},
                    setClickedCalf = {calf -> setClickedCalf(calf)},
                    navigate={destination -> onNavigate(destination)}
                )

            }

        }//end of the lazy column
        if(showDeleteValue){
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = Color.Gray.copy(alpha = .7f))
            )
            ConfirmDelete(
                deleteCalfMethod = {value -> deleteCalfMethod(value)},
                cancelCalfDelete = {value -> showDeleteModal(value)},
                tagNumber = TagNumberToBeDeleted,
                id= calfId

            )

        }else{

        }


    }




}



@Composable
fun FloatingButton(navigate:(Int)-> Unit,showSheetState:()-> Unit = {}){
   // val scope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = {
          //  navigate(R.id.action_mainFragment2_to_newCalfFragment)
//            scope.launch {
//                sheetState.show()
//            }
                  showSheetState()
                  },
        backgroundColor = MaterialTheme.colors.secondary,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary
            )
        }
    )
}

@Composable
fun CustomTopBar(chipTextList:List<String>,searchMethod:(String)->Unit, scope: CoroutineScope, scaffoldState: ScaffoldState){
    //TODO: MOVE THIS TO THE MAIN FRAGMENT
//    val viewSize = rememberWindowSize()
//    val value = viewSize.width



    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            elevation = 8.dp
        ) {
            Column() {


                 SearchText(searchMethod= { tagNumber -> searchMethod(tagNumber) },scope,scaffoldState)
                    //CHIPS GO BELOW HERE
                    LazyRow(
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(chipTextList){
                            Chip(it)
                        }
                    }






            }


        }
        
    }
}

@Composable
fun SearchText(searchMethod:(String)->Unit,scope: CoroutineScope, scaffoldState: ScaffoldState){
    var tagNumber by remember { mutableStateOf("") }
    var clicked by remember { mutableStateOf(false)}
    val source = remember {
        MutableInteractionSource()
    }
    val focusManager = LocalFocusManager.current

    if ( source.collectIsPressedAsState().value){
        clicked = true
    }
    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            scope.launch { scaffoldState.drawerState.open() }
        }) {
            Icon(Icons.Filled.Menu, contentDescription = "Toggle navigation drawer")
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = {Text("Search by tag number")},
            value = tagNumber, onValueChange = {tagNumber = it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            trailingIcon = {
                if(clicked){
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Clear search icon",
                        modifier = Modifier.clickable {
                            tagNumber = ""
                            searchMethod("")
                            focusManager.clearFocus()
                        }
                    )
                }else{
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                }

            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchMethod(tagNumber)
                    focusManager.clearFocus()
                }),
            interactionSource = source,
        )
    }
}


@Composable
fun Chip(value:String){
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colors.secondary
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(8.dp)
        )

    }
}


/**************ERRORS*****************/
@Composable
fun ErrorResponse(refreshMethod:()->Unit) {
    Card(backgroundColor = MaterialTheme.colors.secondary,modifier = Modifier.padding(vertical = 20.dp)) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text("User Notice", style = MaterialTheme.typography.h4)
            Text("A Error has occurred and the development team has been notified. Thank you for your continued support ",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center
            )
            ErrorButton(refreshMethod)
        }
    }

}

@Composable
fun ErrorButton(refreshMethod:()->Unit) {
    Button(onClick = {
        refreshMethod()
    }) {
        Text(text = "Click to reload",
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center)
    }
}




@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeableSample(
    calf:FireBaseCalf,
    showDeleteModal:(Boolean) -> Unit,
    setCalfDeleteTagNId:(String,String)->Unit,
    setClickedCalf:(FireBaseCalf) -> Unit,
    navigate: (Int) -> Unit,
) {


    CalfCard(
        calf,
        showDeleteModal = {value -> showDeleteModal(value)},
        setCalfDeleteTagNId = {value1, value2 -> setCalfDeleteTagNId(value1,value2)},
        setClickedCalf = {calf -> setClickedCalf(calf)},
        navigate={destination -> navigate(destination)}
    )




}


@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalfCard(
    calf:FireBaseCalf,
    showDeleteModal:(Boolean) -> Unit,
    setCalfDeleteTagNId:(String,String)->Unit,
    setClickedCalf:(FireBaseCalf) -> Unit,
    navigate: (Int) -> Unit,
){
    val swipeableState = rememberSwipeableState(0)
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to 0,
                    -130f to 1 // THIS IS THE LEFT SWIPE
                ),
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )


            .background(MaterialTheme.colors.primary)

    ){

        Card(
            modifier = Modifier
                .width(160.dp)
                .height(110.dp)
                .align(Alignment.CenterEnd)

                .padding(horizontal = 15.dp, vertical = 8.dp)
                .clickable {
                    setCalfDeleteTagNId(calf.calftag!!, calf.id!!)
                    showDeleteModal(true)
                },
            elevation = 2.dp,
            backgroundColor = Color.Red,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            /*******THIS CARD IS WHERE WE WANT TO ADD THE CLICK TO SHOW THE CONFIRM DELETE CALF********/


        ){
            Column(

                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center

            ){
                Icon(Icons.Filled.Delete, "delete calf", tint = Color.Black,modifier = Modifier.size(80.dp))
            }

        }
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable {
                    //THIS IS WHERE THE NAVIGATION WILL GO
                    setClickedCalf(calf)
                    navigate(R.id.action_mainFragment2_to_editCalfFragment)

                }
            ,
            elevation = 2.dp,
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ){

            Row(
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Column(modifier = Modifier.weight(2f)){

                    Text(calf.calftag!!,style=typography.h6,color=MaterialTheme.colors.onSecondary, textAlign = TextAlign.Start,maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(calf.details!!,style=typography.subtitle1,color=MaterialTheme.colors.onSecondary,maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Column(modifier = Modifier.weight(1f)){


                    Text(DateFormat.getDateInstance().format(calf.date),style=typography.subtitle1,color=MaterialTheme.colors.onSecondary,)
                    Text(calf.sex!!,style=typography.subtitle1,color=MaterialTheme.colors.onSecondary)
                }

            }


        }
    }

}

@Composable
fun ConfirmDelete(
    deleteCalfMethod: (String) -> Unit,
    cancelCalfDelete: (Boolean) -> Unit,
    tagNumber:String,
    id:String
) {

    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .border(width = 2.dp, color = Color.Red)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(tagNumber, style = MaterialTheme.typography.h4)
            Text("Are you sure you want to delete this calf ? ",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center
            )
            DeleteCalfButton(deleteCalfMethod = {deleteCalfMethod(id)}, cancelCalfDelete = {value -> cancelCalfDelete(value)})
        }
    }

}

@Composable
fun DeleteCalfButton(deleteCalfMethod:() -> Unit,cancelCalfDelete:(Boolean) -> Unit) {
    Row(horizontalArrangement  =  Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){

        Button(onClick = {
            cancelCalfDelete(false)
        }) {
            Text(text = "Cancel",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center)
        }

        Button(onClick = {
            deleteCalfMethod()
            cancelCalfDelete(false)
        }) {
            Text(text = "Confirm",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center)
        }
    }

}







