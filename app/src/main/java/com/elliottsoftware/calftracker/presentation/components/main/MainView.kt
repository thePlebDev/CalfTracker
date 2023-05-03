package com.elliottsoftware.calftracker.presentation.components.main

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.newCalf.CalendarDock
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingUiState
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingViewModel
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionCard
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionValues
import com.elliottsoftware.calftracker.presentation.components.util.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.util.DrawerHeader
import com.elliottsoftware.calftracker.presentation.components.util.MenuItem
import com.elliottsoftware.calftracker.presentation.sharedViews.*
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import com.elliottsoftware.calftracker.util.findActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.*
import kotlinx.coroutines.delay
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView(
    viewModel: MainViewModel = viewModel(),
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    billingViewModel: BillingViewModel,
    newCalfViewModel: NewCalfViewModel
){
    AppTheme(false){
        ScaffoldView(
            viewModel,
            onNavigate,
            sharedViewModel,
            billingViewModel,
            newCalfViewModel
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldView(
    viewModel: MainViewModel = viewModel(),
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    billingViewModel: BillingViewModel,
    newCalfViewModel: NewCalfViewModel,

){

    //BottomSheetScaffold states
    var skipHalfExpanded by remember { mutableStateOf(false) }
    val bottomModalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    ModalBottomSheetLayout(
        sheetState = bottomModalState,
        sheetContent = {
            ModalContent(
                billingViewModel,
                onNavigate = onNavigate,
                bottomModalState = bottomModalState,
                scaffoldState = scaffoldState,
                newCalfViewModel = newCalfViewModel
            )
        },
        sheetBackgroundColor = MaterialTheme.colors.primary
    ){
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.primary,
        bottomBar = {
            BottomNavigation(
                navItemList = listOf(
                    NavigationItem(
                        title = "Logout",
                        contentDescription = "Logout Button",
                        icon = Icons.Default.Logout,
                        onClick = {
                            viewModel.signUserOut()
                            onNavigate(R.id.action_mainFragment2_to_loginFragment)
                                  },

                    ),
                    NavigationItem(
                        title = "Create",
                        contentDescription = "Create Button",
                        icon = Icons.Default.AddCircle,
                        onClick = {
                                  scope.launch {
                                      bottomModalState.show()
                                  }
                        },

                    ),
                    NavigationItem(
                        title = "Settings",
                        contentDescription = "Subscription Button",
                        icon = Icons.Default.Settings,
                        onClick = {
                                  onNavigate(R.id.action_mainFragment2_to_settingsFragment)
                        },

                    )

                )
            )
                    },

        topBar = {
            CustomTopBar(
                viewModel.state.value.chipText,
                {tagNumber -> viewModel.searchCalfListByTag(tagNumber)},
            )
        },
        drawerContent = {},

        ) { paddingValues ->



            HomeView(
                viewModel,onNavigate,sharedViewModel,viewModel.state.value.data,
                {chipText -> viewModel.setChipText(chipText)},
                {viewModel.getCalves()},
                updateCalfListSize = {size -> billingViewModel.updateCalfListSize(size)},
                paddingValues,

                )



        } //this should be outside, Actually this doesn't matter


    }
}



@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModalContent(
    billingViewModel: BillingViewModel,
    newCalfViewModel: NewCalfViewModel,
    onNavigate: (Int) -> Unit,
    bottomModalState:ModalBottomSheetState,
    scaffoldState:ScaffoldState,
){
    val isUserSubscribed = billingViewModel.state.value.subscribed
    val calfSize = billingViewModel.state.value.calfListSize
    val calfLimit = 50
    if(!isUserSubscribed && calfSize >= calfLimit){
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
                ),
                billingViewModel = billingViewModel,
                onNavigate = {location -> onNavigate(location)}
            )
        }
    }else{

        MainBodyView(
            bottomModalState= bottomModalState,
            scaffoldState = scaffoldState,
            newCalfViewModel = newCalfViewModel
        )
    }


}

data class SimpleTextInputData(
    val state:String,
    val placeHolderText:String,
    val updateValue:(String)->Unit,
    val errorMessage:String? = null,
    val key:Int
)

