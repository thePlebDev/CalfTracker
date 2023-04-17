package com.elliottsoftware.calftracker.presentation.sharedViews

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun GradientShimmer(){

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .width(160.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .shimmerEffect()
        ,
    ){

        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ){
            Column(modifier = Modifier.weight(2f)){

            }
            Column(modifier = Modifier.weight(1f)){


            }

        }


    }

}
@Composable
fun Shimmers(){
    Column(){
        GradientShimmer()
        GradientShimmer()
        GradientShimmer()
        GradientShimmer()
        GradientShimmer()

    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember{
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffSetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2* size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5)
            ),
            start = Offset(startOffSetX,0f),
            end = Offset(startOffSetX + size.width.toFloat(),size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}