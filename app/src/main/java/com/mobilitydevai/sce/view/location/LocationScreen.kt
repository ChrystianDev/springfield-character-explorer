package com.mobilitydevai.sce.view.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mobilitydevai.sce.R
import com.mobilitydevai.sce.config.Constants.BASE_URL_IMAGE_LARGE
import com.mobilitydevai.sce.view.core.components.CustomBodyText
import com.mobilitydevai.sce.view.core.components.CustomHomerLoading
import com.mobilitydevai.sce.view.core.components.CustomLoadingStyleIOS
import com.mobilitydevai.sce.view.core.components.SpaceV20

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationScreen(
    locationsViewModel: LocationsViewModel = hiltViewModel(),
) {
    val uiState by locationsViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by locationsViewModel.isLoading.collectAsStateWithLifecycle()
    val likesList by locationsViewModel.likesList.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.canScrollForward
        }.collect { canScrollForward ->
            if (!canScrollForward) {
                if (uiState.entity.results.isNotEmpty() && !isLoading) {
                    locationsViewModel.incrementPage()
                    locationsViewModel.onFetchLocations()
                }
            }
        }
    }


    if (uiState.entity.results.isEmpty() && isLoading) {
        CustomHomerLoading(background = MaterialTheme.colorScheme.onTertiary)
    } else {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onTertiary)
                .padding(vertical = 3.dp)
                .padding(horizontal = 10.dp),
        ) {
            items(
                uiState.entity.results.size
            ) { index ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        model = "$BASE_URL_IMAGE_LARGE${uiState.entity.results[index].imagePath}",
                        contentDescription = "image webp",
                        contentScale = ContentScale.FillWidth,

                        )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            modifier = Modifier.weight(2f),
                            enabled = false,
                            onClick = {}) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = uiState.entity.results[index].name,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        ) {
                            CustomBodyText(
                                text = "${likesList.getOrNull(index)?.likeCount ?: 0}",
                                padding = 0.dp,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(
                                onClick = {
                                    locationsViewModel.toggleLike(index)
                                }) {
                                Icon(
                                    painter = painterResource(
                                        if (likesList.getOrNull(index)?.isLiked == true) R.drawable.icon_thumb_up_selected
                                        else R.drawable.icon_thumb_up_unselected
                                    ),
                                    contentDescription = "like button",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                SpaceV20()
            }
            if (isLoading)
                item {
                    CustomLoadingStyleIOS(padding = 30.dp)
                }
        }
    }
}
