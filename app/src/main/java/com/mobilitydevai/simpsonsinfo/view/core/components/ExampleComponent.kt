/**
 * Created by chrystian.lozada.local (29/10/25)
 */
package com.mobilitydevai.simpsonsinfo.view.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobilitydevai.simpsonsinfo.R

@Preview(showBackground = true, showSystemUi = true)

@Composable
fun ExampleComponent() {

    val gridState = rememberLazyGridState(0)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onTertiary)
            .padding(horizontal = 10.dp)
            .padding(top = 40.dp, bottom = 2.dp),
    ) {
        items(10) { index ->
            Card(
                modifier = Modifier
                    .animateItem(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                )
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Image(
                        painter = painterResource(R.drawable.homer_thinking),
                        contentDescription = null

                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    CustomBodyText(
                        text = "Character",
                        textAlign = TextAlign.Start,

                        )
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable(
                                onClick = {

                                }
                            )
                    ) {
                        Icon(
                            tint = Color.Blue,
                            painter = painterResource(R.drawable.icon_delete),
                            contentDescription = "Delete from Favourites",
                        )
                    }
                }
            }
        }
    }
}