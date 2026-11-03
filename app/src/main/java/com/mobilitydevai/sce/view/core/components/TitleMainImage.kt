package com.mobilitydevai.sce.view.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TitleMainImage(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}