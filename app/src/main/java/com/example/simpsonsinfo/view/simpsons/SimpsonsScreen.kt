/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_SHORT
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.CustomIconButton
import com.example.simpsonsinfo.view.core.components.SpaceH10
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV20
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpsonsScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    onNavigationDetail: () -> Unit,
    onSetDarkMode: (Boolean) -> Unit,
) {

    val uiState by simpsonsViewModel.uiState.collectAsStateWithLifecycle()
    val carouselState = rememberCarouselState(initialItem = 0) { uiState.entity.results.size }
    val scope = rememberCoroutineScope()
    var isDarkThemeMode by remember { mutableStateOf(true) }

//    LaunchedEffect(carouselState.currentItem) {
//        if(carouselState.currentItem == 15){
//            Log.i("CAROUSEL_STATE", "ITEM ENCONTRADO")
//            simpsonsViewModel.onGetSimpsons()
//        }
//    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.entity.results.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(painterResource(R.drawable.doh_homer_simpson), contentDescription = null)
                SpaceV20()
                Text(
                    text = "opssss ...",
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                SpaceV10()
                CustomBodyText("No se obtuvieron resultados, intentelos de nuevo__")

                TextButton(
                    enabled = true,
                    onClick = {
//                        simpsonsViewModel.onGetSimpsons()
                    }) {
                    CustomBodyText(
                        stringResource(R.string.main_get_characters),
                        color = MaterialTheme.colorScheme.primary
                    )
                    SpaceH10()
                    Icon(
                        painterResource(R.drawable.icon_download),
                        contentDescription = "Download characters",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Column {
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
                    CustomIconButton(
                        icon = if (isDarkThemeMode) R.drawable.icon_light_mode else R.drawable.icon_dark_mode,
                        iconDescription = "Set ThemeMode, Dark or Light",
                        onClick = {
                            isDarkThemeMode = !isDarkThemeMode
                            onSetDarkMode(isDarkThemeMode)
                        }
                    )
                }
                SpaceV10()
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
                                .clip(shape = shapes.small)
                                .background(MaterialTheme.colorScheme.background)
                                .maskClip(MaterialTheme.shapes.extraLarge)
                                .padding(vertical = 10.dp, horizontal = 5.dp)
                                .clickable(onClick = {
                                    scope.launch(Dispatchers.Main) {
                                        carouselState.animateScrollToItem(index)
                                    }
                                }),
                            model = "$BASE_URL_IMAGE_SHORT${uiState.entity.results[index].portrait_path}",
                            contentDescription = "image webp"
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = uiState.entity.prev.isNotEmpty(),
                        onClick = {
                            simpsonsViewModel.downPage()
                            simpsonsViewModel.onGetSimpsons()
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.icon_arrow_backward),
                            contentDescription = "forward",
                            tint = if (uiState.entity.prev.isNotEmpty())
                                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                0.2f
                            )
                        )
                    }
                    IconButton(
                        enabled = uiState.entity.next.isNotEmpty(),
                        onClick = {
                            simpsonsViewModel.upPage()
                            simpsonsViewModel.onGetSimpsons()
                        }) {
                        Icon(
                            painterResource(R.drawable.icon_arrow_forward),
                            contentDescription = "forward",
                            tint = if (uiState.entity.next.isNotEmpty())
                                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                0.5f
                            )
                        )
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
                        .padding(10.dp)
                        .clickable(onClick = {
                            onNavigationDetail()
                            simpsonsViewModel.setIndexSelected(index = carouselState.currentItem)

                        }),
                    model = "$BASE_URL_IMAGE_LARGE${uiState.entity.results[carouselState.currentItem].portrait_path}",
                    contentDescription = "image webp"
                )
                SpaceV10()
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = uiState.entity.results[carouselState.currentItem].name,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }


            Icon(
                modifier = Modifier.width(130.dp),
                painter = painterResource(R.drawable.simpsons_logo),
                contentDescription = "simpsons",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


