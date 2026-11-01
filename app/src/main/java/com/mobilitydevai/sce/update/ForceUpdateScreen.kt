/**
 * Created by chrystian.lozada.local (1/11/26)
 */
package com.mobilitydevai.sce.update

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilitydevai.sce.R
import com.mobilitydevai.sce.ui.theme.DarkBackground
import com.mobilitydevai.sce.view.core.components.SpaceV10
import com.mobilitydevai.sce.view.core.components.SpaceV20

/** Blocking screen: the user cannot use the app until it is updated. */
@Composable
fun ForceUpdateScreen(onUpdateClick: () -> Unit) {
    // Back does nothing: the screen cannot be dismissed
    BackHandler(enabled = true) {}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 32.dp)
    ) {
        Icon(
            modifier = Modifier.width(160.dp),
            painter = painterResource(R.drawable.simpsons_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        SpaceV20()
        Text(
            text = stringResource(R.string.force_update_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        SpaceV10()
        Text(
            text = stringResource(R.string.force_update_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        SpaceV20()
        Button(onClick = onUpdateClick) {
            Text(
                text = stringResource(R.string.force_update_button),
                style = MaterialTheme.typography.labelMedium,
                color = DarkBackground
            )
        }
    }
}
