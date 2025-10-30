package com.example.simpsonsinfo.view.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.example.simpsonsinfo.ui.theme.BlackDev
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.CustomSubtitleText
import com.example.simpsonsinfo.view.simpsons.SimpsonsViewModel

@Composable
fun FavouriteScreen(
    favouriteViewModel: FavouriteViewModel = hiltViewModel(),
    simpsonsViewModel: SimpsonsViewModel,
    onNavigationDetail: () -> Unit,
) {
    val gridState = rememberLazyGridState(0)

    val uiState = favouriteViewModel.uiState.collectAsStateWithLifecycle(initialValue = emptyList())

    if (uiState.value.isEmpty()) {
        FavouriteEmptyListScreen()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onTertiary)
                .padding(horizontal = 10.dp)
                .padding(top = 40.dp, bottom = 2.dp),
        ) {
            items(uiState.value.size) { index ->
                val item = uiState.value[index]
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .animateItem(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shapes.large)
                                .clickable {
                                    onNavigationDetail()
                                    simpsonsViewModel.setCharacterSelected(item.toCharacterEntity())
                                },
                            model = "$BASE_URL_IMAGE_LARGE${item.urlImage}",
                            contentDescription = "image webp"
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BlackDev.copy(alpha = 0.5f))
                                .padding(10.dp)
                                .clickable(
                                    onClick = {
                                        favouriteViewModel.onDeleteFavourite(
                                            favouriteCharacter = item
                                        )
                                    }
                                )
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.surface,
                                painter = painterResource(R.drawable.icon_delete),
                                contentDescription = "Delete from Favourites",
                            )
                        }
                    }
                    CustomSubtitleText(
                        text = item.name,
                    )
                }
            }
        }
    }
}