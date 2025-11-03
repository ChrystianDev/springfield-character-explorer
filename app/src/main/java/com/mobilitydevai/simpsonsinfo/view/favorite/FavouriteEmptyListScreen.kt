/**
 * Created by chrystian.lozada.local (17/10/25)
 */
package com.mobilitydevai.simpsonsinfo.view.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilitydevai.simpsonsinfo.R
import com.mobilitydevai.simpsonsinfo.view.core.components.CustomBodyText
import com.mobilitydevai.simpsonsinfo.view.core.components.SpaceV10
import com.mobilitydevai.simpsonsinfo.view.core.components.SpaceV20

@Composable
fun FavouriteEmptyListScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Image(painterResource(R.drawable.homer_sad), contentDescription = null)
        SpaceV20()
        Text(
            text = stringResource(R.string.favourite_title_empty_message),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )
        SpaceV10()
        CustomBodyText(stringResource(R.string.favourite_subtitle_empty_message))
    }
}