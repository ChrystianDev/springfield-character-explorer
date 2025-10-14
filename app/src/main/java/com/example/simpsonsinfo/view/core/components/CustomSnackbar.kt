/**
 * Created by chrystian.lozada.local (14/10/25)
 */
package com.example.simpsonsinfo.view.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpsonsinfo.ui.theme.shapes

@Composable
fun CustomSnackBar(
    message: String = "Message",
    contentColor: Color = MaterialTheme.colorScheme.background,
    containerColor: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(shapes.medium)
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 20.dp)

    ) {
        Text(
            message,
            fontSize = 18.sp,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}