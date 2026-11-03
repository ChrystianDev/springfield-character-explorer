/**
 * Created by chrystian.lozada.local (14/10/25)
 */
package com.mobilitydevai.sce.view.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobilitydevai.sce.R

@Composable
fun CustomHomerLoading(background: Color = MaterialTheme.colorScheme.background) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Icon(
            painterResource( R.drawable.homer_thinking),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        LottieAnimation(
            modifier = Modifier
                .height(100.dp),
            composition = composition,
            speed = 1f,
            reverseOnRepeat = false,
            iterations = LottieConstants.IterateForever,
        )
    }
}