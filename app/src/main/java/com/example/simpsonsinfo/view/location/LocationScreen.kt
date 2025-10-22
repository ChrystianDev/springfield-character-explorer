package com.example.simpsonsinfo.view.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_SHORT
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.CustomHomerLoading
import com.example.simpsonsinfo.view.core.components.CustomLoadingStyleIOS
import com.example.simpsonsinfo.view.core.components.SpaceV20

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationScreen(
    locationsViewModel: LocationsViewModel = hiltViewModel()
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
                .padding(top = 40.dp)
                .padding(bottom = 2.dp)
                .padding(horizontal = 20.dp),
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
                        model = "$BASE_URL_IMAGE_SHORT${uiState.entity.results[index].imagePath}",
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
                                text = if (likesList[index].like > 0) "+${likesList[index].like}" else " ${likesList[index].like}",
                                color = if (likesList[index].like >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(
                                onClick = {
                                    locationsViewModel.updateLikes(index, +1)
                                }) {
                                Icon(
                                    painterResource(if (likesList[index].like > 0) R.drawable.icon_thumb_up_selected else R.drawable.icon_thumb_up_unselected),
                                    contentDescription = "enable/disable sound",
                                    tint = if (likesList[index].like > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                            }
                            IconButton(onClick = {
                                locationsViewModel.updateLikes(index, -1)
                            }) {
                                Icon(
                                    painterResource(R.drawable.icon_thumb_down),
                                    contentDescription = "enable/disable sound",
                                    tint = MaterialTheme.colorScheme.onBackground
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