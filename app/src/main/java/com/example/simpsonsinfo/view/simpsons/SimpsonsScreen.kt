/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.ui.theme.BlackDev
import com.example.simpsonsinfo.ui.theme.ImageBackground
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomText
import com.example.simpsonsinfo.view.core.components.SpaceH10
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpsonsScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel()
) {

    val uiState by simpsonsViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by simpsonsViewModel.isLoading.collectAsStateWithLifecycle()
//    val carouselItems = rememberCarouselState { uiState.entity.results.size }
    val carouselState = rememberCarouselState(initialItem = 0) { uiState.entity.results.size }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp)
            ) {

                Icon(
                    painterResource(R.drawable.homer_thinking),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                LottieAnimation(
                    modifier = Modifier.height(50.dp).offset(50.dp, -100.dp),
                    composition = composition,
                    speed = 1f,
                    reverseOnRepeat = false,
//            iterations = 3,
                    iterations = LottieConstants.IterateForever,
//            safeMode = true,
//            restartOnPlay = false
                )
            }
//            CircularProgressIndicator(
//                modifier = Modifier.size(100.dp),
//                color = Color.Yellow,
//                strokeWidth = 1.dp
//            )
        } else {
            SpaceV40()
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(0.1f))
                        .padding(vertical = 5.dp)
                ) {
                    HorizontalMultiBrowseCarousel(
                        state = carouselState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
//                        .size(150.dp),
                        preferredItemWidth = 100.dp,
                        itemSpacing = 5.dp,
                        contentPadding = PaddingValues(horizontal = 3.dp),
                        flingBehavior = CarouselDefaults.singleAdvanceFlingBehavior(
                            state = carouselState,
                            snapAnimationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    ) { index ->
                        AsyncImage(
                            modifier = Modifier
//                            .clip(shapes.medium)
                                .clip(shape = shapes.medium)
                                .background(MaterialTheme.colorScheme.background)
                                .maskClip(MaterialTheme.shapes.extraLarge)
                                .padding(vertical = 10.dp, horizontal = 5.dp),
                            model = "https://cdn.thesimpsonsapi.com/200${uiState.entity.results[index].portrait_path}",
                            contentDescription = "image webp"
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = {}
                    ) {
                        Icon(
                            painterResource(R.drawable.icon_volume_disabled),
                            contentDescription = "enable/disable sound",
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    Text("Name: ${carouselState.currentItem}")
                    TextButton(onClick = {}) {
                        Text("more", color = MaterialTheme.colorScheme.onTertiary)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
//                        .background(BlackDev)
                        .padding(10.dp),
                    model = "https://cdn.thesimpsonsapi.com/500${uiState.entity.results[carouselState.currentItem].portrait_path}",
                    contentDescription = "image webp"
                )
                SpaceV10()
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = uiState.entity.results[carouselState.currentItem].name,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
//                Text(text = uiState.entity.results[0].name, color = MaterialTheme.colorScheme.primary)
            }
            TextButton(
                enabled = false,
                onClick = {
                    simpsonsViewModel.onGetSimpsons()
                }) {
                CustomText("Get Characters", color = MaterialTheme.colorScheme.primary.copy(0.2f))
                SpaceH10()
                Icon(
                    painterResource(R.drawable.icon_download),
                    contentDescription = "Download characters",
                    tint = MaterialTheme.colorScheme.primary.copy(0.2f)
                )
            }
            Icon(
                modifier = Modifier.width(130.dp),
                painter = painterResource(R.drawable.simpsons_logo),
                contentDescription = "simpsons",
                tint = MaterialTheme.colorScheme.primary
            )
            SpaceV40()
        }
    }
}


