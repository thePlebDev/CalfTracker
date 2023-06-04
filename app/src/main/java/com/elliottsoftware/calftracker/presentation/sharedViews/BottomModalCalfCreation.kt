package com.elliottsoftware.calftracker.presentation.sharedViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.newCalf.CalendarDock
import com.elliottsoftware.calftracker.presentation.components.settings.Header
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import kotlinx.coroutines.launch

/**
 * Used to hold data related to inputs related to calf creation
 * @param state The state of the text being typed.
 * @param placeHolderText The placeholder displayed to the user if there is no input.
 * @param updateValue Callback used to update the state parameter
 * @param errorMessage a optional errorMessage to be shown to the user
 * @param key  needed when used with [items](https://developer.android.com/jetpack/compose/lists#item-keys)
 */
data class SimpleTextInputData(
    val state:String,
    val placeHolderText:String,
    val updateValue:(String)->Unit,
    val errorMessage:String? = null,
    val key:Int
)

/**
 * Used to hold all the related UI components related to calf creation
 * @param bottomModalState State of the [ModalBottomSheetLayout](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ModalBottomSheetLayout(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material.ModalBottomSheetState,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function0)) composable.
 * @param newCalfViewModel The ViewModel responsible for creating and handling all the state related to calf creation
 * @param vaccineList a list of Strings representing all the vaccines the calf has taken
 * @param addItem method used to add vaccines to vaccineList
 * @param removeItem  method used to remove vaccines to vaccineList
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CalfCreation(
    bottomModalState: ModalBottomSheetState,
    newCalfViewModel: NewCalfViewModel,
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
    LazyColumn{
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

        //THIS IS THE CALF CREATION
    }

}

/**
 * Used to hold all the related UI components related to calf creation
 * @param modifier optional parameter, used to fill loading animation in the entire screen. For desired effect, use, Modifier.matchParentSize()
 * @param loadingIconAlignmentModifier modifier used to align the loading icon within a jetpack compose Box
 * @param newCalfViewModel The ViewModel responsible for creating and handling all the state related to calf creation
 * @param bottomModalState State of the [ModalBottomSheetLayout](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ModalBottomSheetLayout(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material.ModalBottomSheetState,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function0)) composable.
 * @param scaffoldState  State for Scaffold composable component.Contains basic screen state, e.g. Drawer configuration, as well as sizes of components after layout has happened
 * @param clearVaccineList  Method used to clear the entire vaccine string list
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCalfLoading(
    modifier:Modifier = Modifier,
    loadingIconAlignmentModifier: Modifier,
    newCalfViewModel: NewCalfViewModel,
    bottomModalState: ModalBottomSheetState,
    scaffoldState: ScaffoldState,
    clearVaccineList:()-> Unit
){
    val scope = rememberCoroutineScope()
    when(val response = newCalfViewModel.state.value.calfSaved){
        is Response.Loading ->{
            Spacer(
                modifier = modifier
                    .background(color = Color.Gray.copy(alpha = .7f))
            )
            CircularProgressIndicator(
                color= MaterialTheme.colors.onSecondary,
                modifier = loadingIconAlignmentModifier
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
                clearVaccineList()
                newCalfViewModel.clearData()
                newCalfViewModel.resetResponse()

            }
        }

        is Response.Failure ->{

        }

    }
}