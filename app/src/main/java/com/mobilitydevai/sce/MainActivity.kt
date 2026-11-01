package com.mobilitydevai.sce

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mobilitydevai.sce.ui.theme.SimpsonsInfoTheme
import com.mobilitydevai.sce.update.ForceUpdateScreen
import com.mobilitydevai.sce.update.ForceUpdateViewModel
import com.mobilitydevai.sce.view.core.navigation.NavigationWrapper
import com.mobilitydevai.sce.view.simpsons.SimpsonsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val simpsonsViewModel: SimpsonsViewModel by viewModels()
    private val forceUpdateViewModel: ForceUpdateViewModel by viewModels()

    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    // The result is not needed: if the user cancels, the blocking screen stays visible
    private val updateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            Log.d(TAG, "In-app update result: ${result.resultCode}")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }
            val updateRequired by forceUpdateViewModel.updateRequired.collectAsStateWithLifecycle()

            SimpsonsInfoTheme(darkTheme = isDarkMode) {
                if (updateRequired) {
                    // The user starts the update by tapping the button
                    ForceUpdateScreen(onUpdateClick = ::startForcedUpdate)
                } else {
                    NavigationWrapper(
                        darkThemeParam = isDarkMode,
                        simpsonsViewModel = simpsonsViewModel,
                        onSetDarkMode = { it ->
                            isDarkMode = it
                        },
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // If an immediate update was interrupted (app sent to background), resume it
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                launchImmediateUpdate(info)
            }
        }
    }

    /**
     * Uses Google Play In-App Updates (IMMEDIATE). If it is not available
     * (not installed from Play, update not yet published, etc.) opens the Play Store listing.
     */
    private fun startForcedUpdate() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { info ->
                val canUpdateInApp =
                    info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                if (canUpdateInApp) launchImmediateUpdate(info) else openPlayStore()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "In-app update not available", e)
                openPlayStore()
            }
    }

    private fun launchImmediateUpdate(info: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            info,
            updateLauncher,
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
        )
    }

    private fun openPlayStore() {
        val market = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        try {
            startActivity(market)
        } catch (_: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                )
            )
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
