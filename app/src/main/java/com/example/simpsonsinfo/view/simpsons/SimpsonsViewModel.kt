/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.view.simpsons

import android.util.Log
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

    init {
        onGetSimpsons()
    }

    private val _uiState = MutableStateFlow(SimpsonsUiState())
    val uiState: StateFlow<SimpsonsUiState> = _uiState
    private val _indexSelected = MutableStateFlow(0)
    val indexSelected: StateFlow<Int> = _indexSelected
    var isLoading = MutableStateFlow(false)

    private val _page = MutableStateFlow("1")

    fun onGetSimpsons() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingData()
            delay(2000)
            val response = simpsonsUsecase(page = _page.value)
            _uiState.update { state ->
                if (response != null) {
                    state.copy(entity = response)
                } else {
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

    fun setIndexSelected(index: Int) {
        Log.i("indexSelected", index.toString())
        _indexSelected.value = index
    }

    fun upPage(){
        var pageNumber = _page.value.toIntOrNull()
        if (pageNumber != null) {
            pageNumber++
            _page.value = pageNumber.toString()
        }
    }

    fun downPage(){
        var pageNumber = _page.value.toIntOrNull()
        if (pageNumber != null) {
            pageNumber--
            _page.value = pageNumber.toString()
        }
    }

}

data class SimpsonsUiState(
    val entity: SimpsonsEntity = SimpsonsEntity(
        0,
        "",
        "",
        0,
        results = emptyList()
    )
)