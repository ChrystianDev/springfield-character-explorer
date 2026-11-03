package com.mobilitydevai.sce.view.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobilitydevai.sce.R

@Stable
@Composable
fun CustomLoadingStyleIOS(padding: Dp = 0.dp) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = padding)
    ) {
        LottieAnimation(
            composition = composition,
            speed = 1f,
            reverseOnRepeat = false,
            iterations = LottieConstants.IterateForever,
        )
    }
}