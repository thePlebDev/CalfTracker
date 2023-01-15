package com.elliottsoftware.calftracker.presentation.components.statsCalf

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.elliottsoftware.calftracker.presentation.theme.AppTheme
import com.elliottsoftware.calftracker.presentation.viewModels.StatsViewModel

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.util.*
import kotlinx.coroutines.launch
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun StatsCalfView(){
    AppTheme(false) {
        MainView()

    }
}


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainView(viewModel:StatsViewModel = viewModel()){

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calf Tracker") },
                navigationIcon = {

                },
                actions = {}
            )
        },
    ) {
        // Screen content

        Column(modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxHeight()) {

//            SampleLineGraph(listOf(DataPoints.dataPoints1),viewModel)
//            HeaderInfo()
//            CalfListView(viewModel.uiState.value.calfList)
            
            LoadingShimmer(imageHeight = 260.dp)


        }


    }
}

@Composable
fun HeaderInfo(){

        Column(modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background( MaterialTheme.colors.primary)
        ) {
            Text("Date : 2022-02-02",style = MaterialTheme.typography.h6)
            Text("Bulls : 2",style = MaterialTheme.typography.h6)
            Text("Heifers : 1",style = MaterialTheme.typography.h6)
        }


}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CalfListView( calfList:List<FireBaseCalf>){
    if(calfList.isEmpty()){
        Text("NOTHING CLICKED")
    }else{
        LazyColumn(){
            //TODO: ADD THIS BACK TO ITEMS: ,key = { it.id!! }
            items(calfList) { calf ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                    //    .background(MaterialTheme.colors.primary) /*******THIS IS WHAT IS COLORING THE CORNERS********/

                    ,
                    elevation = 2.dp,
                    backgroundColor = MaterialTheme.colors.secondary, /******THIS IS WHAT I CHANGED*******/
                    shape = RoundedCornerShape(corner = CornerSize(16.dp))
                ){
                    Row(
                        modifier = Modifier.padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Column(modifier = Modifier.weight(2f)){

                            Text(calf.calfTag!!,style= MaterialTheme.typography.h6,color=MaterialTheme.colors.onSecondary, textAlign = TextAlign.Start,maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(calf.details!!,style= MaterialTheme.typography.subtitle1,color=MaterialTheme.colors.onSecondary,maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Weight: " + calf.birthWeight!!,style= MaterialTheme.typography.subtitle1,color=MaterialTheme.colors.onSecondary,maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        Column(modifier = Modifier.weight(1f)){


                            Text(DateFormat.getDateInstance().format(calf.date),style= MaterialTheme.typography.subtitle1,color=MaterialTheme.colors.onSecondary,)
                            Text(calf.sex!!,style= MaterialTheme.typography.subtitle1,color=MaterialTheme.colors.onSecondary,)
                        }

                    }


                }

            }
        }
    }

}


@Composable
fun SampleLineGraph(lines: List<List<DataPoint>>, viewModel: StatsViewModel) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(color = MaterialTheme.colors.onPrimary),
                    LinePlot.Intersection(color = MaterialTheme.colors.onPrimary),
                    LinePlot.Highlight(color = Color.Yellow),
                )
            ),
            grid = LinePlot.Grid(MaterialTheme.colors.onPrimary, steps = 4),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onSelection = { xLine, points ->
            // Do whatever you want here
            Log.d("POINTS", points[0].y.toString())
            viewModel.changeListUI(points[0].calves)

        }
    )
}

val calf = FireBaseCalf("22d2",
    "22d2",
    "22d2ddrew4r","Bull","STUFF AND THINGS", Date(),"222"
)

object DataPoints {
    val dataPoints1 = listOf(
        DataPoint(0f, 0f, listOf(calf)),
        DataPoint(1f, 2f, listOf(calf,calf)),
        DataPoint(2f, 1f, listOf(calf)),
        DataPoint(3f, 1f, listOf(calf,calf)),
        DataPoint(4f, 5f, listOf(calf)),
        DataPoint(5f, 2f, listOf(calf,calf)),
        DataPoint(6f, 0f, listOf(calf)),
        DataPoint(7f, 0f, listOf(calf,calf)),
        DataPoint(8f, 0f, listOf(calf)),
        DataPoint(9f, 1f, listOf(calf,calf)),
        DataPoint(10f, 1f, listOf(calf)),
        DataPoint(11f, 3f, listOf(calf,calf)),
    )



}



