package com.mobilitydevai.sce.view.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobilitydevai.sce.R
import com.mobilitydevai.sce.ui.theme.shapes

@Composable
fun CustomItemHomerLoading(indexSelected: Boolean = false, size: Dp = 100.dp) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))
    Box(contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .clip(shape = shapes.large)
                .padding(horizontal = 5.dp)
                .background(MaterialTheme.colorScheme.background)
                .height(if (indexSelected) size else size - 10.dp)
                .padding(vertical = 10.dp, horizontal = 5.dp),
            painter = painterResource(R.drawable.homer_thinking),
            contentDescription = "image webp"
        )
        LottieAnimation(
            modifier = Modifier
                .size(50.dp),
            composition = composition,
            speed = 1f,
            reverseOnRepeat = false,
            iterations = LottieConstants.IterateForever,
        )
    }
}