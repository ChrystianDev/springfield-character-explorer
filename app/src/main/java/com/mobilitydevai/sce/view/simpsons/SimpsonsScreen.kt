/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.sce.view.simpsons

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mobilitydevai.sce.BuildConfig
import com.mobilitydevai.sce.R
import com.mobilitydevai.sce.config.Constants.BASE_URL_IMAGE_LARGE
import com.mobilitydevai.sce.config.Constants.BASE_URL_IMAGE_SHORT
import com.mobilitydevai.sce.ui.theme.shapes
import com.mobilitydevai.sce.view.core.components.CustomBiographyText
import com.mobilitydevai.sce.view.core.components.CustomHomerLoading
import com.mobilitydevai.sce.view.core.components.CustomIconButton
import com.mobilitydevai.sce.view.core.components.CustomItemHomerLoading
import com.mobilitydevai.sce.view.core.components.SpaceV10
import com.mobilitydevai.sce.view.core.components.TitleMainImage
import com.mobilitydevai.sce.view.core.components.rememberTextToSpeech
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs

/** Fixed carousel item width, needed to center items (snap + side padding). */
private val CAROUSEL_ITEM_WIDTH = 90.dp

/** Load the next page when the last visible item is this close to the end of the list. */
private const val LOAD_MORE_THRESHOLD = 3

private val CAROUSEL_DONUT_SIZE = 60.dp
private const val CAROUSEL_DONUT_ALPHA = 0.5f

/**
 * Draws donuts (not clickable, not list items) in the empty side space of the carousel:
 * before the first item and, if [showTrailing], after the last one.
 * Positions are read from [listState] at draw time, so they scroll with the carousel.
 */
private fun Modifier.carouselEdgeDonuts(
    listState: LazyListState,
    painter: Painter,
    showTrailing: Boolean,
): Modifier = drawBehind {
    val layoutInfo = listState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    if (visibleItems.isEmpty()) return@drawBehind

    val cellWidth = CAROUSEL_ITEM_WIDTH.toPx()
    val donutWidth = CAROUSEL_DONUT_SIZE.toPx()
    val intrinsic = painter.intrinsicSize
    val donutHeight = if (intrinsic.width > 0f) donutWidth * intrinsic.height / intrinsic.width else donutWidth
    val top = (size.height - donutHeight) / 2

    // Item offsets are relative to the content start; convert to container coordinates
    fun containerX(offset: Int) = (offset - layoutInfo.viewportStartOffset).toFloat()

    fun drawDonut(cellStart: Float) {
        translate(left = cellStart + (cellWidth - donutWidth) / 2, top = top) {
            with(painter) { draw(Size(donutWidth, donutHeight), alpha = CAROUSEL_DONUT_ALPHA) }
        }
    }

    val first = visibleItems.first()
    if (first.index == 0) {
        var cellStart = containerX(first.offset) - cellWidth
        while (cellStart + cellWidth > 0f) {
            drawDonut(cellStart)
            cellStart -= cellWidth
        }
    }

    val last = visibleItems.last()
    if (showTrailing && last.index == layoutInfo.totalItemsCount - 1) {
        var cellStart = containerX(last.offset + last.size)
        while (cellStart < size.width) {
            drawDonut(cellStart)
            cellStart += cellWidth
        }
    }
}

