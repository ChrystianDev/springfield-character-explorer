package com.example.simpsonsinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.simpsonsinfo.ui.theme.SimpsonsInfoTheme
import com.example.simpsonsinfo.view.core.navigation.NavigationWrapper
import com.example.simpsonsinfo.view.simpsons.SimpsonsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val simpsonsViewModel: SimpsonsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }
            SimpsonsInfoTheme(darkTheme = isDarkMode) {
                NavigationWrapper(
                    darkThemeParam = isDarkMode,
                    simpsonsViewModel = simpsonsViewModel,
                    onSetDarkMode = { it ->
                        isDarkMode = it
                    },
                )
            }
        }
    }
}


