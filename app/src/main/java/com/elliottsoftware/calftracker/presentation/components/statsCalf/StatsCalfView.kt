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
            SampleLineGraph(listOf(DataPoints.dataPoints1, DataPoints.dataPoints2),viewModel)
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
            Log.d("POINTS",points.toString())
            viewModel.changeText(xLine.toString())

        }
    )
}

@Composable
internal fun LineGraph1(lines: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(Color.Green, 2.dp),
                    LinePlot.Intersection(Color.Yellow, 5.dp),
                    LinePlot.Highlight(Color.Red, 5.dp),
                    LinePlot.AreaUnderLine(Color.Blue, 0.3f)
                ),
                LinePlot.Line(
                    lines[1],
                    LinePlot.Connection(Color.Gray, 2.dp),
                    LinePlot.Intersection { center, _ ->
                        val px = 2.dp.toPx()
                        val topLeft = Offset(center.x - px, center.y - px)
                        drawRect(Color.Gray, topLeft, Size(px * 2, px * 2))
                    },
                ),
            ),
            selection = LinePlot.Selection(
                highlight = LinePlot.Connection(
                    Color.Green,
                    strokeWidth = 2.dp,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
                )
            ),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
object DataPoints {
    val dataPoints1 = listOf(
        DataPoint(0f, 0f),
        DataPoint(1f, 20f),
        DataPoint(2f, 50f),
        DataPoint(3f, 10f),
        DataPoint(4f, 0f),
        DataPoint(5f, -25f),
        DataPoint(6f, -75f),
        DataPoint(7f, -100f),
        DataPoint(8f, -80f),
        DataPoint(9f, -75f),
        DataPoint(10f, -55f),
        DataPoint(11f, -45f),
        DataPoint(12f, 50f),
        DataPoint(13f, 80f),
        DataPoint(14f, 70f),
        DataPoint(15f, 125f),
        DataPoint(16f, 200f),
        DataPoint(17f, 170f),
        DataPoint(18f, 135f),
        DataPoint(19f, 60f),
        DataPoint(20f, 20f),
        DataPoint(21f, 40f),
        DataPoint(22f, 75f),
        DataPoint(23f, 50f),
    )

    val dataPoints2 = listOf(
        DataPoint(0f, 0f),
        DataPoint(1f, 0f),
        DataPoint(2f, 25f),
        DataPoint(3f, 75f),
        DataPoint(4f, 100f),
        DataPoint(5f, 80f),
        DataPoint(6f, 75f),
        DataPoint(7f, 50f),
        DataPoint(8f, 80f),
        DataPoint(9f, 70f),
        DataPoint(10f, 0f),
        DataPoint(11f, 0f),
        DataPoint(12f, 45f),
        DataPoint(13f, 20f),
        DataPoint(14f, 40f),
        DataPoint(15f, 75f),
        DataPoint(16f, 50f),
        DataPoint(17f, 75f),
        DataPoint(18f, 40f),
        DataPoint(19f, 20f),
        DataPoint(20f, 0f),
        DataPoint(21f, 0f),
        DataPoint(22f, 50f),
        DataPoint(23f, 25f),
    )



}


