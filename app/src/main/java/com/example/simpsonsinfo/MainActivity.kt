package com.example.simpsonsinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
            SimpsonsInfoTheme {
                NavigationWrapper(context = this, simpsonsViewModel = simpsonsViewModel)
            }
        }
    }
}


