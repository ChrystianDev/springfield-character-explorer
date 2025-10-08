package com.example.simpsonsinfo.view.core.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Vertical
@Composable
fun SpaceVy(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun SpaceV5() {
    Spacer(modifier = Modifier.height(5.dp))
}
@Composable
fun SpaceV10() {
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun SpaceV20() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun SpaceV40() {
    Spacer(modifier = Modifier.height(40.dp))
}


// Horizontal
@Composable
fun SpaceHx(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun SpaceH5() {
    Spacer(modifier = Modifier.width(5.dp))
}

@Composable
fun SpaceH10() {
    Spacer(modifier = Modifier.width(10.dp))
}

@Composable
fun SpaceH20() {
    Spacer(modifier = Modifier.width(20.dp))
}

@Composable
fun SpaceH40() {
    Spacer(modifier = Modifier.width(40.dp))
}