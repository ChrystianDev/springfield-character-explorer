/**
 * Created by chrystian.lozada.local (10/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomBiographyText
import com.example.simpsonsinfo.view.core.components.CustomIconButton
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.CustomItemCharacter
import com.example.simpsonsinfo.view.core.components.CustomTitleText
import com.example.simpsonsinfo.view.core.components.SpaceV10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit,
) {

    val uiState by simpsonsViewModel.uiState.collectAsStateWithLifecycle()
    val indexSelected by simpsonsViewModel.indexSelected.collectAsStateWithLifecycle()
    val isLoading by simpsonsViewModel.isLoading.collectAsStateWithLifecycle()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_gray))
    val scope = rememberCoroutineScope()

    val character = uiState.entity.results[indexSelected]

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    CustomIconButton(

                        icon = R.drawable.icon_back,
                        iconDescription = "Back Navigation",
                        onClick = {
                            onBackNavigation()
                        }
                    )
                },
                title = {
                    CustomTitleText(text = "Details")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                )
            )
        },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onTertiary)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),

            ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.large)
                            .padding(10.dp),
                        model = "$BASE_URL_IMAGE_LARGE${uiState.entity.results[indexSelected].portrait_path}",
                        contentDescription = "image webp"
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
//                        .padding(horizontal = 5.dp)
                    ) {
                        CustomBodyText(text = character.name)
                        CustomIconButton(
                            icon = R.drawable.icon_favorite,
                            iconDescription = "Add to Favourites",
                            onClick = {},
                        )
                    }
                }

            }
            SpaceV10()
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        CustomBiographyText(character.occupation ?: "-")
                        CustomBiographyText(character.status)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        CustomBiographyText(if (character.age != null) "${character.age} ${stringResource(R.string.character_years)}" else "-")

                        CustomBiographyText(character.gender)
                        CustomBiographyText(character.birthdate ?: "-")
                    }

                }

            }
            SpaceV10()
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                LazyColumn (modifier = Modifier.padding(10.dp)){
                    items(character.phrases.size) { index ->
                        CustomItemCharacter(
                            text =  character.phrases[index],
                            isLastItem = character.phrases.size == index + 1,
                            onClick = { }
                        )
                    }
                }

            }
        }
    }
}