@OptIn( ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainBodyView(
   newCalfViewModel: NewCalfViewModel,
//    onNavigate: (Int) -> Unit,
//    scaffoldState: ScaffoldState,
//    padding: PaddingValues,
//    vaccineList: MutableList<String>,
//    showModal:()->Unit,
   // hideModal:()->Unit,
    bottomModalState:ModalBottomSheetState,
    scaffoldState:ScaffoldState,

) {
    val scope = rememberCoroutineScope()
    var vaccineList = remember { mutableStateListOf<String>() }
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.TopCenter){


        CalfCreationLayout(
            newCalfViewModel,
            bottomModalState,
            vaccineList = vaccineList,
            addItem = {item -> vaccineList.add(item)},
            removeItem = {item -> vaccineList.remove(item)}
        )

        when(val response = newCalfViewModel.state.value.calfSaved){
            is Response.Loading ->{
                Spacer(
                    modifier = Modifier
                        .matchParentSize()
                        .background(color = Color.Gray.copy(alpha = .7f))
                )
                CircularProgressIndicator(
                    color= MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                )
            }
            is Response.Success ->{
                if(response.data){
                    // we need to clear all the inputs, close the modal and tell the user what calf got created
                    val calfTag = newCalfViewModel.state.value.calfTag
                    LaunchedEffect(response) {
                        scope.launch {
                            bottomModalState.hide()
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Calf $calfTag Created",
                                actionLabel = "Close"
                            )
                        }
                    }


                    vaccineList.clear()
                    newCalfViewModel.clearData()
                    newCalfViewModel.resetResponse()

                }
            }
            is Response.Failure ->{
                Spacer(
                    modifier = Modifier
                        .matchParentSize()
                        .background(color = Color.Gray.copy(alpha = .7f))
                )
                CircularProgressIndicator(
                    color= MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                )
                ErrorResponse(
                    refreshMethod = {newCalfViewModel.resetResponse()},
                    errorMessageTitle = "Calf not created",
                    errorMessageBody = "A error has occurred. Please check your network question and try again",
                    errorButtonMessage = "I understand",
                    modifier=Modifier.align(Alignment.Center)

                )

            }
        }





    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CalfCreationLayout(
    newCalfViewModel: NewCalfViewModel,
    bottomModalState:ModalBottomSheetState,
    vaccineList: List<String>,
    addItem:(String) -> Unit,
    removeItem:(String)->Unit
){
    val scope = rememberCoroutineScope()
    val simpleTextInputList = listOf<SimpleTextInputData>(
        SimpleTextInputData(
            state = newCalfViewModel.state.value.calfTag,
            placeHolderText = "Calf tag number",
            updateValue = { tagNumber -> newCalfViewModel.updateCalfTag(tagNumber)},
            errorMessage = newCalfViewModel.state.value.calfTagError,
            key =0
        ),
        SimpleTextInputData(
            state = newCalfViewModel.state.value.cowTagNumber,
            placeHolderText = "Cow tag number",
            updateValue = { tagNumber -> newCalfViewModel.updateCowTagNumber(tagNumber)},
            key =1
        ),
        SimpleTextInputData(
            state = newCalfViewModel.state.value.cciaNumber,
            placeHolderText = "CCIA number",
            updateValue = { cciaNumber -> newCalfViewModel.updateCciaNumber(cciaNumber)},
            key =2
        ),
        SimpleTextInputData(
            state = newCalfViewModel.state.value.description,
            placeHolderText = "Description",
            updateValue = { description -> newCalfViewModel.updateDescription(description)},
            key =3
        ),
    )

    LazyColumn {
        stickyHeader {
            Header(
                cancelFunction = {
                    scope.launch {
                        bottomModalState.hide()
                    }
                },
                createCalf = {
                    newCalfViewModel.submitCalf(vaccineList)
                }
            )
        }
        item {
            Text(
                text = "Basic Information :",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 8.dp)
            )
        }

        items(
            simpleTextInputList,
            key = { item ->
                item.key
            }
        ) { item ->
            SimpleTextInput(
                state = item.state,
                placeHolderText = item.placeHolderText,
                updateValue = { value -> item.updateValue(value) },
                errorMessage = item.errorMessage
            )
        }
        item {
            BullHeiferRadioInput(
                state = newCalfViewModel.state.value.sex,
                updateSex = { value -> newCalfViewModel.updateSex(value) },
                modifier = Modifier
            )
        }

        item {
            NumberInput(
                "Birth Weight",
                state = newCalfViewModel.state.value.birthWeight,
                updateValue = { value -> newCalfViewModel.updateBirthWeight(value) }
            )
        }
        item {
            CalendarDock(newCalfViewModel)
        }
        item {
            Text(
                text = "Vaccination List :",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 8.dp)
            )
        }
        item {
            VaccinationView(
                vaccineText = newCalfViewModel.state.value.vaccineText,
                updateVaccineText = { text -> newCalfViewModel.updateVaccineText(text) },
                dateText1 = newCalfViewModel.state.value.vaccineDate,
                updateDateText = { date -> newCalfViewModel.updateDateText(date) },
                vaccineList = vaccineList,
                addItemToVaccineList = { item -> addItem(item) },
                removeItemFromVaccineList = { item -> removeItem(item) }
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






@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeView(
    viewModel: MainViewModel,
    onNavigate: (Int) -> Unit,
    sharedViewModel: EditCalfViewModel,
    state: Response<List<FireBaseCalf>>,
    setChipTextMethod: (data: List<FireBaseCalf>) -> Unit,
    errorRefreshMethod: () -> Unit,
    updateCalfListSize: (Int) -> Unit,
    paddingValues: PaddingValues
){



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingValues.calculateBottomPadding())


    ){
        when(state){
            is Response.Loading -> {
                Shimmers()
            }
            is Response.Success -> {

                setChipTextMethod(state.data)
                if(state.data.isEmpty()){
                    Column(){
                        Text(text = "NO CALVES",color =MaterialTheme.colors.onPrimary)

                        //NO CALVES
                    }
                }
                else{

                    updateCalfListSize(state.data.size)
                        MessageList(
                            calfList = state.data,
                            onNavigate,
                            deleteCalfMethod= {calfId -> viewModel.deleteCalf(calfId)},
                            setClickedCalf = {calf -> sharedViewModel.setCalf(calf)},
                            showDeleteModal ={value -> viewModel.setShowDeleteModal(value)},
                            showDeleteValue = viewModel.state.value.showDeleteModal,
                            setCalfDeleteTagNId = {value1,value2 -> viewModel.setCalfDeleteTagNId(value1,value2)},
                            TagNumberToBeDeleted = viewModel.state.value.calfToBeDeletedTagNumber,
                            calfId = viewModel.state.value.calfToBeDeletedId,
                            paginatedQuery = {viewModel.getPaginatedQuery()},
                            paddingValues = paddingValues

                            )

                }

            }
            is Response.Failure -> ErrorResponse(
                refreshMethod = { errorRefreshMethod() },
                errorMessageTitle = "User Notice",
                errorMessageBody = "A Error has occurred and the development team has been notified. Thank you for your continued support ",
                errorButtonMessage = "Click to reload"
            )


            else -> {}
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
    paginatedQuery:() -> Unit,
    paddingValues: PaddingValues


    ) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp // total device screen height
    val scaffoldBottomBarHeight = paddingValues.calculateBottomPadding() // bottom bar height
    val scaffoldTopBarHeight = 100.dp // top bar height, found through trial and error
    val screenHiddenByBars = scaffoldBottomBarHeight + scaffoldTopBarHeight // combination of bottom and top bar
    val screenVisibleToUser = screenHeight - screenHiddenByBars.value //lazyColumn height visible to user

    val itemHeight = 130.dp
    val totalItemHeight = calfList.size* itemHeight.value
    Timber.tag("heights").d(totalItemHeight.toString())
    Timber.tag("heights").d(screenVisibleToUser.toString())

    val userCanScroll = (totalItemHeight > screenVisibleToUser)


    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.TopCenter){
        LazyColumn(
            modifier=Modifier.background(MaterialTheme.colors.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            items(calfList,key = { it.id!! }) { calf ->

                SwipeableSample(
                    calf,
                    showDeleteModal = {value -> showDeleteModal(value)},
                    setCalfDeleteTagNId = {value1, value2 -> setCalfDeleteTagNId(value1,value2)},
                    setClickedCalf = {calf -> setClickedCalf(calf)},
                    navigate={destination -> onNavigate(destination)}
                )

            }

            if(userCanScroll){
                item{
                    Button(
                        modifier = Modifier.padding(bottom = 20.dp),
                        onClick ={paginatedQuery()},
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)

                    ){
                        Text("Load more calves",style = MaterialTheme.typography.h5)
                    }

                }
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
fun CustomTopBar(chipTextList:List<String>,searchMethod:(String)->Unit){
    Column() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            elevation = 8.dp
        ) {
            Column() {


                 SearchText(searchMethod= { tagNumber -> searchMethod(tagNumber) })
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
fun SearchText(searchMethod:(String)->Unit){
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

@RequiresApi(Build.VERSION_CODES.N)
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
                .height(110.dp)
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

data class NavigationItem(
    val title:String,
    val contentDescription:String,
    val icon: ImageVector,
    val onClick: () -> Unit ={},


)

@Composable
fun BottomNavigation(navItemList:List<NavigationItem>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceAround

    ){
        navItemList.forEach {
            BottomNavItem(
                title = it.title,
                description = it.contentDescription,
                icon = it.icon,
                onNavigate = {
                    it.onClick()

                }

            )
        }
    }
}

@Composable
fun BottomNavItem(title:String,description:String,icon:ImageVector,onNavigate: () -> Unit,){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onNavigate() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(28.dp)
        )
        Text(title)

    }
}









