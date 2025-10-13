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
import coil3.compose.AsyncImage
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomBodyText

@Composable
fun FavouritesScreen() {

    val gridState = rememberLazyGridState(0)

    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onTertiary)
            .padding(horizontal = 10.dp)
            .padding(top = 40.dp, bottom = 2.dp),
    ) {
        items(list.size) {
            Card(
                modifier = Modifier.padding(5.dp),
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
                            .clip(shapes.large),
//                            .background(MaterialTheme.colorScheme.onTertiary),
                        model = "$BASE_URL_IMAGE_LARGE/character/1.webp",
                        contentDescription = "image webp"
                    )
                    Icon(
                        modifier = Modifier
                            .clickable(onClick = {})
                            .padding(5.dp),
                        painter = painterResource(R.drawable.icon_delete),
                        contentDescription = "Delete from Favourites"
                    )
                }
                CustomBodyText(
                    text = "Homer ***",
                )
            }
        }
    }
}