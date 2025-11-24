package com.mobilitydevai.sce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mobilitydevai.sce.ui.theme.SimpsonsInfoTheme
import com.mobilitydevai.sce.view.auth.AuthViewModel
import com.mobilitydevai.sce.view.core.navigation.NavigationWrapper
import com.mobilitydevai.sce.view.simpsons.SimpsonsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val simpsonsViewModel: SimpsonsViewModel by viewModels()
    private val authViewModel : AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }
            SimpsonsInfoTheme(darkTheme = isDarkMode) {
                NavigationWrapper(
                    darkThemeParam = isDarkMode,
                    simpsonsViewModel = simpsonsViewModel,
                    authViewModel = authViewModel,
                    onSetDarkMode = { it ->
                        isDarkMode = it
                    },
                )
            }
        }
    }
}


