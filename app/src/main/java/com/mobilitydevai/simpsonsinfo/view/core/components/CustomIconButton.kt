package com.mobilitydevai.simpsonsinfo.view.core.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    icon: Int,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    iconDescription: String
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
        ),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            painterResource(icon),
            contentDescription = iconDescription,
            tint = contentColor
        )
    }
}