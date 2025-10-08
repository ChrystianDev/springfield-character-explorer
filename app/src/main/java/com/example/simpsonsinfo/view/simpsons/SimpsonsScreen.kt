/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.view.core.components.CustomText

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
            .padding(20.dp)
            .scrollable(
                state = rememberScrollState(initial = 0),
                orientation = Orientation.Vertical
            ),
    ) {
        if(isLoading){
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = Color.Yellow,
                strokeWidth = 1.dp
                )
        }else{
            CustomText("Nombre: ")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Yellow.copy(0.5f))
                    .padding(vertical = 20.dp)
            ) {
                CustomText("Data: ${uiState.next} ")
            }
            TextButton(onClick = {
//                simpsonsViewModel.dataLoaded()
//                simpsonsViewModel.isLoadingData()
                simpsonsViewModel.onGetSimpsons()
            }) {
                CustomText("Get Simpsons", color = Color.Yellow)
            }
            Icon(
                modifier = Modifier.size(200.dp),
                painter = painterResource(R.drawable.meta_logo_2),
                contentDescription = "simpsons",
                tint = Color.Yellow
            )
        }
    }
}