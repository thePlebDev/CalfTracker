package com.elliottsoftware.calftracker.presentation.components.subscription

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.elliottsoftware.calftracker.presentation.sharedViews.BannerCard
import com.elliottsoftware.calftracker.presentation.theme.AppTheme

@Composable
fun SubscriptionViews(){
    AppTheme(false){
        SubscriptionView()
    }
}

@Composable
fun SubscriptionView(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BannerCard("Calf Tracker", "Powered by Elliott Software")

        Sublayout()
    }
}


@Composable
fun Sublayout(){
    RoundedCornerShapeDemo()
}

@Composable
fun RoundedCornerShapeDemo(){
    ExampleBox()
}

@Composable
fun ExampleBox(){
    Column(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){

            SubscriptionBox()
            SubscriptionBox()
        }

    }
}

@Composable
fun SubscriptionBox(){

    Box(
        modifier = Modifier.size(125.dp).border(2.dp,MaterialTheme.colors.primary)
    )
}