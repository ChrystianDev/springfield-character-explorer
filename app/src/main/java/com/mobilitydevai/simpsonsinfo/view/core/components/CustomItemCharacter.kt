package com.mobilitydevai.simpsonsinfo.view.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilitydevai.simpsonsinfo.R

@Composable
fun CustomItemCharacter(
    text: String,
    onClick: () -> Unit,
    isLastItem: Boolean = false,
    isPlaying: Boolean = false,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        )
        {
            CustomBodyText(
                modifier = Modifier.weight(4f),
                text = text,
                textAlign = TextAlign.Start
            )
            CustomIconButton(
                modifier = Modifier.weight(1f),
//                icon = if(isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
                // TODO Capturar el final de la reproducción
                icon = if(isPlaying) R.drawable.icon_play else R.drawable.icon_play,
                iconDescription = "Play audio",
                onClick = {
                    onClick()
                })
        }
        if(!isLastItem)
        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 10.dp
            ),
            thickness = 0.5.dp
        )
    }
}