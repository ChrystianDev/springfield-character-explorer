/**
 * Created by chrystian.lozada.local (1/11/26)
 */
package com.mobilitydevai.sce.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.BuildConfig
import com.mobilitydevai.sce.analytics.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForceUpdateViewModel @Inject constructor(
    private val forceUpdateRepository: ForceUpdateRepository,
    private val analyticsManager: AnalyticsManager,
) : ViewModel() {

    private val currentVersionCode = BuildConfig.VERSION_CODE.toLong()

    // Start from the cached value so the app stays blocked even when offline
    private val _updateRequired = MutableStateFlow(
        isOutdated(forceUpdateRepository.cachedMinVersionCode())
    )
    val updateRequired: StateFlow<Boolean> = _updateRequired.asStateFlow()

    init {
        checkForcedUpdate()
    }

    fun checkForcedUpdate() {
        viewModelScope.launch {
            val minVersionCode = forceUpdateRepository.fetchMinVersionCode()
            val required = isOutdated(minVersionCode)
            if (required && !_updateRequired.value) {
                analyticsManager.logCustomEvent(
                    "force_update_required",
                    mapOf(
                        "current_version_code" to currentVersionCode.toString(),
                        "min_version_code" to minVersionCode.toString()
                    )
                )
            }
            _updateRequired.value = required
        }
    }

    private fun isOutdated(minVersionCode: Long) = currentVersionCode < minVersionCode
}
