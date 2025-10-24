/**
 * Created by chrystian.lozada.local (17/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.simpsonsinfo.BuildConfig
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.view.core.components.CustomBiographyText
import com.example.simpsonsinfo.view.core.components.CustomBodyText
import com.example.simpsonsinfo.view.core.components.SpaceH10
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.components.SpaceV20

@Composable
fun SimpsonsEmptyListScreen(
    onGetCharacters: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(painterResource(R.drawable.doh_homer_simpson), contentDescription = null)
        SpaceV20()
        Text(
            text = "opssss ...",
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.primary
        )
        SpaceV10()
        CustomBodyText("No se obtuvieron resultados, intentelos de nuevo__")

        TextButton(
            enabled = true,
            onClick = {
                onGetCharacters()
            }) {
            CustomBodyText(
                stringResource(R.string.main_get_characters),
                color = MaterialTheme.colorScheme.primary
            )
            SpaceH10()
            Icon(
                painterResource(R.drawable.icon_download),
                contentDescription = "Download characters",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        SpaceV20()
        CustomBiographyText("BuildType: ${BuildConfig.BUILD_TYPE}, Flavor: ${BuildConfig.FLAVOR}")
    }
}