@Composable
fun SimpsonsScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    darkThemeParam: Boolean,
    onNavigationDetail: () -> Unit,
    onSetDarkMode: (Boolean) -> Unit,
) {

    val uiState by simpsonsViewModel.uiState.collectAsStateWithLifecycle()
    val indexSelected by simpsonsViewModel.indexSelected.collectAsStateWithLifecycle()
    val isMuted by simpsonsViewModel.isMuted.collectAsStateWithLifecycle()
    val isLoading by simpsonsViewModel.isLoading.collectAsStateWithLifecycle()
    val isLoadingMore by simpsonsViewModel.isLoadingMore.collectAsStateWithLifecycle()

    var isDarkThemeMode by remember { mutableStateOf(darkThemeParam) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val analyticsManager = remember {
        (context.applicationContext as? com.mobilitydevai.sce.SimpsonsInfoApp)?.analyticsManager
    }

    val listState = rememberLazyListState()
    val tts = rememberTextToSpeech()
    val scope = rememberCoroutineScope()

    // Carousel item closest to the center of the carousel (-1 if there are no items)
    val centeredIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo
                .minByOrNull { abs(it.offset + it.size / 2 - viewportCenter) }
                ?.index ?: -1
        }
    }

    // Auto-load the next page before reaching the end, so no empty space appears on the right
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val itemCount = simpsonsViewModel.uiState.value.entity.results.size
            itemCount > 0 && lastVisible >= itemCount - 1 - LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { simpsonsViewModel.loadNextPage() }
    }

    // The selected character is the centered one, updated once the scroll stops
    LaunchedEffect(listState) {
        snapshotFlow { if (listState.isScrollInProgress) -1 else centeredIndex }
            .filter { it >= 0 }
            .distinctUntilChanged()
            .collect { index ->
                if (index != simpsonsViewModel.indexSelected.value) {
                    simpsonsViewModel.setIndexSelected(index)
                    simpsonsViewModel.stopSpeak(tts = tts)
                }
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.entity.results.isEmpty()) {
            if (isLoading) {
                CustomHomerLoading()
            } else {
                SimpsonsEmptyListScreen(
                    onGetCharacters = simpsonsViewModel::onGetCharacters
                )
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
                        onClick = {
                            simpsonsViewModel.toggleMute(tts = tts)
                        }
                    ) {
                        Icon(
                            painterResource(if (isMuted) R.drawable.icon_volume_disabled else R.drawable.icon_volume_active),
                            contentDescription = "enable/disable sound",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    CustomIconButton(
                        icon = if (isDarkThemeMode) R.drawable.icon_light_mode else R.drawable.icon_dark_mode,
                        iconDescription = "Set ThemeMode, Dark or Light",
                        onClick = {
                            isDarkThemeMode = !isDarkThemeMode
                            onSetDarkMode(isDarkThemeMode)
                            // Log theme change
                            analyticsManager?.logThemeChange(isDarkThemeMode)
                        }
                    )
                }
                SpaceV10()
                BoxWithConstraints(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(0.1f))
                        .padding(vertical = 5.dp)
                ) {
                    // Side padding so the first and last items can also be centered
                    val donutPainter = painterResource(R.drawable.simpsons_donut)
                    val sidePadding = ((maxWidth - CAROUSEL_ITEM_WIDTH) / 2).coerceAtLeast(0.dp)
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        // Decorative donuts in the empty space before the first character
                        // and after the last one (only when there are no more pages)
                        modifier = Modifier.carouselEdgeDonuts(
                            listState = listState,
                            painter = donutPainter,
                            showTrailing = !isLoading && !isLoadingMore &&
                                uiState.entity.next.isEmpty(),
                        ),
                        state = listState,
                        contentPadding = PaddingValues(horizontal = sidePadding),
                        // When the scroll stops, an item always ends up centered
                        flingBehavior = rememberSnapFlingBehavior(
                            lazyListState = listState,
                            snapPosition = SnapPosition.Center
                        ),
                    ) {
                        if (isLoading) {
                            items(10) { index ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.width(CAROUSEL_ITEM_WIDTH)
                                ) {
                                    CustomItemHomerLoading(indexSelected = index == centeredIndex)
                                }
                            }
                        } else {
                            items(uiState.entity.results.size) { index ->
                                // Only the centered item grows
                                val isCentered = index == centeredIndex
                                val itemHeight by animateDpAsState(
                                    targetValue = if (isCentered) 100.dp else 90.dp,
                                    label = "carouselItemHeight"
                                )
                                val itemScale by animateFloatAsState(
                                    targetValue = if (isCentered) 1.5f else 1f,
                                    label = "carouselItemScale"
                                )
                                AsyncImage(
                                    modifier = Modifier
                                        .width(CAROUSEL_ITEM_WIDTH)
                                        .clip(shape = shapes.large)
                                        .padding(horizontal = 5.dp)
                                        .background(MaterialTheme.colorScheme.background)
                                        .height(itemHeight)
                                        .padding(vertical = 10.dp, horizontal = 5.dp)
                                        .scale(itemScale)
                                        .clickable(onClick = {
                                            // Bring the tapped item to the center
                                            scope.launch { listState.animateScrollToItem(index) }
                                            simpsonsViewModel.setIndexSelected(index)
                                            simpsonsViewModel.speakItemCharacters(index, tts)
                                        }),
                                    model = "$BASE_URL_IMAGE_SHORT${uiState.entity.results[index].portrait_path}",
                                    contentDescription = "image webp"
                                )
                            }
                            // Placeholder at the end while the next page loads
                            if (isLoadingMore) {
                                item {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.width(CAROUSEL_ITEM_WIDTH)
                                    ) {
                                        CustomItemHomerLoading()
                                    }
                                }
                            }
                        }
                    }
                }
                // ‹ › move one character; the next page is auto-loaded at the end
                val canGoPrevious = indexSelected > 0
                val canGoNext = indexSelected < uiState.entity.results.size - 1
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = canGoPrevious,
                        onClick = {
                            scope.launch { listState.animateScrollToItem(indexSelected - 1) }
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.icon_arrow_backward),
                            contentDescription = "previous character",
                            tint = if (canGoPrevious) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(0.2f)
                        )
                    }
                    IconButton(
                        enabled = canGoNext,
                        onClick = {
                            scope.launch { listState.animateScrollToItem(indexSelected + 1) }
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.icon_arrow_forward),
                            contentDescription = "next character",
                            tint = if (canGoNext) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(0.2f)
                        )
                    }
                }
            }

            if (isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CustomItemHomerLoading(size = 200.dp)
                    SpaceV10()
                    TitleMainImage(stringResource(R.string.main_loading_text))
                }
            } else {
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
                            .padding(10.dp)
                            .clickable(onClick = {
                                onNavigationDetail()
                                simpsonsViewModel.setCharacterSelected(uiState.entity.results[indexSelected])
                            }),
                        model = "$BASE_URL_IMAGE_LARGE${uiState.entity.results[indexSelected].portrait_path}",
                        contentDescription = "image webp"
                    )
                    SpaceV10()
                    TitleMainImage(uiState.entity.results[indexSelected].name)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) {


                    Icon(
                        modifier = Modifier.width(130.dp),
                        painter = painterResource(R.drawable.simpsons_logo),
                        contentDescription = "simpsons",
                        tint = MaterialTheme.colorScheme.primary
                    )


                    // Testing Section - Only visible in DEBUG builds
                    if (BuildConfig.DEBUG) {
                        val crashlyticsManager = remember {
                            (context.applicationContext as? com.mobilitydevai.sce.SimpsonsInfoApp)?.crashlyticsManager
                        }
                        val notificationHelper = remember {
                            (context.applicationContext as? com.mobilitydevai.sce.SimpsonsInfoApp)?.notificationHelper
                        }
                        CustomBiographyText("BuildType: ${BuildConfig.BUILD_TYPE}, Flavor: ${BuildConfig.FLAVOR}")

                        // Crashlytics Testing
                        SpaceV10()
                        Text(
                            text = "Crashlytics Testing",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    crashlyticsManager?.forceCrash()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Force Crash",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }

                            TextButton(
                                onClick = {
                                    crashlyticsManager?.testNonFatalException()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Non-Fatal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    crashlyticsManager?.testCustomError()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Custom Error",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            TextButton(
                                onClick = {
                                    crashlyticsManager?.testBreadcrumbs()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Breadcrumbs",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        // Notifications Testing
                        SpaceV10()
                        Text(
                            text = "Notifications Testing",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    notificationHelper?.showTestNotification()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Test Notification",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            TextButton(
                                onClick = {
                                    val token = notificationHelper?.getToken()
                                    Log.d("SimpsonsScreen", "FCM Token: $token")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Show Token",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
