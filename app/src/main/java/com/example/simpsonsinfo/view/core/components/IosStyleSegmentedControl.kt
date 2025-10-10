/**
 * Created by chrystian.lozada.local (10/10/25)
 */
package com.example.simpsonsinfo.view.core.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IOSStyleSegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val selectedIndex = options.indexOf(selectedOption)
    val segmentWidth = 100.dp
    val thumbOffset by animateDpAsState(targetValue = segmentWidth * selectedIndex)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE5E5EA)) // gris claro estilo iOS
            .height(40.dp)
            .width(segmentWidth * options.size)
    ) {
        // Fondo deslizante (thumb)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .width(segmentWidth)
                .fillMaxHeight()
        )

        // Texto de opciones
        Row(Modifier.fillMaxSize()) {
            options.forEach { option ->
                val selected = option == selectedOption
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .clickable { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (selected) Color.Black else Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewIOSStyleSegmentedControl() {
    var selected by remember { mutableStateOf("Today") }
    val options = listOf("Today", "Week", "Month")

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            IOSStyleSegmentedControl(
                options = options,
                selectedOption = selected,
                onOptionSelected = { selected = it }
            )
        }
    }
}
