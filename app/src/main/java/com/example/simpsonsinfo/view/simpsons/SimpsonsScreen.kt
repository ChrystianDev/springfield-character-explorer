/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_LARGE
import com.example.simpsonsinfo.config.Constants.BASE_URL_IMAGE_SHORT
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.CustomIconButton
import com.example.simpsonsinfo.view.core.components.SpaceH10
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV20
import com.example.simpsonsinfo.view.core.components.rememberTextToSpeech

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpsonsScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel(),
    darkThemeParam: Boolean,
    onNavigationDetail: () -> Unit,
    onSetDarkMode: (Boolean) -> Unit,
) {

    val uiState by simpsonsViewModel.uiState.collectAsStateWithLifecycle()
    val indexSelected by simpsonsViewModel.indexSelected.collectAsStateWithLifecycle()

    var isDarkThemeMode by remember { mutableStateOf(darkThemeParam) }
    var lastVisibleItem by remember { mutableIntStateOf(0) }
    var isSpeaking by remember { mutableStateOf(false) } // TODO: requiere un listener tts
    var isMuted by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val tts = rememberTextToSpeech()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        }.collect { visibleItems ->
            if (visibleItems > 3) {
                lastVisibleItem = visibleItems - 3
            }
            simpsonsViewModel.setIndexSelected(lastVisibleItem)
            if (uiState.entity.results[lastVisibleItem].phrases.isNotEmpty()) {
                var text = "There are no phrases"
                if (uiState.entity.results[lastVisibleItem].phrases.isNotEmpty()) {
                    text = uiState.entity.results[lastVisibleItem].phrases[0]
                }
                val parameters = Bundle().apply {
                    putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, if (isMuted) 0.0f else 1.0f)
                }
                tts.value?.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    parameters,
                    ""
                )
            }
            val totalItems = listState.layoutInfo.totalItemsCount
            if (visibleItems >= totalItems && totalItems > 0) {
                Log.i("LIST-STATE", "End of list reached")
            }
        }
    }

    fun textToSpeech(index: Int) {
        var text = "There are no phrases"
        if (uiState.entity.results[index].phrases.isNotEmpty()) {
            text = uiState.entity.results[index].phrases[0]
        }
        val parameters = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, if (isMuted) 0.0f else 1.0f)
        }
        tts.value?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            parameters,
            "tts_simpsons"
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.entity.results.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(painterResource(R.drawable.doh_homer_simpson), contentDescription = null)
                SpaceV20()
                Text(
                    text = "opssss ...",
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                SpaceV10()
                CustomBodyText("No se obtuvieron resultados, intentelos de nuevo__")

                TextButton(
                    enabled = true,
                    onClick = {
                        simpsonsViewModel.onGetSimpsons()
                    }) {
                    CustomBodyText(
                        stringResource(R.string.main_get_characters),
                        color = MaterialTheme.colorScheme.primary
                    )
                    SpaceH10()
                    Icon(
                        painterResource(R.drawable.icon_download),
                        contentDescription = "Download characters",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = {
                            isMuted = !isMuted
                        }
                    ) {
                        Icon(
                            painterResource(if (isMuted) R.drawable.icon_volume_disabled else R.drawable.icon_volume_active),
                            contentDescription = "enable/disable sound",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    CustomIconButton(
                        icon = if (isDarkThemeMode) R.drawable.icon_light_mode else R.drawable.icon_dark_mode,
                        iconDescription = "Set ThemeMode, Dark or Light",
                        onClick = {
                            isDarkThemeMode = !isDarkThemeMode
                            onSetDarkMode(isDarkThemeMode)
                        }
                    )
                }
                SpaceV10()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(0.1f))
                        .padding(vertical = 5.dp)
                ) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier,
                        state = listState,

                        ) {
                        items(uiState.entity.results.size) { index ->
                            AsyncImage(
                                modifier = Modifier
                                    .clip(shape = shapes.large)
                                    .padding(horizontal = 5.dp)
                                    .background(MaterialTheme.colorScheme.background)
                                    .height(if (index == indexSelected) 100.dp else 90.dp)
                                    .padding(vertical = 10.dp, horizontal = 5.dp)
                                    .scale(
                                        scaleX = if (index == indexSelected) 1.5f else 1f,
                                        scaleY = if (index == indexSelected) 1.5f else 1f
                                    )
                                    .clickable(onClick = {
                                        simpsonsViewModel.setIndexSelected(index)
                                        textToSpeech(index)
                                    }),
                                model = "$BASE_URL_IMAGE_SHORT${uiState.entity.results[index].portrait_path}",
                                contentDescription = "image webp"
                            )
                        }

                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = uiState.entity.prev.isNotEmpty(),
                        onClick = {
                            simpsonsViewModel.downPage()
                            simpsonsViewModel.onGetSimpsons()
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.icon_arrow_backward),
                            contentDescription = "forward",
                            tint = if (uiState.entity.prev.isNotEmpty())
                                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                0.2f
                            )
                        )
                    }
                    IconButton(
                        enabled = uiState.entity.next.isNotEmpty(),
                        onClick = {
                            simpsonsViewModel.upPage()
                            simpsonsViewModel.onGetSimpsons()
                        }) {
                        Icon(
                            painterResource(R.drawable.icon_arrow_forward),
                            contentDescription = "forward",
                            tint = if (uiState.entity.next.isNotEmpty())
                                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                0.5f
                            )
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .padding(10.dp)
                        .clickable(onClick = {
                            onNavigationDetail()
                            simpsonsViewModel.setIndexSelected(index = indexSelected)

                        }),
                    model = "$BASE_URL_IMAGE_LARGE${uiState.entity.results[indexSelected].portrait_path}",
                    contentDescription = "image webp"
                )
                SpaceV10()
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = uiState.entity.results[indexSelected].name,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }


            Icon(
                modifier = Modifier.width(130.dp),
                painter = painterResource(R.drawable.simpsons_logo),
                contentDescription = "simpsons",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
