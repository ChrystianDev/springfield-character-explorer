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
import com.example.simpsonsinfo.R

@Composable
fun IOSStyleSegmentedControl(
    options: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
) {
    val selectedIndex = options.indexOf(selectedOption)
    val segmentWidth = 120.dp
    val thumbOffset by animateDpAsState(targetValue = segmentWidth * selectedIndex)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onTertiary)
            .height(60.dp)
            .width(segmentWidth * options.size)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.background.copy(0.7f))
                .width(segmentWidth)
                .fillMaxHeight()
        )

        Row(Modifier.fillMaxSize()) {
            options.forEach { option ->
               val icon =  when (option) {
                    0 -> R.drawable.icon_home
                    1 -> R.drawable.icon_location
                    2 -> R.drawable.icon_favorite
                    else -> R.drawable.icon_home
                }
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .clickable {
                            onOptionSelected(option)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
