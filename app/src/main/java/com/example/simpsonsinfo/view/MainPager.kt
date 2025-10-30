/**
 * Created by chrystian.lozada.local (10/10/25)
 */
package com.example.simpsonsinfo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.simpsonsinfo.view.core.components.IOSStyleSegmentedControl
import com.example.simpsonsinfo.view.favorite.FavouriteScreen
import com.example.simpsonsinfo.view.location.LocationScreen
import com.example.simpsonsinfo.view.simpsons.SimpsonsScreen
import com.example.simpsonsinfo.view.simpsons.SimpsonsViewModel
import kotlinx.coroutines.launch

@Composable
fun MainPager(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    darkThemeParam: Boolean,
    onNavigationDetail: () -> Unit,
    onSetDarkMode: (Boolean) -> Unit
) {
    val pageState = rememberPagerState { 3 }
    val options = listOf(0, 1, 2)
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pageState
        ) { page ->
            when (page) {
                0 -> SimpsonsScreen(
                    simpsonsViewModel,
                    darkThemeParam,
                    onNavigationDetail,
                    onSetDarkMode,
                )

                1 -> LocationScreen()
                2 -> FavouriteScreen(
                    simpsonsViewModel = simpsonsViewModel,
                    onNavigationDetail = onNavigationDetail
                )
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
            )
        }
    }
}
