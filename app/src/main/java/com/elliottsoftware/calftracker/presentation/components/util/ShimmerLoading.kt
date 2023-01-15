package com.elliottsoftware.calftracker.presentation.components.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset


import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun GradientDemo(){
    val colors = listOf(
        Color.LightGray.copy(alpha = .9f),
        Color.LightGray.copy(alpha = .3f),
        Color.LightGray.copy(alpha = .9f),
    )
    val brush = linearGradient(
        colors,
        start = Offset(200f,200f),
        end = Offset(400f,400f)
    )
    Surface(shape = MaterialTheme.shapes.small) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush)
        )

    }
}
@Composable
fun ShimmerCardItem(
    colors:List<Color>,
    xShimmer:Float,
    yShimmer:Float,
    cardHeight: Dp,
    gradientWidth:Float,
    padding:Dp

    ){
    val brush = linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth,yShimmer - gradientWidth),
        end = Offset(xShimmer,yShimmer)
    )
    Column(modifier = Modifier.padding(padding)) {
        Card(shape = MaterialTheme.shapes.small) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(brush = brush)
            )

            Column(
                modifier = Modifier.height(cardHeight).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary,modifier = Modifier.align(alignment = Alignment.CenterHorizontally))

            }
        }

      //  Text("Loading",style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onPrimary)

    }

}

@Composable
fun LoadingShimmer(
    imageHeight:Dp,
    padding:Dp = 16.dp
){
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val cardWidthPx = with(LocalDensity.current){(maxWidth - (padding * 2)).toPx()}
        val cardHeightPx = with(LocalDensity.current){(imageHeight - padding).toPx()}
        val gradientWidth:Float = (0.2f * cardHeightPx)
        
        val infiniteTransition = rememberInfiniteTransition()
        val xChardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardWidthPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )
        val yChardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardHeightPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )
        val colors = listOf(
        Color.LightGray.copy(alpha = .9f),
        Color.LightGray.copy(alpha = .3f),
        Color.LightGray.copy(alpha = .9f),
    )
        ShimmerCardItem(
            colors,
            xChardShimmer.value,
            yChardShimmer.value,
            imageHeight,
            gradientWidth,
            padding
        )
    }

}