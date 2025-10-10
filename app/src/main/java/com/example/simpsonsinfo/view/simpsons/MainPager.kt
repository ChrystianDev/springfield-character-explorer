/**
 * Created by chrystian.lozada.local (10/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.view.core.components.IOSStyleSegmentedControl
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV20
import com.example.simpsonsinfo.view.core.components.SpaceV40
import kotlinx.coroutines.launch

@Composable
fun MainPager(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    onNavigationDetail: () -> Unit
) {
    val isLoading by simpsonsViewModel.isLoading.collectAsStateWithLifecycle()
    val pageState = rememberPagerState { 3 }
    var selected by remember { mutableIntStateOf(R.drawable.icon_home) }
//    val options = listOf(R.drawable.icon_home, R.drawable.icon_location, R.drawable.icon_favorite)
    val options = listOf(0,1,2)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
                    modifier = Modifier
                        .height(50.dp)
                        .offset(50.dp, -100.dp),
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
            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pageState
            ) { page ->
                when (page) {
                    0 -> SimpsonsScreen(
                        simpsonsViewModel,
                        onNavigationDetail,
                    )

                    1 -> LocationScreen()
                    2 -> FavouritesScreen()
                }
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 30.dp)
            ) {
                IOSStyleSegmentedControl(
                    options,
                    selectedOption = pageState.currentPage,
                    onOptionSelected = { it ->
                        scope.launch {
                            pageState.animateScrollToPage(it)
                        }
                    },
//                    onNavigationDetail = {
//                        //TODO cambiar la navegación de pantallas
//                        onNavigationDetail()
//                    }
                )
            }
        }
    }
}
//
//@Composable
//fun PageContent(index: Int) {
//    when{
//        index == 0 -> SimpsonsScreen()
//        index == 1 -> LocationScreen()
//        index == 2 -> FavouritesScreen()
//    }
//}