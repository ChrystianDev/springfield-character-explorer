/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.view.episode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mobilitydevai.sce.config.Constants
import com.mobilitydevai.sce.ui.theme.shapes
import com.mobilitydevai.sce.view.core.components.CustomHomerLoading

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EpisodesScreen(
    episodesViewModel: EpisodesViewModel = hiltViewModel()
) {
    val uiState by episodesViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by episodesViewModel.isLoading.collectAsStateWithLifecycle()
    val selectedEpisodeIndex by episodesViewModel.selectedEpisodeIndex.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        }.collect { visibleItems ->
            if (visibleItems > 0) {
                val centerIndex = listState.firstVisibleItemIndex
                episodesViewModel.setSelectedEpisodeIndex(centerIndex)
            }
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // LazyRow for episodes
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                        }
                    )
                }
            }
        }
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
            .width(300.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            // Season and Airdate
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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
