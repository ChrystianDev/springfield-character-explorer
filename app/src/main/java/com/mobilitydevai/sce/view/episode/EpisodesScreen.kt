/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.view.episode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mobilitydevai.sce.config.Constants
import com.mobilitydevai.sce.ui.theme.DarkBackground
import com.mobilitydevai.sce.ui.theme.YellowDarkDev
import com.mobilitydevai.sce.ui.theme.shapes
import com.mobilitydevai.sce.view.core.components.CustomHomerLoading
import com.mobilitydevai.sce.view.core.components.CustomLoadingStyleIOS
import com.mobilitydevai.sce.view.core.components.SpaceV10
import com.mobilitydevai.sce.view.core.components.SpaceV20
import com.mobilitydevai.sce.view.core.components.SpaceVAny
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EpisodesScreen(
    episodesViewModel: EpisodesViewModel = hiltViewModel()
) {
    val uiState by episodesViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by episodesViewModel.isLoading.collectAsStateWithLifecycle()
    val selectedEpisodeIndex by episodesViewModel.selectedEpisodeIndex.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = 350.dp
    val cardSpacing = 16.dp
    val sidePadding = (screenWidth - cardWidth) / 2

    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Calculate the centered item based on scroll position
    val centeredItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.width / 2

            layoutInfo.visibleItemsInfo
                .minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }?.index ?: 0
        }
    }

    LaunchedEffect(centeredItemIndex) {
        if (centeredItemIndex < uiState.entity.results.size) {
            episodesViewModel.setSelectedEpisodeIndex(centeredItemIndex)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.canScrollForward
        }.collect { canScrollForward ->
            if (!canScrollForward && uiState.entity.results.isNotEmpty() && !isLoading) {
                episodesViewModel.loadNextPage()
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (uiState.entity.results.isEmpty() && isLoading) {
            CustomHomerLoading()
        } else {
            // Episode Name at the top (dynamic content)
            Text(
                text = if (uiState.entity.results.isNotEmpty() && selectedEpisodeIndex < uiState.entity.results.size) {
                    uiState.entity.results[selectedEpisodeIndex].name
                } else {
                    "Episodes"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 50.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            SpaceVAny(50.dp)

            // LazyRow for episodes
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(cardSpacing),
                contentPadding = PaddingValues(horizontal = sidePadding),
                flingBehavior = snapFlingBehavior,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.entity.results.size) { index ->
                    EpisodeCard(
                        imageUrl = "${Constants.BASE_URL_IMAGE_LARGE}${uiState.entity.results[index].image_path}",
                        synopsis = uiState.entity.results[index].synopsis,
                        season = uiState.entity.results[index].season,
                        airdate = uiState.entity.results[index].airdate,
                        isSelected = index == selectedEpisodeIndex,
                        onClick = {
                            episodesViewModel.setSelectedEpisodeIndex(index)
                            scope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    )
                }
                if (isLoading)
                    item {
                        CustomLoadingStyleIOS(padding = 30.dp)
                    }
            }

            if (uiState.seasons.isNotEmpty()) {
                SpaceV20()
                // Seasons
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.seasons, key = { it.number }) { season ->
                        OutlinedLabel(
                            text = "Season %02d".format(season.number),
                            isSelected = season.number == uiState.selectedSeason,
                            onClick = { episodesViewModel.setSelectedSeason(season.number) }
                        )
                    }
                }
                SpaceV10()
                // Chapters of the selected season
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.selectedSeasonEpisodeCount) { index ->
                        val chapter = index + 1
                        OutlinedLabel(
                            text = "Chapter %02d".format(chapter),
                            isSelected = chapter == uiState.selectedChapter,
                            onClick = { episodesViewModel.setSelectedChapter(chapter) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedLabel(
    text: String,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(shapes.small)
            .background(if (isSelected) YellowDarkDev else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) YellowDarkDev else MaterialTheme.colorScheme.primary,
                shape = shapes.small
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) DarkBackground else MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EpisodeCard(
    imageUrl: String,
    synopsis: String,
    season: Int,
    airdate: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Episode Image
            AsyncImage(
                model = imageUrl,
                contentDescription = "Episode image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Synopsis
            Text(
                text = synopsis,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            SpaceV20()
            // Season and Airdate
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Season $season",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = airdate.ifEmpty { "N/A" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
