/**
 * Created by chrystian.lozada.local (10/10/25)
 */
package com.mobilitydevai.simpsonsinfo.view.detail

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mobilitydevai.simpsonsinfo.R
import com.mobilitydevai.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.mobilitydevai.simpsonsinfo.ui.theme.shapes
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomBiographyText
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomBodyText
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomIconButton
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomItemCharacter
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomSnackBar
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomTitleText
import com.mobilitydevai.simpsonsinfo.view.core.components.SpaceV10
import com.mobilitydevai.simpsonsinfo.view.core.components.rememberTextToSpeech
import com.mobilitydevai.simpsonsinfo.view.favorite.FavouriteViewModel
import com.mobilitydevai.simpsonsinfo.view.simpsons.SimpsonsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    favouriteViewModel: FavouriteViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSpeaking by remember { mutableStateOf(false) }
    val tts = rememberTextToSpeech()

    val character by simpsonsViewModel.characterSelected.collectAsStateWithLifecycle()
    val isFavourite by favouriteViewModel.isFavourite.collectAsStateWithLifecycle()

    favouriteViewModel.existsItem(character.id)
    val favouriteItem = character.toFavouriteCharacterEntity()


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    CustomSnackBar(
                        message = if (isFavourite) stringResource(R.string.favourite_character_added) else stringResource(
                            R.string.favourite_character_deleted
                        )
                    )
                }
            )
        },
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
                        model = "$BASE_URL_IMAGE_LARGE${character.portrait_path}",
                        contentDescription = "image webp"
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        CustomBodyText(text = character.name)
                        CustomIconButton(
                            icon = if (isFavourite) R.drawable.icon_favorite_selected else R.drawable.icon_favorite_unselected,
                            iconDescription = "Add to Favourites",
                            onClick = {
                                if (isFavourite) {
                                    favouriteViewModel.onDeleteFavourite(favouriteItem)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Deleted")
                                    }
                                } else {
                                    favouriteViewModel.onSaveFavourite(favouriteItem)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Added")
                                    }
                                }
                            },
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
                        CustomBiographyText(
                            if (character.age != null) "${character.age} ${
                                stringResource(
                                    R.string.character_years
                                )
                            }" else "-"
                        )

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
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    items(character.phrases.size) { index ->
                        val phrase = character.phrases[index]
                        CustomItemCharacter(
                            text = phrase,
                            isLastItem = character.phrases.size == index + 1,
                            isPlaying = isSpeaking,
                            onClick = {
                                simpsonsViewModel.speakItemDetail(
                                    tts = tts,
                                    phrase,
                                )
                            }
                        )
                    }
                }

            }
        }
    }
}
