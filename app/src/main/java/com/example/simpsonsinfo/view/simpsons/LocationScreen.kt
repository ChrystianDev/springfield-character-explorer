package com.example.simpsonsinfo.view.simpsons

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_SHORT
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomBiographyText
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.CustomItemCharacter
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV20
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationScreen() {
    val list = listOf(1, 2, 3, 4, 5)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
    ) {
        items(list.size) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(shape = shapes.large)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 10.dp, horizontal = 5.dp),

                    model = "$BASE_URL_IMAGE_SHORT/location/1.webp",
                    contentDescription = "image webp"
                )

            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    enabled = false,
                    onClick = {}) {
                    Text(
                        "Location: Springfield",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = {}
                ) {
                    Icon(
                        painterResource(R.drawable.icon_thumb_up_unselected),
                        contentDescription = "enable/disable sound",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
            SpaceV10()
            CustomBiographyText("dslfslfjsflsd lfjdflsfjdsl fslfkdjflsf lsfj dflsjf slfd fslfjds fldsf sdlfjslfdsjf ldsfj dslf jlf jsfldskfj fls j")
            if (list.size != it + 1) {
                SpaceV20()
                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    ),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
                SpaceV20()
            }
        }
    }
}