package com.example.simpsonsinfo.view.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_SHORT
import com.example.simpsonsinfo.view.core.components.CustomBiographyText
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV20

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationScreen(
    locationsViewModel: LocationsViewModel = hiltViewModel()
) {
    val uiState by locationsViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by locationsViewModel.isLoading.collectAsStateWithLifecycle()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))

    if(isLoading){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painterResource(R.drawable.homer_thinking),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            LottieAnimation(
                modifier = Modifier
                    .height(50.dp)
                    .offset(50.dp, -100.dp),
                composition = composition,
                speed = 1f,
                reverseOnRepeat = false,
                iterations = LottieConstants.IterateForever,
            )
        }
    }else {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onTertiary)
                .padding(top = 40.dp)
                .padding(bottom = 2.dp)
                .padding(horizontal = 20.dp),
        ) {
            items(uiState.entity.results.size) { index ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        model = "$BASE_URL_IMAGE_SHORT${uiState.entity.results[index].imagePath}",
                        contentDescription = "image webp",
                        contentScale = ContentScale.FillWidth,

                        )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            enabled = false,
                            onClick = {}) {
                            Text(
                                uiState.entity.results[index].use,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            onClick = {}
                        ) {
                            Icon(
                                painterResource(R.drawable.icon_thumb_up_unselected),
                                contentDescription = "enable/disable sound",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    SpaceV10()
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        ),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    SpaceV10()
                    CustomBiographyText(
                        text = uiState.entity.results[index].name,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    SpaceV10()
                }
                SpaceV20()
            }
        }
    }
}