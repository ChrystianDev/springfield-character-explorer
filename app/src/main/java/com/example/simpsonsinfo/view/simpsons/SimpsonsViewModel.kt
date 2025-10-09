/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpsonsinfo.domain.entity.SimpsonsEntity
import com.example.simpsonsinfo.domain.usecase.SimpsonsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimpsonsViewModel @Inject constructor(
    private val simpsonsUsecase: SimpsonsUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimpsonsUiState())
    var isLoading = MutableStateFlow(false)

    val uiState: StateFlow<SimpsonsUiState> = _uiState

    fun onGetSimpsons() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingData()
            delay(1000)
            val response = simpsonsUsecase("Home", "Sprinfield's citizen")
            _uiState.update { state ->
                if (response != null) {
                    state.copy(entity = response)
                }else{
                    state
                }
            }
            dataLoaded()
        }
    }

    fun isLoadingData() {
        isLoading.value = true
    }

    fun dataLoaded() {
        isLoading.value = false
    }
}

data class SimpsonsUiState(val entity: SimpsonsEntity = SimpsonsEntity(0, "", "", 0))