package com.mobilitydevai.sce.view.core.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Vertical
@Composable
fun SpaceV10() {
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun SpaceV20() {
    Spacer(modifier = Modifier.height(20.dp))
}


// Horizontal
@Composable
fun SpaceH10() {
    Spacer(modifier = Modifier.width(10.dp))
}