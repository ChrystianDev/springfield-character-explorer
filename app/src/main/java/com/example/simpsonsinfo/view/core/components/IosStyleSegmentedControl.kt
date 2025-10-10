/**
 * Created by chrystian.lozada.local (10/10/25)
 */
package com.example.simpsonsinfo.view.core.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IOSStyleSegmentedControl(
    options: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    onNavigationDetail: () -> Unit
) {
    val selectedIndex = options.indexOf(selectedOption)
    val segmentWidth = 120.dp
    val thumbOffset by animateDpAsState(targetValue = segmentWidth * selectedIndex)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onTertiary)
            .height(40.dp)
            .width(segmentWidth * options.size)
    ) {
        // Fondo deslizante (thumb)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.background.copy(0.7f))
                .width(segmentWidth)
                .fillMaxHeight()
        )

        // Texto de opciones
        Row(Modifier.fillMaxSize()) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .clickable {
                            onOptionSelected(option)
                            onNavigationDetail()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(option),
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
//                    Text(
//                        text = option,
//                        color = if (selected) Color.Black else Color.DarkGray,
//                        fontSize = 14.sp
//                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewIOSStyleSegmentedControl() {
//    var selected by remember { mutableIntStateOf(R.drawable.icon_volume_disabled) }
//    val options = listOf(R.drawable.icon_home, R.drawable.icon_favorite, R.drawable.icon_settings)
//
//    Surface(color = MaterialTheme.colorScheme.background) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            IOSStyleSegmentedControl(
//                options = options,
//                selectedOption = selected,
//                onOptionSelected = { selected = it }
//            )
//        }
//    }
//}
