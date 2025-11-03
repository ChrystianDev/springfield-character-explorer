package com.mobilitydevai.simpsonsinfo.view.core.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomLoading() {
    CircularProgressIndicator(
    modifier = Modifier.size(100.dp),
    color = Color.Yellow,
    strokeWidth = 1.dp
)
}