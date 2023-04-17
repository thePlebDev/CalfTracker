package com.elliottsoftware.calftracker.presentation.sharedViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.SheetState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VaccinationView(
    vaccineText: String,
    updateVaccineText:(String)->Unit,
    dateText1:String,
    updateDateText: (String) -> Unit,
    vaccineList: List<String>,
    addItemToVaccineList:(String) -> Unit,
    removeItemFromVaccineList: (String) -> Unit
){




    val convertedDate = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    val selectedDate = remember { mutableStateOf<LocalDate?>(convertedDate) }
    val calendarState = rememberSheetState()

    CalendarView(
        calendarState,
        selectedDate = selectedDate,
        updateDateText = {
                newDate -> updateDateText(newDate)
        }
    )

    Column(){
        VaccineRow(
            vaccineText =vaccineText,
            updateText = {text -> updateVaccineText( text)},
            calendarState = calendarState,
            selectedDate = selectedDate,
            dateText = dateText1,
            updateDateText = {dateText ->updateDateText(dateText) },
            modifier = Modifier.fillMaxWidth()
        )

        VaccineButtonRow(
            vaccineText = vaccineText,
            addToVaccineList ={
                val text = "$vaccineText " + selectedDate.value
                val check = vaccineList.indexOf(text)
                if(check == -1){
                    addItemToVaccineList(text)
                    //vaccineList.add(text)
                }
            },
            modifier = Modifier.fillMaxWidth()

        )

        VaccineLazyColumn(
            vaccineList = vaccineList,
            removeItemFromList ={item ->
                removeItemFromVaccineList(item)
                //vaccineList.remove(item)
                                },
            modifier = Modifier.fillMaxWidth()
        )


    }

}

@Composable
fun CalendarView(
    calendarState: SheetState,
    selectedDate: MutableState<LocalDate?>,
    updateDateText: (String) -> Unit
){
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date(selectedDate = selectedDate.value){ newDate ->

            selectedDate.value = newDate

            updateDateText(newDate.toString())

        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VaccineLazyColumn(
    vaccineList:List<String>,
    removeItemFromList:(String)->Unit,
    modifier: Modifier = Modifier

){
    LazyColumn(modifier = modifier
        .padding(horizontal = 8.dp)
        .height(150.dp)

    ){
        itemsIndexed(
            items = vaccineList,
            key ={index:Int,item:String ->item.hashCode() + index}
        ){ index:Int, item:String ->
            /***********SETTING UP SWIPE TO DISMISS****************/
            val dismissState = rememberDismissState(
                confirmStateChange = {

                    // vaccineList.remove(item)
                    removeItemFromList(item)
                    true
                }
            )
            SwipeToDismiss(state = dismissState, background ={} ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            BorderStroke(2.dp, Color.LightGray),
                            shape = RoundedCornerShape(8)
                        )
                ){
                    Text(text = item, fontSize = 20.sp,modifier = Modifier.padding(8.dp))
                }

            }
        }



    }

}
@Composable
fun VaccineButtonRow(
    vaccineText: String,
    addToVaccineList:() -> Unit,
    modifier: Modifier = Modifier,

    ){
    Row(
        modifier = modifier
            .padding(8.dp)
    ){
        Spacer(Modifier.weight(1.5f))
        Button(
            enabled = vaccineText.length >1,
            onClick = {
                addToVaccineList()


            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Vaccinate")
        }
    }
}

@Composable
fun VaccineRow(
    vaccineText:String,
    updateText:(String)->Unit,
    calendarState: SheetState,
    selectedDate: MutableState<LocalDate?>,
    dateText: String,
    updateDateText:(String) -> Unit,
    modifier: Modifier = Modifier

){
    Row(
        modifier = modifier
            .padding(8.dp)
    ) {
        //I NEED A TEXT INPUT AND A DATE INPUT
        OutlinedTextField(
            modifier = Modifier.weight(1.5f),
            singleLine = true,
            value = vaccineText,
            onValueChange = { updateText(it) },
            textStyle = TextStyle(fontSize = 20.sp),
            placeholder = {
                Text(text = "Vaccination", fontSize = 20.sp)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            )

        )
        OutlinedTextField(
            modifier = Modifier
                .clickable { calendarState.show() }
                .weight(1f),
            enabled = false,
            value = selectedDate.value.toString(),
            onValueChange = { updateDateText(it) },
            textStyle = TextStyle(fontSize = 20.sp),
            placeholder = {
                Text(text = dateText, fontSize = 20.sp)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            )

        )
    }
}