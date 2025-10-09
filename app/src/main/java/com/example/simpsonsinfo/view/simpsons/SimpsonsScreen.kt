/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.ui.theme.BlackDev
import com.example.simpsonsinfo.ui.theme.DarkBackground
import com.example.simpsonsinfo.ui.theme.GrayDev30
import com.example.simpsonsinfo.ui.theme.ImageBackground
import com.example.simpsonsinfo.ui.theme.shapes
import com.example.simpsonsinfo.view.core.components.CustomText
import com.example.simpsonsinfo.view.core.components.SpaceH10
import com.example.simpsonsinfo.view.core.components.SpaceV10

@Composable
fun SimpsonsScreen(
    simpsonsViewModel: SimpsonsViewModel = hiltViewModel()
) {

    val uiState by simpsonsViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by simpsonsViewModel.isLoading.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
//            .padding(20.dp)
            .scrollable(
                state = rememberScrollState(initial = 0),
                orientation = Orientation.Vertical
            ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = Color.Yellow,
                strokeWidth = 1.dp
            )
        } else {
//            Image(painterResource(R.drawable.ic_launcher_foreground), contentDescription = null)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(0.5f))
                    .padding(vertical = 20.dp)
            ) {
                LazyRow {
                    items(uiState.entity.results.size) { index ->
                        AsyncImage(
                            modifier = Modifier
                                .clip(shapes.medium)
                                .padding(horizontal = 5.dp)
                                .background(ImageBackground)
                                .padding(10.dp),
                            model = "https://cdn.thesimpsonsapi.com/200${uiState.entity.results[index].portrait_path}",
                            contentDescription = "image webp"
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ImageBackground)
                        .padding(10.dp),
                    model = "https://cdn.thesimpsonsapi.com/500/character/1.webp",
                    contentDescription = "image webp"
                )
                SpaceV10()
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Homer Simpson",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                    )
//                Text(text = uiState.entity.results[0].name, color = MaterialTheme.colorScheme.primary)
            }
            TextButton(onClick = {
                simpsonsViewModel.onGetSimpsons()
            }) {
                CustomText("Get Characters", color = MaterialTheme.colorScheme.primary)
                SpaceH10()
                Icon(
                    painterResource(R.drawable.icon_download),
                    contentDescription = "Download characters",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Icon(
//                modifier = Modifier.size(200.dp),
                painter = painterResource(R.drawable.simpsons_logo),
                contentDescription = "simpsons",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}