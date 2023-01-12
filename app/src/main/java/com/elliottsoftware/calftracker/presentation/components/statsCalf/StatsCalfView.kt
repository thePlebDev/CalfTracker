package com.elliottsoftware.calftracker.presentation.components.statsCalf

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch



@Composable
fun StatsCalfView(){
    AppTheme(false) {
        MainView()

    }
}

data class  PieChartInput(
    val color:Color,
    val value:Int,
    val description:String,
    val isTapped:Boolean = false
)

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

        Column() {
            SampleLineGraph(listOf(DataPoints.dataPoints1),viewModel)
            TestingText(viewModel.uiState.value)
        }


    }
}

@Composable
fun TestingText(text:String){
    Text(text)
}


@Composable
fun SampleLineGraph(lines: List<List<DataPoint>>,viewModel: StatsViewModel) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(color = Color.Red),
                    LinePlot.Intersection(color = Color.Red),
                    LinePlot.Highlight(color = Color.Yellow),
                )
            ),
            grid = LinePlot.Grid(Color.Blue, steps = 4),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onSelection = { xLine, points ->
            // Do whatever you want here
            Log.d("POINTS", points[0].y.toString())
            viewModel.changeText(points[0].y.toString())

        }
    )
}

object DataPoints {
    val dataPoints1 = listOf(
        DataPoint(0f, 0f),
        DataPoint(1f, 2f),
        DataPoint(2f, 1f),
        DataPoint(3f, 1f),
        DataPoint(4f, 5f),
        DataPoint(5f, 2f),
        DataPoint(6f, 0f),
        DataPoint(7f, 0f),
        DataPoint(8f, 0f),
        DataPoint(9f, 1f),
        DataPoint(10f, 1f),
        DataPoint(11f, 3f),
    )



}



