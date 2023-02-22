package com.elliottsoftware.calftracker.presentation.sharedViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * A banner meant to show a large text and small subtext
 * @param banner The main text shown at 40.sp.
 * @param bannerDescription The subtext shown at 18.sp.
 * @param modifier  a optional [Modifier][androidx.compose.ui.Modifier] for this text field
 */
@Composable
fun BannerCard(
    banner: String,
    bannerDescription:String,
    modifier: Modifier = Modifier
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {

        Text(banner,fontSize = 40.sp,
            fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 0.dp,16.dp,0.dp,0.dp)
        )
        Text(bannerDescription,fontSize = 18.sp, fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,)

    }
}